package mod.kr8gz.farmingutils.gui.elements;

import net.minecraft.client.gui.Gui;

public class Line extends ModGuiElement {
    public Line(int left, int right, int y) {
        super(left, y, right - left, 1);
    }

    @Override
    public void draw() {
        Gui.drawRect(xPosition, yPosition, xPosition + width + 1, yPosition + 1, 0xffffffff);
    }
}
