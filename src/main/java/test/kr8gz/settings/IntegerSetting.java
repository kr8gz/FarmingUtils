package test.kr8gz.settings;

public class IntegerSetting extends NumberSetting<Integer> {
    public IntegerSetting(Settings settings,
                          String key, String description, Integer defaultValue,
                          int min, int max, int step) {
        super(settings, key, description, defaultValue, min, max, step);
        assert step > 0;
        assert (max - min) % step == 0;
    }

    @Override
    public boolean canSet(Integer newValue) {
        return newValue >= min && newValue <= max && (newValue - min) % step == 0;
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
