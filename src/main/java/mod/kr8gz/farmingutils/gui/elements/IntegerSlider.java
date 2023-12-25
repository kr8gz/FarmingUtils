package mod.kr8gz.farmingutils.gui.elements;

import mod.kr8gz.farmingutils.gui.screens.ModGuiScreen;
import test.kr8gz.settings.types.IntegerSetting;

import java.util.function.Supplier;

public class IntegerSlider extends Slider<IntegerSetting, Integer> {
    public IntegerSlider(ModGuiScreen screen, IntegerSetting boundSetting, int x, int y, Supplier<Boolean> enabledCondition) {
        super(screen, boundSetting, x, y, enabledCondition);
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
