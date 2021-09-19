package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.config.ConfigManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeybindManager {
    public static boolean showOverlay = true;
    public static boolean showBPS = true;
    public static boolean showAngleHelper = true;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void keyListener(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();

            if (ConfigManager.showOverlay.get()) {
                if (key == ConfigManager.overlayToggleKey.get()) {
                    showOverlay = !showOverlay;
                }
                if (key == ConfigManager.bpsToggleKey.get() && ConfigManager.showBPS.get()) {
                    showBPS = !showBPS;
                }
                if (key == ConfigManager.angleHelperToggleKey.get() && ConfigManager.showAngleHelper.get()) {
                    showAngleHelper = !showAngleHelper;
                }
            }
        }
    }
}
