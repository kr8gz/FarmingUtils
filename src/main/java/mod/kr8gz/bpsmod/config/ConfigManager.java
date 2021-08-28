package mod.kr8gz.bpsmod.config;

import mod.kr8gz.bpsmod.BPSMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigManager {
    public static Configuration config;

    public static int roudingPrecision = 2;
    public static float overlayScale = 1.3f;
    public static float padding = 3f;
    public static int[] bpsTimes = {1, 10, 60};
    public static boolean disableWarning = false;
    public static boolean disableOverlay = false;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(BPSMod.MODID)) {
            sync();
        }
    }

    public static void sync() {
        roudingPrecision = config.get(
                "general", "Rounding Precision", 2,
                "Precision for rounding floating-point numbers.",
                1, 5
        ).getInt(2);

        overlayScale = (float) config.get(
                "general", "Overlay Scale", 1.3d,
                "Overlay scale.",
                0.5d, 3d
        ).getDouble(1.3d);

        padding = (float) config.get(
                "general", "Overlay Spacing", 3d,
                "Spacing between lines for overlay.",
                0.1d, 10d
        ).getDouble(3d);

        bpsTimes = config.get(
                "general", "BPS Times", new int[] {1, 10, 60},
                "Overlay will show the average BPS for each time specified here.",
                1, 3600,
                false, 10
        ).getIntList();

        disableWarning = config.get(
                "general", "Disable Player Count Warning", false,
                "Disables warning in overlay if player count is more than 1."
        ).getBoolean(false);

        disableOverlay = config.get(
                "general", "Disable Overlay", false,
                "Disables overlay."
        ).getBoolean(false);

        config.save();
    }

}
