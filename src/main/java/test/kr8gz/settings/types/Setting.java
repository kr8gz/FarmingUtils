package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

public abstract class Setting<T> extends Settings.AbstractSetting<T> {
    protected T value;

    public Setting(Settings settings,
                   String key, String description, T defaultValue) {
        super(settings, key, description);
        this.value = defaultValue;
    }

    public boolean canSet(T newValue) {
        return true;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public boolean set(T newValue) {
        boolean canSet = this.canSet(newValue);
        if (canSet) {
            this.value = newValue;
            this.save();
        }
        return canSet;
    }

    @Override
    public String toString() {
        return this.key + "=" + this.get();
    }
}
