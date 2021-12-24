package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;
import mod.kr8gz.farmingutils.util.Colors;

public class Line extends ModGuiElement {
    public Line(ModGuiScreen screen, int left, int right, int y) {
        super(screen, left, y, right - left, 1);
    }

    @Override
    public void draw() {
        drawRect(xPosition, yPosition, xPosition + width + 1, yPosition + 1, Colors.rgba(Colors.WHITE));
    }
}
