package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.FarmingUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.types.BooleanSetting;

public class KeybindManager {
    private static final Settings toggleStates = new Settings(FarmingUtils.MODID + "/toggles.txt");

    // step 1
    private static final KeyBinding toggleOverlay;
    public static final BooleanSetting overlayToggled = new BooleanSetting(toggleStates,
            "overlayToggled", null, false
    );

    private static final KeyBinding toggleBPS;
    public static final BooleanSetting BPSToggled = new BooleanSetting(toggleStates,
            "BPSToggled", null, false
    );

    private static final KeyBinding toggleAngleHelper;
    public static final BooleanSetting angleHelperToggled = new BooleanSetting(toggleStates,
            "angleHelperToggled", null, false
    );

    private static final KeyBinding toggleBreakingHelper;
    public static final BooleanSetting breakingHelperToggled = new BooleanSetting(toggleStates,
            "breakingHelperToggled", null, false
    );

    static {
        toggleStates.init();

        // step 2
        toggleOverlay = new KeyBinding("Toggle Overlay", Keyboard.KEY_O, FarmingUtils.NAME);
        toggleBPS = new KeyBinding("Toggle BPS", Keyboard.KEY_B, FarmingUtils.NAME);
        toggleAngleHelper = new KeyBinding("Toggle Angle Helper", Keyboard.KEY_H, FarmingUtils.NAME);
        toggleBreakingHelper = new KeyBinding("Toggle Breaking Helper", Keyboard.KEY_U, FarmingUtils.NAME);

        // step 3
        ClientRegistry.registerKeyBinding(toggleOverlay);
        ClientRegistry.registerKeyBinding(toggleBPS);
        ClientRegistry.registerKeyBinding(toggleAngleHelper);
        ClientRegistry.registerKeyBinding(toggleBreakingHelper);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void tickEvent(InputEvent.KeyInputEvent event) {
        // step 4
        if (toggleOverlay.isPressed()) overlayToggled.set(!overlayToggled.get());
        if (toggleBPS.isPressed()) BPSToggled.set(!BPSToggled.get());
        if (toggleAngleHelper.isPressed()) angleHelperToggled.set(!angleHelperToggled.get());
        if (toggleBreakingHelper.isPressed()) breakingHelperToggled.set(!breakingHelperToggled.get());
    }
}
