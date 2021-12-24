package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;

public class TextLabel extends ModGuiElement {
    String text;
    float scale;
    int color;

    public TextLabel(ModGuiScreen screen, String text, int x, int y, int maxWidth) {
        this(screen, text, x, y, 1f, maxWidth, Colors.WHITE);
    }

    public TextLabel(ModGuiScreen screen, String text, int x, int y, int maxWidth, int color) {
        this(screen, text, x, y, 1f, maxWidth, color);
    }

    public TextLabel(ModGuiScreen screen, String text, int x, int y, float scale, int maxWidth) {
        this(screen, text, x, y, scale, maxWidth, Colors.WHITE);
    }

    public TextLabel(ModGuiScreen screen, String text, int x, int y, float scale, int maxWidth, int color) {
        super(screen, x, y, maxWidth, 0);
        this.text = text;
        this.scale = scale;
        this.height = getHeight();
        this.color = color;
    }

    int getHeight() {
        return (int) (Minecraft.getMinecraft().fontRendererObj.splitStringWidth(text, (int) (width / scale)) * scale);
    }

    @Override
    public void draw() {
        height = getHeight();
        Helper.glSetScale(scale);
        Minecraft.getMinecraft().fontRendererObj.drawSplitString(text, (int) (xPosition / scale), (int) (yPosition / scale), (int) (width / scale), color);
        Helper.glResetScale();
    }
}
