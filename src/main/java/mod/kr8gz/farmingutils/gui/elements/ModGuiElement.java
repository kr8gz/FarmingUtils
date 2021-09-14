package mod.kr8gz.farmingutils.gui.elements;

public abstract class ModGuiElement {
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;

    public ModGuiElement(int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }

    public abstract void draw();

    /** hint: if for example {@code mousePressed()} is fired then {@code mousePressedGlobal()} won't be fired */
    public void mousePressed() {}
    public void mousePressedGlobal() {}
    public void mouseReleased() {}
    public void mouseReleasedGlobal() {}
    public void mouseHovered() {}
    public void mouseMovedGlobal(int mouseX, int mouseY) {}
    public void keyTyped(char character, int key) {}
}
