package com.stzhangjk.ideaplugin.whojoinwho.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SqlJoiningGraphToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        VerticalLayout layout = new VerticalLayout(10);
        JPanel whole = new JPanel(layout);
        whole.add(new GraphvizPropertyPanel(project));

        Content content = toolWindow.getContentManager().getFactory().createContent(whole, null, true);
        toolWindow.getContentManager().addContent(content);

    }

}
