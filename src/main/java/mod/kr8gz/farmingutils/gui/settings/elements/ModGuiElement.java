package mod.kr8gz.farmingutils.gui.settings.elements;

import net.minecraft.client.gui.Gui;

public abstract class ModGuiElement extends Gui {
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
    public boolean mouseHovering;
    public boolean scrollable = true;

    public ModGuiElement(int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }

    public abstract void draw();

    /** if for example {@code mousePressed()} is fired then {@code mousePressedGlobal()} won't be fired */
    public void mousePressed(int mouseX, int mouseY) {}
    public void mousePressedGlobal() {}
    public void mouseReleased() {}
    public void mouseReleasedGlobal() {}
    public void mouseHovered() {}
    public void mouseStopHovered() {}
    public void mouseMovedGlobal(int mouseX, int mouseY) {}

    /** return true to prevent screen closing on esc */
    public boolean keyTyped(char character, int key) {
        return false;
    }

    /** for initializing and resetting {@code ModToggleableGuiElement}s used for settings */
    public void updateStateFromBoundSetting() {}
}
