package mod.kr8gz.farmingutils;

import mod.kr8gz.farmingutils.gui.ModConfigCommand;
import mod.kr8gz.farmingutils.gui.OverlayHandler;
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
    public static final String VERSION = "1.0.0";

    public FarmingUtils() {
        MinecraftForge.EVENT_BUS.register(new OverlayHandler());
        MinecraftForge.EVENT_BUS.register(new ModConfigCommand.EventHandler());
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ModConfigCommand());
    }

}
