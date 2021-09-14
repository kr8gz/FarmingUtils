package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(Settings settings,
                          String key, String description, Boolean defaultValue) {
        super(settings, key, description, defaultValue);
    }

    @Override
    public boolean setFromString(String string) {
        value = Boolean.parseBoolean(string);
        return true;
    }
}
