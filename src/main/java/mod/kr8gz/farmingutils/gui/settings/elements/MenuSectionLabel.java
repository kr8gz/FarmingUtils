package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.gui.settings.GuiModConfig;
import mod.kr8gz.farmingutils.util.Colors;

public class MenuSectionLabel extends TextLabel {
    private final int sectionScrollAmount;

    public MenuSectionLabel(String text, int x, int y, float scale, int maxWidth, int sectionScrollAmount) {
        super(text, x, y, scale, maxWidth);
        this.sectionScrollAmount = -sectionScrollAmount;
    }

    @Override
    public void mouseHovered() {
        setColor(Colors.LIGHTBLUE);
    }

    @Override
    public void mouseStopHovered() {
        setColor(Colors.WHITE);
    }

    @Override
    public void mousePressed() {
        setColor(Colors.LIGHTBLUE);
        GuiModConfig.updateAmountScrolled(sectionScrollAmount);
    }

    @Override
    public void mouseReleased() {
        setColor(Colors.WHITE);
    }
}
