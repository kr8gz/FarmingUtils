package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.screens.ModGuiScreen;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.gui.FontRenderer;

public class MenuSectionLabel extends TextLabel {
    private final ModGuiScreen screen;
    private final int sectionScrollAmount;

    public MenuSectionLabel(ModGuiScreen screen, String text, int x, int y, float scale, int maxWidth, int sectionScrollAmount) {
        super(text, x, y, scale, maxWidth);
        this.screen = screen;
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
    public void mousePressed(int mouseX, int mouseY) {
        color = Colors.LIGHTBLUE;
        screen.updateAmountScrolled(sectionScrollAmount);
    }

    @Override
    public void mouseReleased() {
        color = Colors.WHITE;
    }

    @Override
    int getHeight() {
        return Helper.round(scale * getFontRenderer().FONT_HEIGHT);
    }

    @Override
    public void draw() {
        Helper.renderWithScale(scale, () -> {
            FontRenderer fr = getFontRenderer();
            int textWidth = Helper.round(fr.getStringWidth(text) * scale);
            int ellipsisWidth = Helper.round(fr.getStringWidth("...") * scale);
            String text1 = text;
            if (textWidth > width && textWidth > ellipsisWidth) {
                text1 = fr.trimStringToWidth(text1, Helper.round((width - ellipsisWidth) / scale)).trim() + "...";
            }
            fr.drawStringWithShadow(text1, Helper.round((xPosition + width) / scale) - fr.getStringWidth(text1), Helper.round(yPosition / scale), color);
        });
    }
}
