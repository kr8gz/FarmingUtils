package mod.kr8gz.farmingutils.util;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings("unused")
public class Logger {
    private static final Path logPath = Paths.get(FarmingUtils.MODID + "/log.txt");

    static {
        Helper.createFile(logPath);
    }

    public static void log(String line) {
        if (ConfigManager.logInfo.get()) {
            try {
                List<String> lines = Files.readAllLines(logPath);
                lines.add(line);
                Files.write(logPath, lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}