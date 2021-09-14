package test.kr8gz.settings.types;

import test.kr8gz.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FixedLengthListSetting<T> extends AbstractListSetting<T> {
    public FixedLengthListSetting(Settings settings,
                                  String key, String description, T[] defaultValues) {
        this(settings, key, description, new ArrayList<>(Arrays.asList(defaultValues)));
    }

    public FixedLengthListSetting(Settings settings,
                                  String key, String description, List<T> defaultValues) {
        super(settings, key, description, defaultValues);
    }
}
