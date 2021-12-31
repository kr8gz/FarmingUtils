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

    public static boolean isValidAngles(double dYaw, double dPitch) {
        int width   = OverlaySection.getScreenWidth();
        int height  = OverlaySection.getScreenHeight();

        double yaw = Helper.round(MathHelper.wrapAngleTo180_double(mc.thePlayer.rotationYaw), 1) + dYaw;
        double pitch = Helper.round(MathHelper.wrapAngleTo180_double(mc.thePlayer.rotationPitch), 1) + dPitch;

        double yawDiff      = ConfigManager.angleHelperYaw.get().doubleValue() - yaw;
        double pitchDiff    = ConfigManager.angleHelperPitch.get().doubleValue() - MathHelper.clamp_double(pitch, -90.0d, 90.0d);
        if (yawDiff > 180) {
            yawDiff -= 180;
        } else if (yawDiff < -180) {
            yawDiff += 180;
        }

        boolean validYaw = true;
        boolean validPitch = true;

        if (ConfigManager.enableYaw.get() && (ConfigManager.oppositeYaw.get() || Math.abs(yawDiff) < 90)) {
            double yawTolerance = ConfigManager.yawTolerance.get().doubleValue();
            int left    = Helper.round(width / 2d + width * Math.tan(Math.toRadians(yawDiff + yawTolerance)));
            int right   = Helper.round(width / 2d + width * Math.tan(Math.toRadians(yawDiff - yawTolerance)));
            if (left >= 0 && right <= width) {
                validYaw = Math.abs(yawDiff) <= yawTolerance || 180 - Math.abs(yawDiff) <= yawTolerance;
            } else {
                validYaw = false;
            }
        }

        if (ConfigManager.enablePitch.get() && Math.abs(pitchDiff) < 90) {
            double pitchTolerance = ConfigManager.pitchTolerance.get().doubleValue();
            int top     = Helper.round(height / 2d + height * Math.tan(Math.toRadians(pitchDiff + pitchTolerance)));
            int bottom  = Helper.round(height / 2d + height * Math.tan(Math.toRadians(pitchDiff - pitchTolerance)));
            if (top >= 0 && bottom <= height) {
                validPitch = Math.abs(pitchDiff) <= pitchTolerance;
            } else {
                validPitch = false;
            }
        }

        return validYaw && validPitch;
    }

    public static void draw() {
        if (ConfigManager.enableOverlay.get() && !KeybindManager.overlayToggled.get() && ConfigManager.enableAngleHelper.get() && !KeybindManager.angleHelperToggled.get()) {
            int red     = Colors.rgba(Colors.RED, ConfigManager.angleHelperOpacity.get().doubleValue());
            int green   = Colors.rgba(Colors.GREEN, ConfigManager.angleHelperOpacity.get().doubleValue());

            int width   = OverlaySection.getScreenWidth();
            int height  = OverlaySection.getScreenHeight();

            double yawDiff      = ConfigManager.angleHelperYaw.get().doubleValue() - Helper.round(MathHelper.wrapAngleTo180_double(mc.thePlayer.rotationYaw), 1);
            double pitchDiff    = ConfigManager.angleHelperPitch.get().doubleValue() - Helper.round(MathHelper.wrapAngleTo180_double(mc.thePlayer.rotationPitch), 1);
            if (yawDiff > 180) {
                yawDiff -= 180;
            } else if (yawDiff < -180) {
                yawDiff += 180;
            }

            if (ConfigManager.enableYaw.get() && (ConfigManager.oppositeYaw.get() || Math.abs(yawDiff) < 90)) {
                double yawTolerance = ConfigManager.yawTolerance.get().doubleValue();
                int left    = Helper.round(width / 2d + width * Math.tan(Math.toRadians(yawDiff + yawTolerance)));
                int right   = Helper.round(width / 2d + width * Math.tan(Math.toRadians(yawDiff - yawTolerance)));
                if (left >= 0 && right <= width) {
                    drawRect(left, 0, left == right ? left + 1 : right, height, Math.abs(yawDiff) <= yawTolerance || 180 - Math.abs(yawDiff) <= yawTolerance ? green : red);
                }
            }

            if (ConfigManager.enablePitch.get() && Math.abs(pitchDiff) < 90) {
                double pitchTolerance = ConfigManager.pitchTolerance.get().doubleValue();
                int top     = Helper.round(height / 2d + height * Math.tan(Math.toRadians(pitchDiff + pitchTolerance)));
                int bottom  = Helper.round(height / 2d + height * Math.tan(Math.toRadians(pitchDiff - pitchTolerance)));
                if (top >= 0 && bottom <= height) {
                    drawRect(0, top, width, top == bottom ? top + 1 : bottom, Math.abs(pitchDiff) <= pitchTolerance ? green : red);
                }
            }
        }
    }
}
