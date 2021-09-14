package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.elements.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.types.BooleanSetting;
import test.kr8gz.settings.types.DecimalSetting;
import test.kr8gz.settings.types.IntegerSetting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GuiModConfig extends GuiScreen {
    int left;
    int right;
    int maxDescWidth;

    int amountScrolled = 0;
    int offset = 0;
    int maxScrollHeight;
    public static List<ModGuiElement> elementList = new ArrayList<>();
    ModGuiElement selectedElement;

    boolean isLast;

    @Override
    public void initGui() {
        this.left = width / 8;
        this.right = width - left;
        this.maxDescWidth = width * 3/4 - 16;

        elementList.clear();
        amountScrolled = 0;
        offset = 0;
        isLast = false;

        Keyboard.enableRepeatEvents(true);

        String string = FarmingUtils.NAME + " Settings";
        elementList.add(new TextLabel(string, width / 2 - fontRendererObj.getStringWidth(string), height / 8 + offset, 2f));
        if (height / 8 < 20) {
            offset = 20;
        }

        addCheckBox(ConfigManager.showOverlay);
        addCheckBox(ConfigManager.showWarnings, () -> ConfigManager.showOverlay.get());

        addIntegerSlider(ConfigManager.roudingPrecision, () -> ConfigManager.showOverlay.get());
        addFloatSlider(ConfigManager.overlayScale, () -> ConfigManager.showOverlay.get());
        addFloatSlider(ConfigManager.padding, () -> ConfigManager.showOverlay.get());

        addCheckBox(ConfigManager.showBPS, () -> ConfigManager.showOverlay.get());

        addCheckBox(ConfigManager.showJacobsHelper, () -> ConfigManager.showOverlay.get());
        addCheckBox(ConfigManager.jacobsHelperAlert, () -> ConfigManager.showOverlay.get() && ConfigManager.showJacobsHelper.get());
        addCheckBox(ConfigManager.showCropsUntilAlert, () -> ConfigManager.showOverlay.get() && ConfigManager.showJacobsHelper.get() && ConfigManager.jacobsHelperAlert.get());
        addCheckBox(ConfigManager.showTimeUntilAlert, () -> ConfigManager.showOverlay.get() && ConfigManager.showJacobsHelper.get() && ConfigManager.jacobsHelperAlert.get());
        addIntegerSlider(ConfigManager.alertExtraPercent, () -> ConfigManager.showOverlay.get() && ConfigManager.showJacobsHelper.get() && ConfigManager.jacobsHelperAlert.get());

        isLast = true;
        addCheckBox(ConfigManager.logInfo);

        maxScrollHeight = height - height / 4 - offset + 10;
    }

    /** helper methods for adding GUI elements */
    private void addCheckBox(BooleanSetting setting) {
        addCheckBox(setting, () -> true);
    }

    private void addCheckBox(BooleanSetting setting, Supplier<Boolean> enabledCondition) {
        int prev = offset;
        addOtherStuff(setting, 32);
        elementList.add(new CheckBox(
                setting,
                right - 32, height / 4 + prev + (offset - prev) / 2 - 26,
                enabledCondition
        ));
    }

    private void addIntegerSlider(IntegerSetting setting) {
        addIntegerSlider(setting, () -> true);
    }

    private void addIntegerSlider(IntegerSetting setting, Supplier<Boolean> enabledCondition) {
        int prev = offset;
        addOtherStuff(setting, 140);
        elementList.add(new IntegerSlider(
                setting,
                right - 100, height / 4 + prev + (offset - prev) / 2 - 26,
                enabledCondition
        ));
    }

    private void addFloatSlider(DecimalSetting setting) {
        addFloatSlider(setting, () -> true);
    }

    private void addFloatSlider(DecimalSetting setting, Supplier<Boolean> enabledCondition) {
        int prev = offset;
        addOtherStuff(setting, 140);
        elementList.add(new DecimalSlider(
                setting,
                right - 100, height / 4 + prev + (offset - prev) / 2 - 26,
                enabledCondition
        ));
    }

    private void addOtherStuff(Settings.AbstractSetting<?> setting, int spaceNeeded) {
        int top = height / 4 + offset;
        elementList.add(new TextLabel(setting.key, left, top, 1.3f, maxDescWidth - spaceNeeded));
        TextLabel desc;
        elementList.add(desc = new TextLabel(setting.description, left, top + 15, maxDescWidth - spaceNeeded));

        if (!isLast) {
            elementList.add(new Line(left, right, desc.yPosition + desc.getHeight() + 9));
        }

        offset += 36 + desc.getHeight();
    }

    @Override
    public void handleMouseInput() {
        int before = amountScrolled;
        amountScrolled = MathHelper.clamp_int(amountScrolled + Mouse.getEventDWheel() / 5, Math.min(maxScrollHeight, 0), 0);
        for (ModGuiElement e : elementList) {
            e.yPosition += amountScrolled - before;
        }

        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        if (Mouse.getEventButtonState()) {
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
        else if (Mouse.getEventButton() != -1) {
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
                    e.mouseHovered();
                }
            }
        }
        for (ModGuiElement e : elementList) e.mouseMovedGlobal(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (ModGuiElement e : elementList) e.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
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
