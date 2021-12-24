package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(Settings settings,
                          String key, String description, Boolean defaultValue) {
        super(settings, key, description, defaultValue);
    }

    @Override
    public boolean setFromString(String string) {
        if (string.equals("false")) {
            value = false;
        } else if (string.equals("true")) {
            value = true;
        } else {
            return false;
        }
        this.save();
        return true;
    }
}
