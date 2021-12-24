package mod.kr8gz.farmingutils.gui.overlay;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.KeybindManager;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

public class AngleHelperOverlay extends Gui {
    final static Minecraft mc = Minecraft.getMinecraft();

    public static void draw() {
        if (ConfigManager.showOverlay.get() && !KeybindManager.overlayToggled.get() && ConfigManager.showAngleHelper.get() && !KeybindManager.angleHelperToggled.get()) {
            int red     = Colors.rgba(Colors.RED, ConfigManager.angleHelperOpacity.get().doubleValue());
            int green   = Colors.rgba(Colors.GREEN, ConfigManager.angleHelperOpacity.get().doubleValue());

            int width   = OverlaySection.getScreenWidth();
            int height  = OverlaySection.getScreenHeight();

            double yawDiff      = ConfigManager.angleHelperYaw.get().doubleValue() - Helper.round(MathHelper.wrapAngleTo180_double(mc.thePlayer.rotationYaw), 1);
            double pitchDiff    = ConfigManager.angleHelperPitch.get().doubleValue() - Helper.round(MathHelper.wrapAngleTo180_double(mc.thePlayer.rotationPitch), 1);

            // yaw logic
            if (ConfigManager.showYaw.get() && (ConfigManager.oppositeYaw.get() || Math.abs(yawDiff) < 90)) {
                double yawTolerance = ConfigManager.yawTolerance.get().doubleValue();
                int left    = (int) Math.round(width / 2d + width * Math.tan(Math.toRadians(yawDiff + yawTolerance)));
                int right   = (int) Math.round(width / 2d + width * Math.tan(Math.toRadians(yawDiff - yawTolerance)));
                if (left >= 0 && right <= width) {
                    drawRect(left, 0, left == right ? left + 1 : right, height, Math.abs(yawDiff) <= yawTolerance || 180 - Math.abs(yawDiff) <= yawTolerance ? green : red);
                }
            }

            // pitch logic (basically the same as yaw)
            if (ConfigManager.showPitch.get() && Math.abs(pitchDiff) < 90) {
                double pitchTolerance = ConfigManager.pitchTolerance.get().doubleValue();
                int top     = (int) Math.round(height / 2d + height * Math.tan(Math.toRadians(pitchDiff + pitchTolerance)));
                int bottom  = (int) Math.round(height / 2d + height * Math.tan(Math.toRadians(pitchDiff - pitchTolerance)));
                if (top >= 0 && bottom <= height) {
                    drawRect(0, top, width, top == bottom ? top + 1 : bottom, Math.abs(pitchDiff) <= pitchTolerance ? green : red);
                }
            }
        }
    }
}
