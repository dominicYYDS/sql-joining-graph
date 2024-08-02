package com.dominicyyds.sqljoininggraph.utils;

import com.intellij.execution.util.EnvironmentVariable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingsUtil {

    public static String stringifyProps(Map<String, String> props) {
        if (props == null || props.isEmpty()) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> entry : props.entrySet()) {
            if (buf.length() > 0) {
                buf.append(";");
            }
            buf.append(StringUtil.escapeChar(entry.getKey(), ';')).append("=").append(StringUtil.escapeChar(entry.getValue(), ';'));
        }
        return buf.toString();
    }

    public static String stringifyProps(List<EnvironmentVariable> envs) {
        if (envs == null || envs.isEmpty()) {
            return "";
        }
        return envs.stream()
                .map(prop -> String.format("%s=%s", StringUtil.escapeChar(prop.getName(), ';'), StringUtil.escapeChar(prop.getValue(), ';')))
                .collect(Collectors.joining(";"));
    }

    public static List<EnvironmentVariable> convertToVariables(Map<String, String> map, final boolean readOnly) {
        if (map == null || map.isEmpty()) {
            return new ArrayList<>();
        }
        return ContainerUtil.map(map.entrySet(), entry -> new EnvironmentVariable(entry.getKey(), entry.getValue(), readOnly) {
            @Override
            public boolean getNameIsWriteable() {
                return !readOnly;
            }
        });
    }

}
