package mod.kr8gz.farmingutils.data;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Logger {
    private final Path logPath = Paths.get("logs/." + FarmingUtils.MODID + ".log");

    private void logInfo() {
        try {
            try {
                Files.createFile(logPath);
            } catch (FileAlreadyExistsException ignored) {}

            List<String> lines = Files.readAllLines(logPath);

            lines.add("--- client tick " + OverlaySection.getCurrentTick() + " ---");
            lines.add("nothing to log for now");

            Files.write(logPath, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (ConfigManager.logInfo.get()) {
                if (OverlaySection.getCurrentTick() == 1) {
                    try {
                        try {
                            Files.createFile(logPath);
                        } catch (FileAlreadyExistsException ignored) {}
                        Files.write(logPath, new ArrayList<>());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (OverlaySection.getCurrentTick() % 200 == 0) {
                    logInfo();
                }
            }
        }
    }
}