package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ListSetting<T> extends AbstractListSetting<T> {
    public ListSetting(Settings settings,
                       String key, String description, T[] defaultValues) {
        this(settings, key, description, new ArrayList<>(Arrays.asList(defaultValues)));
    }

    public ListSetting(Settings settings,
                       String key, String description, List<T> defaultValues) {
        super(settings, key, description, defaultValues);
    }

    public boolean add(T e) {
        boolean canSet = this.canSet(e);
        if (canSet) {
            this.values.add(e);
            this.save();
        }
        return canSet;
    }

    public boolean add(int index, T e) {
        boolean canSet = this.canSet(e);
        if (canSet) {
            this.values.add(index, e);
            this.save();
        }
        return canSet;
    }

    public void remove(T e) {
        this.values.remove(e);
        this.save();
    }

    public void remove(int index) {
        this.values.remove(index);
        this.save();
    }

    public void clear() {
        this.values.clear();
        this.save();
    }
}
