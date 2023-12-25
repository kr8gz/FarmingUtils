package test.kr8gz.settings;

import mod.kr8gz.farmingutils.util.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings {
    private final Path path;
    public final ArrayList<AbstractSetting<?>> settingsList = new ArrayList<>();

    public Settings(String path) {
        this.path = Paths.get(path);
        Helper.createFile(this.path);
    }

    public static abstract class AbstractSetting<T> {
        protected final Settings settings;
        public final String key;
        public final String description;

        public AbstractSetting(Settings settings, String key, String description) {
            settings.settingsList.add(this);
            this.settings = settings;
            this.key = key;
            this.description = description;
        }

        public abstract T get();

        public abstract boolean set(T newValue);

        public abstract void reset();

        protected void save() {
            try {
                List<String> tmp = new ArrayList<>();
                this.settings.settingsList.forEach(e -> tmp.add(e.toString()));
                Files.write(this.settings.path, tmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * @return whether the set was successful
         **/
        public abstract boolean setFromString(String string);

        @Override
        public abstract String toString();
    }

    public void load() {
        HashMap<String, String> map = new HashMap<>();
        try {
            for (String line : Files.readAllLines(this.path)) {
                int sep = line.indexOf('=');
                if (sep != -1) {
                    map.put(line.substring(0, sep), line.substring(sep + 1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (AbstractSetting<?> s : settingsList) {
            try {
                if (map.containsKey(s.key) && s.setFromString(map.get(s.key))) {
                    continue;
                }
            } catch (NumberFormatException ignored) {}
            s.reset();
        }
    }
}
