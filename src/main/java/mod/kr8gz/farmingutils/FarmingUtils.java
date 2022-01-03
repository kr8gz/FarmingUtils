package mod.kr8gz.farmingutils;

import mod.kr8gz.farmingutils.gui.KeybindManager;
import mod.kr8gz.farmingutils.gui.overlay.BPSOverlay;
import mod.kr8gz.farmingutils.gui.overlay.OverlayHandler;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.gui.settings.CommandModConfig;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = FarmingUtils.MODID,
        name = FarmingUtils.NAME,
        version = FarmingUtils.VERSION,
        clientSideOnly = true,
        canBeDeactivated = true
)
public class FarmingUtils {
    public static final String MODID = "farmingutils";
    public static final String NAME = "FarmingUtils";
    public static final String VERSION = "v0.1.4-alpha";

    public FarmingUtils() {
        Helper.createDirectory(FarmingUtils.MODID + "/config");

        MinecraftForge.EVENT_BUS.register(new OverlayHandler());
        MinecraftForge.EVENT_BUS.register(new OverlaySection.EventHandler());
        MinecraftForge.EVENT_BUS.register(new BPSOverlay.EventHandler());
        MinecraftForge.EVENT_BUS.register(new KeybindManager());
        MinecraftForge.EVENT_BUS.register(new CommandModConfig());
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CommandModConfig());
    }

}
