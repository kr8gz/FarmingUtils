package test.kr8gz.settings.types;

import mod.kr8gz.farmingutils.util.Helper;
import test.kr8gz.settings.Settings;

import java.math.BigDecimal;

public class DecimalSetting extends NumberSetting<BigDecimal> {
    public final int precision;

    public DecimalSetting(Settings settings,
                          String key, String description, BigDecimal defaultValue,
                          BigDecimal min, BigDecimal max, BigDecimal step) {
        super(settings, key, description, defaultValue, min, max, step);
        assert min != null && max != null && step != null && Helper.isDivisible(max.subtract(min), step);
        this.precision = Math.max(min.scale(), step.scale());
    }

    @Override
    public boolean canSet(BigDecimal newValue) {
        return newValue.scale() <= precision
                && (min == null || newValue.compareTo(min) >= 0)
                && (max == null || newValue.compareTo(max) <= 0)
                && (step == null || Helper.isDivisible(newValue.subtract(min), step));
    }

    @Override
    public boolean setFromString(String string) {
        BigDecimal d = new BigDecimal(string);
        if (canSet(d)) {
            value = d;
            this.save();
            return true;
        } else {
            return false;
        }
    }
}
