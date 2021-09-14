package mod.kr8gz.farmingutils.gui.elements;

import test.kr8gz.settings.types.DecimalSetting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;

public class DecimalSlider extends Slider<DecimalSetting, BigDecimal> {
    public DecimalSlider(DecimalSetting boundSetting, int x, int y) {
        super(boundSetting, x, y);
    }

    public DecimalSlider(DecimalSetting boundSetting, int x, int y, Supplier<Boolean> enabledCondition) {
        super(boundSetting, x, y, enabledCondition);
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
