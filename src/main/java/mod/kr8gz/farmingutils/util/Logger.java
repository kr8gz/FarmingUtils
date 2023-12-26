package mod.kr8gz.farmingutils.util;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Logger {
    public static final Path LOG_PATH = Paths.get(FarmingUtils.MODID, "log.txt");

    static {
        try {
            new FileWriter(LOG_PATH.toString()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String line) {
        if (ConfigManager.logInfo.get()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_PATH.toString(), true))) {
                writer.write(line);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}