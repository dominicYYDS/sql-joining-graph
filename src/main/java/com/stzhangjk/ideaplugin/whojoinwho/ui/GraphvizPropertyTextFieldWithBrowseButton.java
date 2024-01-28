package com.stzhangjk.ideaplugin.whojoinwho.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GraphvizPropertyTextFieldWithBrowseButton extends TextFieldWithBrowseButton {

    @NotNull
    @Override
    protected Icon getDefaultIcon() {
        return AllIcons.General.InlineVariables;
    }

    @NotNull
    @Override
    protected Icon getHoveredIcon() {
        return AllIcons.General.InlineVariablesHover;
    }

}
