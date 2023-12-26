package mod.kr8gz.farmingutils.screens;

import mod.kr8gz.farmingutils.gui.ModGuiElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ModGuiScreen extends GuiScreen {
    final ModGuiScreen parentScreen;
    int amountScrolled = 0;
    int maxScrollHeight;

    /** when modifying the elementList with existing elements use nextElements instead */
    public List<ModGuiElement> elementList = new ArrayList<>();
    public List<ModGuiElement> nextElements = new ArrayList<>();
    ModGuiElement selectedElement;

    public ModGuiScreen(ModGuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        amountScrolled = 0;
        maxScrollHeight = 0;
        elementList.clear();
        nextElements.clear();
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
            if (e.scrollable) {
                e.yPosition += amountScrolled - before;
            }
        }
    }

    @Override
    public void handleMouseInput() {
        updateAmountScrolled();

        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
            selectedElement = null;
            List<ModGuiElement> rList = new ArrayList<>(elementList);
            Collections.reverse(rList);
            for (ModGuiElement e : rList) {
                if (mouseX > e.xPosition && mouseX < e.xPosition + e.width && mouseY > e.yPosition && mouseY < e.yPosition + e.height && selectedElement == null) {
                    e.mousePressed(mouseX, mouseY);
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

        // TODO inspect wtf this is for
        if (nextElements.size() > 0) {
            elementList = new ArrayList<>(nextElements);
        } else {
            nextElements = elementList;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        boolean shouldCloseScreen = elementList.stream().allMatch(e -> e.keyTyped(typedChar, keyCode));
        if (shouldCloseScreen && keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        elementList.forEach(ModGuiElement::draw);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
