package mod.kr8gz.bpsmod;

import mod.kr8gz.bpsmod.config.ConfigManager;
import mod.kr8gz.bpsmod.gui.ModConfigCommand;
import mod.kr8gz.bpsmod.gui.OverlayHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = BPSMod.MODID,
        name = BPSMod.NAME,
        version = BPSMod.VERSION,
        clientSideOnly = true,
        canBeDeactivated = true
)
public class BPSMod {
    public static final String MODID = "bpsmod";
    public static final String NAME = "BPS Mod";
    public static final String VERSION = "1.0.0";

    public BPSMod() {
        MinecraftForge.EVENT_BUS.register(new OverlayHandler());
        MinecraftForge.EVENT_BUS.register(new ConfigManager());
        MinecraftForge.EVENT_BUS.register(new ModConfigCommand.EventHandler());
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        ConfigManager.config = new Configuration(event.getSuggestedConfigurationFile());
        ConfigManager.config.load();
        ConfigManager.sync();

        ClientCommandHandler.instance.registerCommand(new ModConfigCommand());
    }

}
