package mod.kr8gz.farmingutils.gui;

import test.kr8gz.settings.NumberSetting;

public class SliderTextBox<S extends NumberSetting<T>, T extends Number> extends SettingTextBox<S, T> {
    final Slider<S, T> slider;

    public SliderTextBox(Slider<S, T> slider, int x, int y, int width, int height) {
        super(slider.boundSetting, x, y, width, height);
        this.slider = slider;
    }

    @Override
    public void updateFromSetting() {
        this.value = boundSetting.get().toString().replaceFirst("\\.0$", "");
    }

    @Override
    public boolean isEnabled() {
        return slider.isEnabled();
    }

    @Override
    public boolean keyTyped(char character, int key) {
        if (super.keyTyped(character, key)) return true;

        if (isValidInput()) {
            slider.value = slider.boundSetting.get();
            slider.updateSliderPos(slider.value);
        }
        return false;
    }
}
