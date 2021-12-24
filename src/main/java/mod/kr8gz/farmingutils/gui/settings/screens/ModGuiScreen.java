package mod.kr8gz.farmingutils.gui.settings.screens;

import mod.kr8gz.farmingutils.gui.settings.elements.ModGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public abstract class ModGuiScreen extends GuiScreen {
    ModGuiScreen parentScreen;
    int amountScrolled = 0;
    int maxScrollHeight;
    public final List<ModGuiElement> elementList = new ArrayList<>();
    ModGuiElement selectedElement;

    public ModGuiScreen(ModGuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        amountScrolled = 0;
        maxScrollHeight = 0;
        elementList.clear();
        selectedElement = null;
        Keyboard.enableRepeatEvents(true);
    }

    /** input handling methods */
    private void updateAmountScrolled() {
        updateAmountScrolled(amountScrolled + Mouse.getEventDWheel() / 5);
    }

    public void updateAmountScrolled(int newAmount) {
        int before = amountScrolled;
        amountScrolled = MathHelper.clamp_int(newAmount, Math.min(maxScrollHeight, 0), 0);
        for (ModGuiElement e : elementList) {
            if (e.scrollable)
                e.yPosition += amountScrolled - before;
        }
    }

    @Override
    public void handleMouseInput() {
        updateAmountScrolled();

        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
            selectedElement = null;
            for (ModGuiElement e : elementList) {
                if (mouseX > e.xPosition && mouseX < e.xPosition + e.width && mouseY > e.yPosition && mouseY < e.yPosition + e.height) {
                    e.mousePressed();
                    selectedElement = e;
                } else {
                    e.mousePressedGlobal();
                }
            }
        }
        else if (Mouse.getEventButton() == 0) {
            for (ModGuiElement e : elementList) {
                if (e == selectedElement) {
                    e.mouseReleased();
                } else {
                    e.mouseReleasedGlobal();
                }
            }
        }
        else {
            for (ModGuiElement e : elementList) {
                if (mouseX > e.xPosition && mouseX < e.xPosition + e.width && mouseY > e.yPosition && mouseY < e.yPosition + e.height) {
                    e.mouseHovering = true;
                    e.mouseHovered();
                } else if (e.mouseHovering) {
                    e.mouseHovering = false;
                    e.mouseStopHovered();
                }
            }
        }
        for (ModGuiElement e : elementList) e.mouseMovedGlobal(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        boolean close = true;
        for (ModGuiElement e : elementList) {
            if (e.keyTyped(typedChar, keyCode)) {
                close = false;
            }
        }
        if (close && keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        for (ModGuiElement e : elementList) e.draw();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
