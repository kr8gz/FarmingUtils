package test.kr8gz.settings;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Settings {
    private final Path path;
    private boolean isInit = false;
    public final ArrayList<AbstractSetting<?>> settingsList = new ArrayList<>();

    public Settings(String path) {
        this.path = Paths.get(path);
        try {
            Files.createFile(this.path);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /** this should be called only once just after initializing all your settings */
    public void init() {
        if (!this.isInit) {
            this.isInit = true;
            List<String> keyList = this.settingsList.stream().map(e -> e.key).collect(Collectors.toList());
            try {
                for (String line : Files.readAllLines(this.path)) {
                    int sep = line.indexOf('=');
                    String key = line.substring(0, sep);
                    if (Collections.frequency(keyList, key) > 1) { // duplicate key
                        throw new IllegalStateException("Duplicate setting keys");
                    }
                    for (AbstractSetting<?> s : this.settingsList) {
                        if (s.key.equals(key)) {
                            s.setFromString(line.substring(sep + 1));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException("init() called more than once");
        }
    }
}
