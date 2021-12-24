package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.function.Supplier;

public abstract class Button extends ModToggleableGuiElement {
    String text;
    float textScale;
    int color;

    boolean mouseOver;

    public Button(ModGuiScreen screen, int xPosition, int yPosition, int width, int height, String text, float textScale, int color) {
        this(screen, xPosition, yPosition, width, height, text, textScale, color, () -> true);
    }

    public Button(ModGuiScreen screen, int xPosition, int yPosition, int width, int height, String text, float textScale, int color, Supplier<Boolean> enabledCondition) {
        super(screen, xPosition, yPosition, width, height, enabledCondition);
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
        if (enabled) action();
    }

    @Override
    public void draw() {
        int color = this.color;
        enabled = enabledCondition.get();
        if (enabled) {
            if (mouseOver) {
                drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Colors.rgba(this.color, 0.3d));
            }
        } else {
            color = Colors.GRAY;
        }
        Helper.drawEmptyRect(xPosition, yPosition, xPosition + width, yPosition + height, Colors.rgba(color));
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        Helper.glSetScale(textScale);
        fr.drawStringWithShadow(text, (xPosition + width / 2f) / textScale - fr.getStringWidth(text) / 2f, (yPosition + height / 2f) / textScale - 4, mouseOver && enabled ? Colors.rgba(Colors.WHITE) : Colors.rgba(color));
        Helper.glResetScale();
    }
}
