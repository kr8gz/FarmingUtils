package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractListSetting<T> extends Settings.AbstractSetting<List<T>> {
    protected List<T> values;
    protected final List<T> defaultValues;

    public AbstractListSetting(Settings settings,
                               String key, String description, T[] defaultValues) {
        this(settings, key, description, new ArrayList<>(Arrays.asList(defaultValues)));
    }

    public AbstractListSetting(Settings settings,
                               String key, String description, List<T> defaultValues) {
        super(settings, key, description);
        this.values = defaultValues;
        this.defaultValues = new ArrayList<>(defaultValues);
    }

    @Override
    public boolean setFromString(String string) {
        boolean inString = false;
        boolean isEscape = false;
        StringBuilder currentValue = new StringBuilder();
        values.clear();

        for (char c : string.toCharArray()) {
            if (c == '\\') {
                isEscape = true;
            } else {
                if (c == '"' && !isEscape) {
                    inString = !inString;
                } else if (isEscape && c != '"') {
                    currentValue.append("\\").append(c);
                } else if (inString) {
                    currentValue.append(c);
                } else if (c == ';') {
                    T newValue = parseSingleElementFromString(currentValue.toString());
                    if (canSet(newValue)) {
                        values.add(newValue);
                        currentValue.setLength(0); // reset
                    } else {
                        return false;
                    }
                }
                isEscape = false;
            }
        }
        return true;
    }

    protected abstract T parseSingleElementFromString(String string);

    public abstract boolean canSet(T e);

    @Override
    public List<T> get() {
        return this.values;
    }

    public boolean set(int index, T e) {
        boolean canSet = this.canSet(e);
        if (canSet) {
            this.values.set(index, e);
            this.save();
        }
        return canSet;
    }

    @Override
    public boolean set(List<T> list) {
        this.values.clear();
        boolean success = true;
        for (T e : list) {
            if (this.canSet(e)) {
                this.values.add(e);
            } else {
                success = false;
                this.values = new ArrayList<>(this.defaultValues);
                break;
            }
        }
        this.save();
        return success;
    }

    public void reset() {
        this.values = new ArrayList<>(this.defaultValues);
        this.save();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.key + "=");
        this.values.forEach(e -> stringBuilder
                .append("\"")
                .append(e.toString().replace("\"", "\\\""))
                .append("\";")
        );
        return stringBuilder.toString();
    }
}
