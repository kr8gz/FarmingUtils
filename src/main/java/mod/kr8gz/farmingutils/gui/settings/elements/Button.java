package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class Button extends ModGuiElement {
    String text;
    float textScale;
    int color;

    boolean mouseOver;

    public Button(int xPosition, int yPosition, int width, int height, String text, float textScale, int color) {
        super(xPosition, yPosition, width, height);
        this.text = text;
        this.textScale = textScale;
        this.color = color;
    }

    protected abstract void action();

    @Override
    public void mouseHovered() {
        mouseOver = true;
    }

    @Override
    public void mouseStopHovered() {
        mouseOver = false;
    }

    @Override
    public void mousePressed() {
        mouseOver = true;
    }

    @Override
    public void mouseReleased() {
        mouseOver = false;
        action();
    }

    @Override
    public void draw() {
        if (mouseOver) {
            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Colors.rgba(color, 0.3d));
        }
        Helper.drawEmptyRect(xPosition, yPosition, xPosition + width, yPosition + height, Colors.rgba(color));
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        Helper.glSetScale(textScale);
        fr.drawStringWithShadow(text, (xPosition + width / 2f) / textScale - fr.getStringWidth(text) / 2f, (yPosition + height / 2f) / textScale - 4, mouseOver ? Colors.rgba(Colors.WHITE) : Colors.rgba(color));
        Helper.glResetScale();
    }
}
