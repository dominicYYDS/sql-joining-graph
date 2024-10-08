package com.dominicyyds.sqljoininggraph.ui;

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
        Content content = toolWindow.getContentManager()
                .getFactory()
                .createContent(new SqlJoiningGraphToolWindow(project, toolWindow), null, true);
        toolWindow.getContentManager().addContent(content);

    }

}
