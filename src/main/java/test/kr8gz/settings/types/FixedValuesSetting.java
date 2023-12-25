package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FixedValuesSetting<T> extends SimpleSetting<T> {
    protected final List<T> allowed;
    protected int current;

    public FixedValuesSetting(Settings settings,
                              String key, String description, T[] values) {
        this(settings, key, description, new ArrayList<>(Arrays.asList(values)));
    }

    public FixedValuesSetting(Settings settings,
                              String key, String description, List<T> values) {
        super(settings, key, description, values.get(0));
        this.allowed = values;
        this.current = 0;
    }

    @Override
    public boolean canSet(T newValue) {
        return allowed.contains(newValue);
    }

    @Override
    public T get() {
        return this.allowed.get(current);
    }

    @Override
    public boolean set(T newValue) {
        int index = this.allowed.indexOf(newValue);
        if (index != -1) {
            this.current = index;
            return true;
        } else {
            return false;
        }
    }
}
