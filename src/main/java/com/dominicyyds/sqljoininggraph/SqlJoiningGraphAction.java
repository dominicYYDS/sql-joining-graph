package com.dominicyyds.sqljoininggraph;

import com.dominicyyds.sqljoininggraph.computers.PsiJavaFileStringConstComputer;
import com.dominicyyds.sqljoininggraph.entity.JoinEntry;
import com.dominicyyds.sqljoininggraph.entity.SqlJoiningGraphSettings;
import com.dominicyyds.sqljoininggraph.service.SqlJoiningGraphSettingsService;
import com.dominicyyds.sqljoininggraph.utils.ExtractUtil;
import com.dominicyyds.sqljoininggraph.utils.GraphvizUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiUtilCore;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SqlJoiningGraphAction extends AnAction {

    private static final Logger log = Logger.getInstance(SqlJoiningGraphAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        VirtualFile fileSelected = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (fileSelected == null) {
            log.info("没选中文件");
            return;
        }

        NotificationGroupManager.getInstance()
                .getNotificationGroup("SqlJoiningGraph Notification Group")
                .createNotification("Start generating table diagram!! Please wait a few...", NotificationType.INFORMATION)
                .notify(event.getProject());

        Set<JoinEntry> joins = new TreeSet<>(JoinEntry.COMPARATOR);
        VfsUtilCore.iterateChildrenRecursively(fileSelected, f -> true, fileOrDir -> {
            if (fileOrDir.isDirectory() || !(fileOrDir.getFileType() instanceof JavaFileType)) {
                return true;
            }
            log.info(String.format("处理[%s]\n", fileOrDir.getPath()));
            PsiFile psiFile = PsiUtilCore.getPsiFile(event.getProject(), fileOrDir);
            if (psiFile instanceof PsiJavaFile) {
                List<String> sqls = PsiJavaFileStringConstComputer.INSTANCE.compute((PsiJavaFile) psiFile);
                sqls.stream().map(this::extract).forEach(joins::addAll);
            }
            return true;
        });

        try {
            SqlJoiningGraphSettingsService settingsService = event.getProject().getService(SqlJoiningGraphSettingsService.class);
            SqlJoiningGraphSettings settings = settingsService.getState();
            GraphvizUtil.draw(joins, new File(settings.getOutputFile()), settings);
            ProjectView.getInstance(event.getProject()).refresh();
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("SqlJoiningGraph Notification Group")
                    .createNotification(String.format("sql joining graph finished!! at %s", settings.getOutputFile()), NotificationType.INFORMATION)
                    .notify(event.getProject());
        } catch (IOException e) {
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("SqlJoiningGraph Notification Group")
                    .createNotification(String.format("sql joining graph error: %s", e.getLocalizedMessage()), NotificationType.ERROR)
                    .notify(event.getProject());
            throw new RuntimeException(e);
        }
    }

    private Set<JoinEntry> extract(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Select) {
                Set<JoinEntry> js = ExtractUtil.extract(((Select) statement));
                log.info(js.toString());
                return js;
            }
        } catch (JSQLParserException e) {
//            e.printStackTrace();
//           log.info(String.format("解析sql失败[%s]\n", e.getMessage()));
        }
        return new TreeSet<>(JoinEntry.COMPARATOR);
    }
}