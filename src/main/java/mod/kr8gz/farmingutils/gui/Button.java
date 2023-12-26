package mod.kr8gz.farmingutils.gui;

public abstract class Button extends ModGuiElement implements ModGuiElement.Toggleable {
    boolean mouseOver;

    public Button(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
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
        if (isEnabled()) action();
    }
}
