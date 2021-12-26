package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

public class OverlayPositionSetting extends Setting<Integer> {
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

    @Override
    public String toString() {
        return this.key + "=" + this.get();
    }
}
