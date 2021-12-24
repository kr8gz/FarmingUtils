package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;
import test.kr8gz.settings.types.Setting;

import java.util.function.Supplier;

public class SettingTextBox extends TextBox {
    public Setting<?> boundSetting;

    public SettingTextBox(ModGuiScreen screen, Setting<?> boundSetting, int x, int y, int width, int height, Supplier<Boolean> enabledCondition) {
        super(screen, x, y, width, height, enabledCondition);
        this.boundSetting = boundSetting;
        this.updateStateFromBoundSetting();
    }

    @Override
    public void updateStateFromBoundSetting() {
        this.value = boundSetting.get().toString();
    }

    @Override
    protected void checkBadInput() {
        badInput = false;
        try {
            if (!boundSetting.setFromString(value)) {
                badInput = true;
            }
        } catch (NumberFormatException e) {
            badInput = true;
        }
    }
}
