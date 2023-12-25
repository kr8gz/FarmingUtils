package mod.kr8gz.farmingutils.gui.elements;

import test.kr8gz.settings.types.SimpleSetting;

public abstract class SettingTextBox extends TextBox implements ModGuiElement.BoundToSetting {
    public final SimpleSetting<?> boundSetting;

    public SettingTextBox(SimpleSetting<?> boundSetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.boundSetting = boundSetting;
        updateFromSetting();
    }

    @Override
    public void updateFromSetting() {
        this.value = boundSetting.get().toString();
    }

    @Override
    protected boolean checkValidInput() {
        try {
            return boundSetting.setFromString(value);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
