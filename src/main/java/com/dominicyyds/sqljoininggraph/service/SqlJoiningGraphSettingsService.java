package com.dominicyyds.sqljoininggraph.service;

import com.dominicyyds.sqljoininggraph.entity.SqlJoiningGraphSettings;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
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
}
