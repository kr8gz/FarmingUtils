package mod.kr8gz.farmingutils.gui.settings.screens;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.gui.settings.elements.*;
import mod.kr8gz.farmingutils.util.Colors;
import net.minecraft.client.Minecraft;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.types.BooleanSetting;
import test.kr8gz.settings.types.DecimalSetting;
import test.kr8gz.settings.types.IntegerSetting;

import java.util.function.Supplier;

import static mod.kr8gz.farmingutils.config.ConfigManager.*;

public class GuiModConfig extends ModGuiScreen {
    static int left;
    static int right;
    static int maxDescWidth;
    static int offset = 0;
    static int sections;

    public GuiModConfig(ModGuiScreen parentScreen) {
        super(parentScreen);
    }

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public void initGui() {
        super.initGui();

        left = width / 4;
        right = width * 7/8;
        maxDescWidth = right - left - 16;
        offset = 0;
        sections = 0;

        addTitleLabel(FarmingUtils.NAME + " Settings", height / 8 + offset);
        if (height / 8 < 20) offset = 20;

        addSection("General");
        addCheckBox(enableOverlay);
        addCheckBox(showWarnings, () -> enableOverlay.get());
        addIntegerSlider(roudingPrecision, () -> enableOverlay.get());
        addDecimalSlider(overlayScale, () -> enableOverlay.get());
        addDecimalSlider(overlayBackgroundOpacity, () -> enableOverlay.get());

        addSection("BPS");
        addCheckBox(enableBPS, () -> enableOverlay.get());
        // TODO someday bpsTimes

        addSection("Jacob's Helper");
        addCheckBox(enableJacobsHelper, () -> enableOverlay.get());
        addCheckBox(jacobsHelperAlert, () -> enableOverlay.get() && enableJacobsHelper.get());
        addCheckBox(showCropsUntilAlert, () -> enableOverlay.get() && enableJacobsHelper.get() && jacobsHelperAlert.get());
        addCheckBox(showTimeUntilAlert, () -> enableOverlay.get() && enableJacobsHelper.get() && jacobsHelperAlert.get());
        addIntegerSlider(alertExtraPercent, () -> enableOverlay.get() && enableJacobsHelper.get() && jacobsHelperAlert.get());

        addSection("Angle Helper");
        addCheckBox(enableAngleHelper, () -> enableOverlay.get());
        addDecimalSlider(angleHelperOpacity, () -> enableOverlay.get() && enableAngleHelper.get());
        addCheckBox(enableYaw, () -> enableOverlay.get() && enableAngleHelper.get());
        addDecimalSlider(angleHelperYaw, () -> enableOverlay.get() && enableAngleHelper.get() && enableYaw.get());
        addDecimalSlider(yawTolerance, () -> enableOverlay.get() && enableAngleHelper.get() && enableYaw.get());
        addCheckBox(oppositeYaw, () -> enableOverlay.get() && enableAngleHelper.get() && enableYaw.get());
        addCheckBox(enablePitch, () -> enableOverlay.get() && enableAngleHelper.get());
        addDecimalSlider(angleHelperPitch, () -> enableOverlay.get() && enableAngleHelper.get() && enablePitch.get());
        addDecimalSlider(pitchTolerance, () -> enableOverlay.get() && enableAngleHelper.get() && enablePitch.get());

        addSection("Breaking Helper");
        addCheckBox(enableBlockBreakAlert, () -> enableOverlay.get());
        addDecimalSlider(blockBreakAlertDelay, () -> enableOverlay.get() && enableBlockBreakAlert.get());
        addCheckBox(lockYawAndPitch, () -> enableOverlay.get() && enableAngleHelper.get());
        addDecimalSlider(lockYawAndPitchDelay, () -> enableOverlay.get() && enableAngleHelper.get() && lockYawAndPitch.get());
        addCheckBox(smallerBreakingHelperOverlayVersion, () -> enableOverlay.get() && (enableBlockBreakAlert.get() || (lockYawAndPitch.get() && enableAngleHelper.get())));

        addSection("Miscellaneous");
        addCheckBox(logInfo);

        int w = (right - left) / 2 - 4;
        int xOffs = (right - left) / 2 + 4;

        elementList.add(new Button(left, height / 4 + offset, w, 32, "Edit Overlay", 1.3f, Colors.LIGHTBLUE) {
            @Override
            protected void action() {
                Minecraft.getMinecraft().displayGuiScreen(new GuiEditOverlay(GuiModConfig.this));
            }
        });

        elementList.add(new Button(left + xOffs, height / 4 + offset, w, 32, "Reset All", 1.3f, Colors.RED2) {
            @Override
            protected void action() {
                Minecraft.getMinecraft().displayGuiScreen(new GuiResetAllConfirmation(GuiModConfig.this));
            }
        });

        elementList.add(new Button(left, height / 4 + offset + 40, w, 32, "Import Settings", 1.3f, Colors.GREEN2) {
            @Override
            protected void action() {
                Minecraft.getMinecraft().displayGuiScreen(new GuiImportSettings(GuiModConfig.this));
            }
        });

        elementList.add(new Button(left + xOffs, height / 4 + offset + 40, w, 32, "Export Settings", 1.3f, Colors.YELLOW2) {
            @Override
            protected void action() {
                Minecraft.getMinecraft().displayGuiScreen(new GuiExportSettings(GuiModConfig.this));
            }
        });

        offset += 80;

        maxScrollHeight = height * 3/4 - offset;
    }

    /** helper methods for adding GUI elements */
    private void addTitleLabel(String text, int y) {
        elementList.add(new TextLabel(text, width * 9/16 - fontRendererObj.getStringWidth(text), y, 2f, Integer.MAX_VALUE, Colors.WHITE));
    }

    private void addSection(String name) {
        MenuSectionLabel label = new MenuSectionLabel(this, name, width / 20, height / 4 + sections * 18, 1.5f, width * 3 / 20, offset + height / 4);
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

    private void addIntegerSlider(IntegerSetting setting) {
        addIntegerSlider(setting, () -> true);
    }

    private void addIntegerSlider(IntegerSetting setting, Supplier<Boolean> enabledCondition) {
        int prev = offset;
        addOtherStuff(setting, 162);
        elementList.add(new IntegerSlider(
                this, setting,
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
                this, setting,
                right - 100, height / 4 + prev + (offset - prev) / 2 - 26,
                enabledCondition
        ));
    }

    private void addOtherStuff(Settings.AbstractSetting<?> setting, int spaceNeeded) {
        int top = height / 4 + offset;
        TextLabel name;
        TextLabel desc;
        elementList.add(name = new TextLabel(setting.key, left, top, 1.3f, maxDescWidth - spaceNeeded));
        elementList.add(desc = new TextLabel(setting.description, left, top + name.height + 3, maxDescWidth - spaceNeeded, Colors.GRAY));
        elementList.add(new Line(left, right, desc.yPosition + desc.height + 9));
        offset += 24 + name.height + desc.height;
    }
}
