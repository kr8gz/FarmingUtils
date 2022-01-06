package mod.kr8gz.farmingutils.gui.settings.screens;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.gui.settings.elements.*;
import mod.kr8gz.farmingutils.util.Colors;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.types.BooleanSetting;
import test.kr8gz.settings.types.DecimalSetting;
import test.kr8gz.settings.types.IntegerSetting;

import java.util.function.Supplier;

import static mod.kr8gz.farmingutils.config.ConfigManager.*;

public class GuiModConfig extends ModGuiScreen {
    static int left;
    static int right;
    static int maxWidth;
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
        maxWidth = right - left;
        maxDescWidth = maxWidth - 16;
        offset = height / 4;
        sections = 0;

        addTitleLabel(FarmingUtils.NAME + " Settings", height / 8);
        if (height / 8 < 20) offset += 20;

        addSection("General");
        addCheckBox(enableOverlay);
        addIntegerSlider(roudingPrecision, () -> enableOverlay.get());
        addDecimalSlider(overlayScale, () -> enableOverlay.get());
        addDecimalSlider(overlayBackgroundOpacity, () -> enableOverlay.get());

        addSection("BPS");
        addCheckBox(enableBPS, () -> enableOverlay.get());
        addEditListButton(new GuiEditListSetting<>(this, bpsTimes), () -> enableOverlay.get() && enableBPS.get());

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

        int w = maxWidth / 2 - 4;
        int xOffs = maxWidth / 2 + 4;

        elementList.add(new TextButton(left, offset, w, 32, "Edit Overlay", 1.3f, Colors.LIGHTBLUE) {
            @Override
            protected void action() {
                mc.displayGuiScreen(new GuiEditOverlay(GuiModConfig.this));
            }
        });

        elementList.add(new TextButton(left + xOffs, offset, w, 32, "Reset All", 1.3f, Colors.RED2) {
            @Override
            protected void action() {
                mc.displayGuiScreen(new GuiResetAllConfirmation(GuiModConfig.this));
            }
        });

        elementList.add(new TextButton(left, offset + 40, w, 32, "Import Settings", 1.3f, Colors.GREEN2) {
            @Override
            protected void action() {
                mc.displayGuiScreen(new GuiImportSettings(GuiModConfig.this));
            }
        });

        elementList.add(new TextButton(left + xOffs, offset + 40, w, 32, "Export Settings", 1.3f, Colors.YELLOW2) {
            @Override
            protected void action() {
                mc.displayGuiScreen(new GuiExportSettings(GuiModConfig.this));
            }
        });

        offset += 80;

        maxScrollHeight = height - offset;
    }

    /** helper methods for adding GUI elements */
    private void addTitleLabel(String text, int y) {
        elementList.add(new TextLabel(text, width * 9/16 - fontRendererObj.getStringWidth(text), y, 2f, Integer.MAX_VALUE, Colors.WHITE));
    }

    private void addSection(String name) {
        MenuSectionLabel label = new MenuSectionLabel(this, name, width / 20, height / 4 + sections * 18, 1.5f, width * 3 / 20, offset);
        label.scrollable = false;
        elementList.add(label);
        addTitleLabel(name, offset + 12);

//        TextLabel infoLabel;
//        if (info != null) {
//            elementList.add(infoLabel = new TextLabel(info, left, offset + 38, maxWidth, Colors.YELLOW));
//            offset += infoLabel.height + 9;
//        }

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
                right - 32, prev + (offset - prev) / 2 - 26,
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
                right - 100, prev + (offset - prev) / 2 - 26,
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
                right - 100, prev + (offset - prev) / 2 - 26,
                enabledCondition
        ));
    }

    private void addEditListButton(GuiEditListSetting<?> screen) {
        addEditListButton(screen, () -> true);
    }

    private void addEditListButton(GuiEditListSetting<?> screen, Supplier<Boolean> enabledCondition) {
        int prev = offset;
        addOtherStuff(screen.boundListSetting, 110);
        elementList.add(new TextButton(right - 102, prev + (offset - prev) / 2 - 25, 100, 28, "Edit", 1.3f, Colors.WHITE, enabledCondition) {
            @Override
            protected void action() {
                mc.displayGuiScreen(screen);
            }
        });
    }

    private void addOtherStuff(Settings.AbstractSetting<?> setting, int spaceNeeded) {
        TextLabel name;
        TextLabel desc;
        elementList.add(name = new TextLabel(setting.key, left, offset, 1.3f, maxDescWidth - spaceNeeded));
        elementList.add(desc = new TextLabel(setting.description, left, offset + name.height + 3, maxDescWidth - spaceNeeded, Colors.GRAY));
        elementList.add(new Line(left, right, desc.yPosition + desc.height + 9));
        offset += 24 + name.height + desc.height;
    }
}
