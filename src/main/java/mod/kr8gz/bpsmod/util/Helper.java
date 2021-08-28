package mod.kr8gz.bpsmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

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
        drawOverlayString(string, xy, xy, 1f, 0xffffff);
    }

    public static void drawOverlayString(String string, float x, float y) {
        drawOverlayString(string, x, y, 1f, 0xffffff);
    }

    public static void drawOverlayString(String string, float x, float y, float scale) {
        drawOverlayString(string, x, y, scale, 0xffffff);
    }

    public static void drawOverlayString(String string, float x, float y, float scale, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        x = x < 0 ? scaledRes.getScaledWidth() - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) - x) * scale : x * scale;
        y = y < 0 ? scaledRes.getScaledHeight() + y * scale : y * scale;
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(string, x / scale, y / scale, color);
        GL11.glPopMatrix();
    }

    public static float round(float value, int precision) {
        return (float) ((int) ((value + (value >= 0 ? 1 : -1) * 0.5 * Math.pow(10, -precision)) * Math.pow(10, precision)) / Math.pow(10, precision));
    }

}
