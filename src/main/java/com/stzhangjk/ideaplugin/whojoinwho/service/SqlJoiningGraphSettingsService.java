package com.stzhangjk.ideaplugin.whojoinwho.service;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.stzhangjk.ideaplugin.whojoinwho.entity.SqlJoiningGraphSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service(Service.Level.PROJECT)
@State(name = "sql-joining-graph", storages = @Storage("sql-joining-graph.xml"))
public class SqlJoiningGraphSettingsService implements PersistentStateComponent<SqlJoiningGraphSettings> {

    private SqlJoiningGraphSettings settings;

    @Override
    public @Nullable SqlJoiningGraphSettings getState() {
        return settings;
    }

    @Override
    public void loadState(@NotNull SqlJoiningGraphSettings state) {
        settings = new SqlJoiningGraphSettings();
        XmlSerializerUtil.copyBean(state, settings);
    }

    public SqlJoiningGraphSettings getSettings() {
        return settings;
    }

    public SqlJoiningGraphSettingsService setSettings(SqlJoiningGraphSettings settings) {
        this.settings = settings;
        return this;
    }
}
