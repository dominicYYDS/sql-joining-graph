package com.dominicyyds.sqljoininggraph.service;

import com.dominicyyds.sqljoininggraph.entity.SqlJoiningGraphSettings;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;

@Service(Service.Level.PROJECT)
@State(name = "sql-joining-graph", storages = @Storage("sql-joining-graph.xml"))
public class SqlJoiningGraphSettingsService implements PersistentStateComponent<SqlJoiningGraphSettings> {

    private Project project;

    private static final String DEFAULT_OUTPUT_FILE = "sql-joining-graph.svg";

    public SqlJoiningGraphSettingsService(Project project) {
        this.project = project;
    }

    private SqlJoiningGraphSettings settings;

    @Override
    public @Nullable SqlJoiningGraphSettings getState() {
        return settings;
    }

    @Override
    public void loadState(@NotNull SqlJoiningGraphSettings state) {
        settings = new SqlJoiningGraphSettings();
        if (StringUtils.isBlank(state.getOutputFile()) && project != null && StringUtils.isNotBlank(project.getBasePath())) {
            state.setOutputFile(Paths.get(project.getBasePath(), DEFAULT_OUTPUT_FILE).toString());
        }
        XmlSerializerUtil.copyBean(state, settings);
    }
}
