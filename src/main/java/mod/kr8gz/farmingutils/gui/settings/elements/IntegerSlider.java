package mod.kr8gz.farmingutils.gui.settings.elements;

import test.kr8gz.settings.types.IntegerSetting;

import java.util.function.Supplier;

public class IntegerSlider extends Slider<IntegerSetting, Integer> {
    public IntegerSlider(IntegerSetting boundSetting, int x, int y) {
        super(boundSetting, x, y);
    }

    public IntegerSlider(IntegerSetting boundSetting, int x, int y, Supplier<Boolean> enabledCondition) {
        super(boundSetting, x, y, enabledCondition);
    }

    @Override
    void updateSliderPos(Integer value) {
        this.sliderPos = (float) (value - boundSetting.min) / (boundSetting.max - boundSetting.min);
    }

    @Override
    void updateValue(float sliderPos) {
        this.value = Math.round(boundSetting.min + (boundSetting.max - boundSetting.min) * sliderPos);
    }
}
