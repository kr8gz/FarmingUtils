package test.kr8gz.settings;

public abstract class ListSetting<T> extends AbstractListSetting<T> {
    public final int minValues;
    public final int maxValues;

    public ListSetting(Settings settings,
                       String key, String description, T[] defaultValues) {
        super(settings, key, description, defaultValues);
        this.minValues = 0;
        this.maxValues = Integer.MAX_VALUE;
    }

    public ListSetting(Settings settings,
                       String key, String description, T[] defaultValues, int minValues) {
        super(settings, key, description, defaultValues);
        this.minValues = minValues;
        this.maxValues = Integer.MAX_VALUE;
    }

    public ListSetting(Settings settings,
                       String key, String description, T[] defaultValues, int minValues, int maxValues) {
        super(settings, key, description, defaultValues);
        this.minValues = minValues;
        this.maxValues = maxValues;
    }

    public boolean add(T e) {
        boolean canSet = this.canSet(e) && this.values.size() >= minValues && this.values.size() <= maxValues;
        if (canSet) {
            this.values.add(e);
            this.save();
        }
        return canSet;
    }

    public boolean add(int index, T e) {
        boolean canSet = this.canSet(e) && this.values.size() >= minValues && this.values.size() <= maxValues;
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
