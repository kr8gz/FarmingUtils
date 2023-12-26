package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.gui.FontRenderer;

public class FixedTextLabel extends TextLabel {
    static final FontRenderer fr = getFontRenderer();

    public FixedTextLabel(String text, int xCenter, int yCenter, float scale) {
        super(text, Helper.round(xCenter - fr.getStringWidth(text) / 2f * scale), Helper.round(yCenter - fr.FONT_HEIGHT / 2f * scale), scale, Integer.MAX_VALUE);
    }
}
