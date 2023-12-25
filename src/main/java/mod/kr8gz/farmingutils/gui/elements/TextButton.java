package mod.kr8gz.farmingutils.gui.elements;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.gui.FontRenderer;

public abstract class TextButton extends Button {
    String text;
    float textScale;
    int color;

    public TextButton(int xPosition, int yPosition, int width, int height, String text, float textScale, int color) {
        super(xPosition, yPosition, width, height);
        this.text = text;
        this.textScale = textScale;
        this.color = color;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void draw() {
        boolean enabled = isEnabled();
        int boxColor = enabled ? this.color : Colors.GRAY;
        if (enabled && mouseOver) {
            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, Colors.rgba(boxColor, 0.3d));
        }
        Helper.drawEmptyRect(xPosition, yPosition, xPosition + width, yPosition + height, Colors.rgba(boxColor));

        FontRenderer fr = getFontRenderer();
        int textColor = enabled && mouseOver ? Colors.rgba(Colors.WHITE) : Colors.rgba(boxColor);
        Helper.renderWithScale(textScale, () -> fr.drawStringWithShadow(text, (xPosition + width / 2f) / textScale - fr.getStringWidth(text) / 2f, (yPosition + height / 2f) / textScale - 4, textColor));
    }
}
