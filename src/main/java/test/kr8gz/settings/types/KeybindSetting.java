package test.kr8gz.settings.types;

import org.lwjgl.input.Keyboard;
import test.kr8gz.settings.Settings;

public class KeybindSetting extends Setting<Integer> {
    public KeybindSetting(Settings settings,
                          String key, String description, Integer defaultValue) {
        super(settings, key, description, defaultValue);
    }

    @Override
    public boolean canSet(Integer newValue) {
        return Keyboard.getKeyName(newValue) != null;
    }

    @Override
    public boolean setFromString(String string) {
        int i = Integer.parseInt(string);
        if (canSet(i)) {
            value = i;
            return true;
        } else {
            return false;
        }
    }
}
