package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.FarmingUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.types.BooleanSetting;

public class KeybindManager {
    private static final Settings toggleStates = new Settings(FarmingUtils.MODID + "/toggles.txt");

    // step 1
    private final KeyBinding toggleOverlay;
    public static BooleanSetting overlayToggled = new BooleanSetting(toggleStates,
            "overlayToggled", null, false
    );

    private final KeyBinding toggleBPS;
    public static BooleanSetting BPSToggled = new BooleanSetting(toggleStates,
            "BPSToggled", null, false
    );

    private final KeyBinding toggleAngleHelper;
    public static BooleanSetting angleHelperToggled = new BooleanSetting(toggleStates,
            "angleHelperToggled", null, false
    );

    public KeybindManager() {
        toggleStates.init();

        // step 2
        toggleOverlay      = new KeyBinding("Toggle Overlay", Keyboard.KEY_O, FarmingUtils.NAME);
        toggleBPS          = new KeyBinding("Toggle BPS", Keyboard.KEY_B, FarmingUtils.NAME);
        toggleAngleHelper  = new KeyBinding("Toggle Angle Helper", Keyboard.KEY_H, FarmingUtils.NAME);

        // step 3
        ClientRegistry.registerKeyBinding(toggleOverlay);
        ClientRegistry.registerKeyBinding(toggleBPS);
        ClientRegistry.registerKeyBinding(toggleAngleHelper);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            // step 4
            if (toggleOverlay.isPressed())      overlayToggled.set(!overlayToggled.get());
            if (toggleBPS.isPressed())          BPSToggled.set(!BPSToggled.get());
            if (toggleAngleHelper.isPressed())  angleHelperToggled.set(!angleHelperToggled.get());
        }
    }
}
