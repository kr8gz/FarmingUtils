package mod.kr8gz.farmingutils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

// TODO this class should not exist in this form
public class Helper {
    public static void sendDebugMessage(String value) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("[DEBUG] " + value));
    }

    public static void playClientSound(String name) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation(name)));
    }

    public static void renderWithScale(float scale, Runnable runnable) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);

        runnable.run();

        GlStateManager.popMatrix();
    }

    public static void renderWithColor(int color, Runnable runnable) {
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        GlStateManager.color(r, g, b, a);

        runnable.run();

        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    public static int round(float value) {
        return Math.round(value);
    }

    public static int round(double value) {
        return (int) Math.round(value);
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

    public static void drawEmptyRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, top + 1, color);
        Gui.drawRect(left, top, left + 1, bottom, color);
        Gui.drawRect(left, bottom - 1, right, bottom, color);
        Gui.drawRect(right - 1, top, right, bottom, color);
    }

    public static void createDirectory(String name) {
        try {
            Files.createDirectories(Paths.get(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(Path path) {
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
