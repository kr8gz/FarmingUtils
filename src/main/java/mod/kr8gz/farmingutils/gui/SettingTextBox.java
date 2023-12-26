package mod.kr8gz.farmingutils.gui;

import test.kr8gz.settings.Settings;

public abstract class SettingTextBox<S extends Settings.AbstractSetting<T>, T> extends TextBox implements ModGuiElement.BoundToSetting {
    public final S boundSetting;

    public SettingTextBox(S boundSetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.boundSetting = boundSetting;
        updateFromSetting();
    }

    @Override
    public void updateFromSetting() {
        this.value = boundSetting.get().toString();
    }

    @Override
    protected boolean isValidInput() {
        try {
            return boundSetting.setFromString(value);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
