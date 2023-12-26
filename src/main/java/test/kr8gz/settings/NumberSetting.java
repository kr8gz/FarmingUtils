package test.kr8gz.settings;

public abstract class NumberSetting<T extends Number> extends SimpleSetting<T> {
    public final T min;
    public final T max;
    public final T step;

    public NumberSetting(Settings settings,
                         String key, String description, T defaultValue,
                         T min, T max, T step) {
        super(settings, key, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }
}
