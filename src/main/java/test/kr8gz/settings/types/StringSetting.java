package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

import java.util.regex.Pattern;

public class StringSetting extends Setting<String> {
    Pattern pattern;

    public StringSetting(Settings settings,
                         String key, String description, String defaultValue) {
        this(settings, key, description, defaultValue, null);
    }

    public StringSetting(Settings settings,
                         String key, String description, String defaultValue,
                         String pattern) {
        super(settings, key, description, defaultValue);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean canSet(String newValue) {
        if (pattern != null) {
            return pattern.matcher(newValue).matches();
        } else {
            return true;
        }
    }

    @Override
    public boolean setFromString(String string) {
        if (canSet(string)) {
            value = string;
            return true;
        } else {
            return false;
        }
    }
}
