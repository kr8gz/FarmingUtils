package mod.kr8gz.farmingutils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Helper {

    public static void sendDebugMessage(int value) {
        sendDebugMessage(String.valueOf(value));
    }

    public static void sendDebugMessage(String value) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("[DEBUG] " + value));
    }

    public static void playClientSound(String name) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(name)));
    }

    public static void drawOverlayString(String string, float xy) {
        drawOverlayString(string, xy, xy, 1f, 0xffffff, true);
    }

    public static void drawOverlayString(String string, float xy, boolean advancedNegativeMode) {
        drawOverlayString(string, xy, xy, 1f, 0xffffff, advancedNegativeMode);
    }

    public static void drawOverlayString(String string, float x, float y) {
        drawOverlayString(string, x, y, 1f, 0xffffff, true);
    }

    public static void drawOverlayString(String string, float x, float y, boolean advancedNegativeMode) {
        drawOverlayString(string, x, y, 1f, 0xffffff, advancedNegativeMode);
    }

    public static void drawOverlayString(String string, float x, float y, float scale) {
        drawOverlayString(string, x, y, scale, 0xffffff, true);
    }

    public static void drawOverlayString(String string, float x, float y, float scale, boolean advancedNegativeMode) {
        drawOverlayString(string, x, y, scale, 0xffffff, advancedNegativeMode);
    }

    public static void drawOverlayString(String string, float x, float y, float scale, int color) {
        drawOverlayString(string, x, y, scale, color, true);
    }

    public static void drawOverlayString(String string, float x, float y, float scale, int color, boolean advancedNegativeMode) {
        glSetScale(scale);
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        x = advancedNegativeMode && x < 0 ? scaledRes.getScaledWidth() - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) - x) * scale : x;
        y = advancedNegativeMode && y < 0 ? scaledRes.getScaledHeight() - y * scale : y; // y * scale
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(string, x / scale, y / scale, color);
        glResetScale();
    }

    public static void glSetScale(float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
    }

    public static void glResetScale() {
        GL11.glPopMatrix();
    }

    public static float round(float value, int precision) {
        return (float) (Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision));
    }

    public static String formatInt(int i) {
        return NumberFormat.getIntegerInstance(Locale.US).format(i);
    }

    public static boolean isDivisible(BigDecimal a, BigDecimal b) {
        return a.remainder(b).compareTo(BigDecimal.ZERO) == 0;
    }
}
