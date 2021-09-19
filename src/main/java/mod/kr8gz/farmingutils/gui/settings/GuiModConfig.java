package mod.kr8gz.farmingutils.gui.settings;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.settings.elements.*;
import mod.kr8gz.farmingutils.util.Colors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.types.BooleanSetting;
import test.kr8gz.settings.types.DecimalSetting;
import test.kr8gz.settings.types.IntegerSetting;
import test.kr8gz.settings.types.KeybindSetting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GuiModConfig extends GuiScreen {
    static int left;
    static int right;
    static int maxDescWidth;

    static int amountScrolled = 0;
    static int offset = 0;
    static int maxScrollHeight;

    public static final List<ModGuiElement> elementList = new ArrayList<>();
    static ModGuiElement selectedElement;

    static int sections;

    @Override
    public void initGui() {
        left = width / 4;
        right = width * 7/8;
        maxDescWidth = right - left - 16;

        amountScrolled = 0;
        offset = 0;

        elementList.clear();
        selectedElement = null;

        sections = 0;

        Keyboard.enableRepeatEvents(true);

        addTitleLabel(FarmingUtils.NAME + " Settings", height / 8 + offset);
        if (height / 8 < 20) offset = 20;

        addSection("Overlay");
        addCheckBox(ConfigManager.showOverlay);
        addKeybindBox(ConfigManager.overlayToggleKey, () -> ConfigManager.showOverlay.get());
        addCheckBox(ConfigManager.showWarnings, () -> ConfigManager.showOverlay.get());
        addIntegerSlider(ConfigManager.roudingPrecision, () -> ConfigManager.showOverlay.get());
        addDecimalSlider(ConfigManager.overlayScale, () -> ConfigManager.showOverlay.get());
        addDecimalSlider(ConfigManager.overlayBackgroundOpacity, () -> ConfigManager.showOverlay.get());

        addSection("BPS");
        addCheckBox(ConfigManager.showBPS, () -> ConfigManager.showOverlay.get());
        addKeybindBox(ConfigManager.bpsToggleKey, () -> ConfigManager.showOverlay.get() && ConfigManager.showBPS.get());
        // TODO someday ConfigManager.bpsTimes

        addSection("Jacob's Helper");
        addCheckBox(ConfigManager.showJacobsHelper, () -> ConfigManager.showOverlay.get());
        addCheckBox(ConfigManager.jacobsHelperAlert, () -> ConfigManager.showOverlay.get() && ConfigManager.showJacobsHelper.get());
        addCheckBox(ConfigManager.showCropsUntilAlert, () -> ConfigManager.showOverlay.get() && ConfigManager.showJacobsHelper.get() && ConfigManager.jacobsHelperAlert.get());
        addCheckBox(ConfigManager.showTimeUntilAlert, () -> ConfigManager.showOverlay.get() && ConfigManager.showJacobsHelper.get() && ConfigManager.jacobsHelperAlert.get());
        addIntegerSlider(ConfigManager.alertExtraPercent, () -> ConfigManager.showOverlay.get() && ConfigManager.showJacobsHelper.get() && ConfigManager.jacobsHelperAlert.get());

        addSection("Angle Helper");
        addCheckBox(ConfigManager.showAngleHelper, () -> ConfigManager.showOverlay.get());
        addKeybindBox(ConfigManager.angleHelperToggleKey, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get());
        addDecimalSlider(ConfigManager.angleHelperOpacity, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get());
        addCheckBox(ConfigManager.showYaw, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get());
        addDecimalSlider(ConfigManager.angleHelperYaw, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get() && ConfigManager.showYaw.get());
        addDecimalSlider(ConfigManager.yawTolerance, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get() && ConfigManager.showYaw.get());
        addCheckBox(ConfigManager.oppositeYaw, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get() && ConfigManager.showYaw.get());
        addCheckBox(ConfigManager.showPitch, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get());
        addDecimalSlider(ConfigManager.angleHelperPitch, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get() && ConfigManager.showPitch.get());
        addDecimalSlider(ConfigManager.pitchTolerance, () -> ConfigManager.showOverlay.get() && ConfigManager.showAngleHelper.get() && ConfigManager.showPitch.get());

        addSection("Miscellaneous");
        addCheckBox(ConfigManager.logInfo);

        maxScrollHeight = height * 3/4 - offset;
    }

    /** helper methods for adding GUI elements */
    private void addTitleLabel(String text, int y) {
        elementList.add(new TextLabel(text, width * 9/16 - fontRendererObj.getStringWidth(text), y, 2f, Integer.MAX_VALUE, Colors.WHITE));
    }

    private void addSection(String name) {
        MenuSectionLabel label = new MenuSectionLabel(name, width / 16, height / 4 + sections * 18, 1.5f, width / 8, offset + height / 4);
        label.scrollable = false;
        elementList.add(label);
        addTitleLabel(name, height / 4 + offset + 12);
        offset += 40;
        sections++;
    }

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

    private void addKeybindBox(KeybindSetting setting) {
        addKeybindBox(setting, () -> true);
    }

    private void addKeybindBox(KeybindSetting setting, Supplier<Boolean> enabledCondition) {
        int prev = offset;
        addOtherStuff(setting, 54);
        elementList.add(new KeybindBox(
                setting,
                right - 54, height / 4 + prev + (offset - prev) / 2 - 20,
                54, 20,
                enabledCondition
        ));
    }

    private void addIntegerSlider(IntegerSetting setting) {
        addIntegerSlider(setting, () -> true);
    }

    private void addIntegerSlider(IntegerSetting setting, Supplier<Boolean> enabledCondition) {
        int prev = offset;
        addOtherStuff(setting, 162);
        elementList.add(new IntegerSlider(
                setting,
                right - 100, height / 4 + prev + (offset - prev) / 2 - 26,
                enabledCondition
        ));
    }

    private void addDecimalSlider(DecimalSetting setting) {
        addDecimalSlider(setting, () -> true);
    }

    private void addDecimalSlider(DecimalSetting setting, Supplier<Boolean> enabledCondition) {
        int prev = offset;
        addOtherStuff(setting, 162);
        elementList.add(new DecimalSlider(
                setting,
                right - 100, height / 4 + prev + (offset - prev) / 2 - 26,
                enabledCondition
        ));
    }

    private void addOtherStuff(Settings.AbstractSetting<?> setting, int spaceNeeded) {
        int top = height / 4 + offset;
        TextLabel name;
        TextLabel desc;
        elementList.add(name = new TextLabel(setting.key, left, top, 1.3f, maxDescWidth - spaceNeeded));
        elementList.add(desc = new TextLabel(setting.description, left, top + name.getHeight() + 3, maxDescWidth - spaceNeeded, Colors.GRAY));
        elementList.add(new Line(left, right, desc.yPosition + desc.getHeight() + 9));
        offset += 24 + name.getHeight() + desc.getHeight();
    }

    /** input handling methods */
    private static void updateAmountScrolled() {
        updateAmountScrolled(amountScrolled + Mouse.getEventDWheel() / 5);
    }

    public static void updateAmountScrolled(int newAmount) {
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        boolean close = true;
        for (ModGuiElement e : elementList) {
            if (e.keyTyped(typedChar, keyCode)) {
                close = false;
            }
        }
        if (close) {
            super.keyTyped(typedChar, keyCode);
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
