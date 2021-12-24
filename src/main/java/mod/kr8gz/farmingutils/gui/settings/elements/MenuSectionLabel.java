package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class MenuSectionLabel extends TextLabel {
    private final int sectionScrollAmount;

    public MenuSectionLabel(ModGuiScreen screen, String text, int x, int y, float scale, int maxWidth, int sectionScrollAmount) {
        super(screen, text, x, y, scale, maxWidth);
        this.sectionScrollAmount = -sectionScrollAmount;
    }

    @Override
    public void mouseHovered() {
        color = Colors.LIGHTBLUE;
    }

    @Override
    public void mouseStopHovered() {
        color = Colors.WHITE;
    }

    @Override
    public void mousePressed() {
        color = Colors.LIGHTBLUE;
        screen.updateAmountScrolled(sectionScrollAmount);
    }

    @Override
    public void mouseReleased() {
        color = Colors.WHITE;
    }

    @Override
    int getHeight() {
        return (int) (scale * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
    }

    @Override
    public void draw() {
        Helper.glSetScale(scale);
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

        int textWidth = (int) (fr.getStringWidth(text) * scale);
        int ellipsisWidth = (int) (fr.getStringWidth("...") * scale);
        String text1 = text;
        if (textWidth > width && textWidth > ellipsisWidth) {
            text1 = fr.trimStringToWidth(text1, (int) ((width - ellipsisWidth) / scale)).trim() + "...";
        }
        fr.drawStringWithShadow(text1, (int) ((xPosition + width) / scale) - fr.getStringWidth(text1), (int) (yPosition / scale), color);

        Helper.glResetScale();
    }
}
