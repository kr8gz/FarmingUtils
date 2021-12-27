package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;

public class TextLabel extends ModGuiElement {
    String text;
    float scale;
    int color;

    public TextLabel(String text, int x, int y, int maxWidth) {
        this(text, x, y, 1f, maxWidth, Colors.WHITE);
    }

    public TextLabel(String text, int x, int y, int maxWidth, int color) {
        this(text, x, y, 1f, maxWidth, color);
    }

    public TextLabel(String text, int x, int y, float scale, int maxWidth) {
        this(text, x, y, scale, maxWidth, Colors.WHITE);
    }

    public TextLabel(String text, int x, int y, float scale, int maxWidth, int color) {
        super(x, y, maxWidth, 0);
        this.text = text;
        this.scale = scale;
        this.height = getHeight();
        this.color = color;
    }

    int getHeight() {
        return Helper.round(Minecraft.getMinecraft().fontRendererObj.splitStringWidth(text, Helper.round(width / scale)) * scale);
    }

    @Override
    public void draw() {
        height = getHeight();
        Helper.glSetScale(scale);
        Minecraft.getMinecraft().fontRendererObj.drawSplitString(text, Helper.round(xPosition / scale), Helper.round(yPosition / scale), Helper.round(width / scale), color);
        Helper.glResetScale();
    }
}
