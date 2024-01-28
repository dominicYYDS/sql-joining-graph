package com.stzhangjk.ideaplugin.whojoinwho;

import ch.intellij.chview.psiresolvers.PsiExpressionListResolver;
import ch.tools.intellij.psi.PsiTools;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.ui.content.Content;
import com.stzhangjk.ideaplugin.whojoinwho.entity.JoinEntry;
import com.stzhangjk.ideaplugin.whojoinwho.ui.GraphvizPropertyPanel;
import com.stzhangjk.ideaplugin.whojoinwho.utils.ExtractUtil;
import com.stzhangjk.ideaplugin.whojoinwho.utils.GraphvizUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class WhoJoinWhoAction extends AnAction implements ToolWindowFactory {

    private GraphvizPropertyPanel propertyPanel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        VerticalLayout layout = new VerticalLayout(10);
        JPanel whole = new JPanel(layout);
        whole.add(new GraphvizPropertyPanel());

        Content content = toolWindow.getContentManager().getFactory().createContent(whole, null, true);
        toolWindow.getContentManager().addContent(content);
        System.out.println(toolWindow.getContentManager().getContentCount());

    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        VirtualFile fileSelected = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (fileSelected == null) {
            System.out.println("没选中文件");
            return;
        }

        Set<JoinEntry> joins = new TreeSet<>(JoinEntry.COMPARATOR);
        VfsUtilCore.iterateChildrenRecursively(fileSelected, f -> true, fileOrDir -> {
            if (fileOrDir.isDirectory() || !"java".equals(fileOrDir.getExtension())) {
                return true;
            }
            System.out.printf("处理[%s]\n", fileOrDir.getPath());
            PsiFile psiFile = PsiUtilCore.getPsiFile(event.getProject(), fileOrDir);
            psiFile.accept(new JavaRecursiveElementVisitor() {

                @Override
                public void visitVariable(PsiVariable variable) {
                    System.out.println("==============");
                    System.out.printf("处理[%s]\n", variable.getName());
//                    for (PsiElement child : variable.getChildren()) {
//                        System.out.printf("name=\"%s\", text=\"%s\", type=\"%s\"\n", variable.getName(), child.getText(), child.getClass());
//                    }

                    if (variable.getInitializer() == null) {
                        return;
                    }

                    if ("String".equals(variable.getTypeElement().getText())
                            && variable.getInitializer().getText().contains("\"")
                            && !(variable.getInitializer() instanceof PsiMethodCallExpression)
                            && !(variable.getInitializer() instanceof PsiExpressionListResolver)
                            && PsiTreeUtil.getChildrenOfTypeAsList(variable.getInitializer(), PsiMethodCallExpression.class).isEmpty()) {
                        System.out.println(variable.getInitializer().getText());
                        String sql = variable.getInitializer() instanceof PsiLiteralExpression
                                ? variable.getInitializer().getText().replace("\"", "")
                                : PsiTools.psiElementToString(variable.getInitializer());
                        System.out.printf("%s=\"%s\"\n", variable.getName(), sql);
                        joins.addAll(extract(sql));
                    }
                }
            });
            return true;
        });
        try {
            GraphvizUtil.draw(joins, new File(event.getProject().getBasePath() + "/db.svg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<JoinEntry> extract(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Select) {
                Set<JoinEntry> js = ExtractUtil.extract(((Select) statement));
                System.out.println(js);
                return js;
            }
        } catch (JSQLParserException e) {
//           System.out.printf("解析sql失败[%s]\n", e.getMessage());
        }
        return new TreeSet<>(JoinEntry.COMPARATOR);
    }
}