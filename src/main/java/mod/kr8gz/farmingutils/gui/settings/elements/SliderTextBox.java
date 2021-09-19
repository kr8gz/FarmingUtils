package mod.kr8gz.farmingutils.gui.settings.elements;

import test.kr8gz.settings.types.NumberSetting;

import java.util.function.Supplier;

public class SliderTextBox<S extends NumberSetting<T>, T> extends TextBox {
    Slider<S, T> slider;

    public SliderTextBox(Slider<S, T> slider, int x, int y, int width, int height, String value, Supplier<Boolean> enabledCondition) {
        super(slider.boundSetting, x, y, width, height, value, enabledCondition);
        this.slider = slider;
    }

    @Override
    public boolean keyTyped(char character, int key) {
        if (super.keyTyped(character, key)) {
            if (!badInput) {
                slider.value = slider.boundSetting.get();
                slider.updateSliderPos(slider.value);
            }
            return true;
        }
        return false;
    }
}
