package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.screens.ModGuiScreen;
import test.kr8gz.settings.DecimalSetting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;

public class DecimalSlider extends Slider<DecimalSetting, BigDecimal> {
    public DecimalSlider(ModGuiScreen screen, DecimalSetting boundSetting, int x, int y, Supplier<Boolean> enabledCondition) {
        super(screen, boundSetting, x, y, enabledCondition);
    }

    @Override
    void updateSliderPos(BigDecimal value) {
        this.sliderPos = value.subtract(boundSetting.min).divide(boundSetting.max.subtract(boundSetting.min), 2, RoundingMode.HALF_UP).floatValue();
    }

    @Override
    void updateValue(float sliderPos) {
        this.value = boundSetting.min.add(boundSetting.max.subtract(boundSetting.min).multiply(new BigDecimal(sliderPos))).setScale(boundSetting.precision, RoundingMode.HALF_UP);
    }
}
