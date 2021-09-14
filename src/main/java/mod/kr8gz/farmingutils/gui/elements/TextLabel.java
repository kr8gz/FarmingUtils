package mod.kr8gz.farmingutils.gui.elements;

import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;

public class TextLabel extends ModGuiElement {
    String text;
    float scale;

    public TextLabel(String text, int x, int y) {
        this(text, x, y, 1f, Integer.MAX_VALUE);
    }

    public TextLabel(String text, int x, int y, float scale) {
        this(text, x, y, scale, Integer.MAX_VALUE);
    }

    public TextLabel(String text, int x, int y, int maxWidth) {
        this(text, x, y, 1f, maxWidth);
    }

    public TextLabel(String text, int x, int y, float scale, int maxWidth) {
        super(x, y, maxWidth, 0);
        this.text = text;
        this.scale = scale;
        this.height = getHeight();
    }

    public int getHeight() {
        return (int) (Minecraft.getMinecraft().fontRendererObj.splitStringWidth(text, width) * scale);
    }

    @Override
    public void draw() {
        Helper.glSetScale(scale);
        Minecraft.getMinecraft().fontRendererObj.drawSplitString(text, (int) (xPosition / scale), yPosition, (int) (width / scale), 0xffffff);
        Helper.glResetScale();
    }
}
