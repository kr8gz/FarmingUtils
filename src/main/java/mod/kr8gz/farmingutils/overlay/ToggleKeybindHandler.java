package mod.kr8gz.farmingutils.overlay;

import mod.kr8gz.farmingutils.FarmingUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.BooleanSetting;

public class ToggleKeybindHandler {
    private static final Settings toggleStates = new Settings(FarmingUtils.MODID + "/toggles.txt");

    private final KeyBinding keyBinding;
    private final BooleanSetting setting;

    private ToggleKeybindHandler(String name, int keyCode) {
        this.keyBinding = new KeyBinding(name, keyCode, FarmingUtils.NAME);
        this.setting = new BooleanSetting(toggleStates, name, null, true);

        ClientRegistry.registerKeyBinding(keyBinding);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void tickEvent(InputEvent.KeyInputEvent event) {
        if (keyBinding.isPressed()) setting.set(!setting.get());
    }

    public boolean get() {
        return setting.get();
    }

    public static final ToggleKeybindHandler showOverlay = new ToggleKeybindHandler("Toggle Overlay", Keyboard.KEY_O);
    public static final ToggleKeybindHandler showBPS = new ToggleKeybindHandler("Toggle BPS", Keyboard.KEY_B);
    public static final ToggleKeybindHandler showAngleHelper = new ToggleKeybindHandler("Toggle Angle Helper", Keyboard.KEY_H);
    public static final ToggleKeybindHandler showBreakingHelper = new ToggleKeybindHandler("Toggle Breaking Helper", Keyboard.KEY_U);

    static {
        toggleStates.load();
    }
}
