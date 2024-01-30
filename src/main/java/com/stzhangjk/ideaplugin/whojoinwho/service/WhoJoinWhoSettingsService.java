package com.stzhangjk.ideaplugin.whojoinwho.service;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.stzhangjk.ideaplugin.whojoinwho.entity.WhoJoinWhoSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service(Service.Level.PROJECT)
@State(name = "WhoJoinWho", storages = @Storage("who-join-who.xml"))
public class WhoJoinWhoSettingsService implements PersistentStateComponent<WhoJoinWhoSettings> {

    private WhoJoinWhoSettings settings;

    @Override
    public @Nullable WhoJoinWhoSettings getState() {
        return settings;
    }

    @Override
    public void loadState(@NotNull WhoJoinWhoSettings state) {
        settings = new WhoJoinWhoSettings();
        XmlSerializerUtil.copyBean(state, settings);
    }

    public WhoJoinWhoSettings getSettings() {
        return settings;
    }

    public WhoJoinWhoSettingsService setSettings(WhoJoinWhoSettings settings) {
        this.settings = settings;
        return this;
    }
}
