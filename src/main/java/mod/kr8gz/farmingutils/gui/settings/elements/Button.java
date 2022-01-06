package mod.kr8gz.farmingutils.gui.settings.elements;

import java.util.function.Supplier;

public abstract class Button extends ModToggleableGuiElement {
    boolean mouseOver;

    public Button(int xPosition, int yPosition, int width, int height, Supplier<Boolean> enabledCondition) {
        super(xPosition, yPosition, width, height, enabledCondition);
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
    public void mousePressed(int mouseX, int mouseY) {
        mouseOver = true;
    }

    @Override
    public void mouseReleased() {
        mouseOver = false;
        if (enabled) action();
    }
}
