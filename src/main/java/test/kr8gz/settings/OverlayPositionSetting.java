package test.kr8gz.settings;

public class OverlayPositionSetting extends SimpleSetting<Integer> {
    public OverlayPositionSetting(Settings settings, String key, Integer defaultValue) {
        super(settings, key, null, defaultValue);
    }

    @Override
    public boolean setFromString(String string) {
        int i = Integer.parseInt(string);
        if (canSet(i)) {
            value = i;
            this.save();
            return true;
        } else {
            return false;
        }
    }
}
