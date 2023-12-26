package test.kr8gz.settings;

public class BooleanSetting extends SimpleSetting<Boolean> {
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
