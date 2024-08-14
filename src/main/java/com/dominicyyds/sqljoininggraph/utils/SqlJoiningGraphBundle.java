package com.dominicyyds.sqljoininggraph.utils;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public final class SqlJoiningGraphBundle {

    @NonNls
    public static final String BUNDLE = "messages.SqlJoiningGraphBundle";
    @NonNls
    @PropertyKey(resourceBundle = BUNDLE)
    public static final String GENERATE_START = "generate.start";
    @NonNls
    @PropertyKey(resourceBundle = BUNDLE)
    public static final String GENERATE_FAIL = "generate.fail";
    @NonNls
    @PropertyKey(resourceBundle = BUNDLE)
    public static final String GENERATE_EMPTY = "generate.empty";
    @NonNls
    @PropertyKey(resourceBundle = BUNDLE)
    public static final String GENERATE_SUCCESS = "generate.success";
    @NonNls
    @PropertyKey(resourceBundle = BUNDLE)
    public static final String GENERATE_FAIL_UNKNOWN = "generate.fail.unknown";
    @NonNls
    @PropertyKey(resourceBundle = BUNDLE)
    public static final String TOOLWINDOW_TITLE_SETTINGS = "toolwindow.title.settings";
    @NonNls
    @PropertyKey(resourceBundle = BUNDLE)
    public static final String TOOLWINDOW_TITLE_DETAILS = "toolwindow.title.details";

    private static final DynamicBundle INSTANCE = new DynamicBundle(BUNDLE);

    private SqlJoiningGraphBundle() {}

    public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

    public static Supplier<@Nls String> lazyMessage(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.getLazyMessage(key, params);
    }
}