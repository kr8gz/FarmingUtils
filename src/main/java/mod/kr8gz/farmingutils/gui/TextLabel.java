package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;

import static mod.kr8gz.farmingutils.util.Helper.round;

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
        return round(getFontRenderer().splitStringWidth(text, round(width / scale)) * scale);
    }

    @Override
    public void draw() {
        height = getHeight();
        Helper.renderWithScale(scale, () -> getFontRenderer().drawSplitString(text, round(xPosition / scale), round(yPosition / scale), round(width / scale), color));
    }
}
