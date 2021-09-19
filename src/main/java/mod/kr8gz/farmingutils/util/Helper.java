package mod.kr8gz.farmingutils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Helper {
    public static void sendDebugMessage(Object value) {
        sendDebugMessage(String.valueOf(value));
    }

    public static void sendDebugMessage(String value) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("[DEBUG] " + value));
    }

    public static void playClientSound(String name) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(name)));
    }

    public static void glSetScale(float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
    }

    public static void glResetScale() {
        GlStateManager.popMatrix();
    }

    public static float round(float value, int precision) {
        return (float) (Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision));
    }

    public static double round(double value, int precision) {
        return Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
    }

    public static String formatInt(int i) {
        return NumberFormat.getIntegerInstance(Locale.US).format(i);
    }

    public static String formatTime(int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds % 60 == 0) {
            return seconds / 60 + "m";
        } else {
            return String.format("%sm%ss", seconds / 60, seconds % 60);
        }
    }

    public static boolean isDivisible(BigDecimal a, BigDecimal b) {
        return a.remainder(b).compareTo(BigDecimal.ZERO) == 0;
    }
}
