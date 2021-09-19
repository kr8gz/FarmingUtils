package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.FarmingUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class KeybindManager {
    // step 1
    private final KeyBinding    toggleOverlay;
    public static boolean       showOverlay = true;
    private final KeyBinding    toggleBPS;
    public static boolean       showBPS = true;
    private final KeyBinding    toggleAngleHelper;
    public static boolean       showAngleHelper = true;

    public KeybindManager() {
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
            if (toggleOverlay.isPressed())      showOverlay = !showOverlay;
            if (toggleBPS.isPressed())          showBPS = !showBPS;
            if (toggleAngleHelper.isPressed())  showAngleHelper = !showAngleHelper;
        }
    }
}
