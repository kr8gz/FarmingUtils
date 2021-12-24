package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class FixedTextLabel extends TextLabel {
    static final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    public FixedTextLabel(ModGuiScreen screen, String text, int xCenter, int yCenter, float scale) {
        super(screen, text, (int) (xCenter - fr.getStringWidth(text) / 2f * scale), (int) (yCenter - fr.FONT_HEIGHT / 2f * scale), scale, Integer.MAX_VALUE);
    }
}
