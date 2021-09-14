package mod.kr8gz.farmingutils.config;

import mod.kr8gz.farmingutils.FarmingUtils;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.types.BooleanSetting;
import test.kr8gz.settings.types.DecimalSetting;
import test.kr8gz.settings.types.IntegerSetting;
import test.kr8gz.settings.types.ListSetting;

import java.math.BigDecimal;

public class ConfigManager {
    public static final Settings settings = new Settings("config/" + FarmingUtils.MODID + ".cfg");

    public static IntegerSetting roudingPrecision = new IntegerSetting(settings,
            "Rounding Precision", "Precision for rounding floating-point numbers.", 2,
            1, 5, 1
    );

    public static DecimalSetting overlayScale = new DecimalSetting(settings,
            "Overlay Scale", "Overlay scale.", new BigDecimal("1.3"),
            new BigDecimal("0.1"), new BigDecimal("3"), new BigDecimal("0.1")
    );

    public static DecimalSetting padding = new DecimalSetting(settings,
            "Overlay Spacing", "Spacing between lines in overlay.", new BigDecimal("3"),
            new BigDecimal("0.1"), new BigDecimal("5"), new BigDecimal("0.1")
    );

    public static ListSetting<Integer> bpsTimes = new ListSetting<Integer>(settings,
            "BPS Times", "Overlay will show the average BPS for each time specified here.", new Integer[]{1, 10, 60}
    ) {
        @Override
        protected Integer parseSingleElementFromString(String string) {
            return Integer.parseInt(string);
        }

        @Override
        public boolean canSet(Integer e) {
            return true;
        }
    }; // FIXME this is ugly

    public static BooleanSetting showOverlay = new BooleanSetting(settings,
            "Show Overlay", "Shows overlay.", true
    );

    public static BooleanSetting showWarnings = new BooleanSetting(settings,
            "Show Warnings", "Shows warnings in overlay.", true
    );

    public static BooleanSetting showBPS = new BooleanSetting(settings,
            "Show BPS", "Shows BPS overlay.", true
    );

    public static BooleanSetting showJacobsHelper = new BooleanSetting(settings,
            "Show Jacob's Contests Helper", "Shows crop counter for Jacob's Contests.", true
    );

    public static BooleanSetting jacobsHelperAlert = new BooleanSetting(settings,
            "Jacob's Contests Helper Alert", "Shows alert when required crop count after 20 minutes for a specific medal is reached.", true
    );

    public static BooleanSetting showCropsUntilAlert = new BooleanSetting(settings,
            "Show Crops Until Alert", "Shows crops needed until alert will be shown.", true
    );

    public static BooleanSetting showTimeUntilAlert = new BooleanSetting(settings,
            "Show Time Until Alert", "Shows time needed until alert will be shown.", true
    );

    public static IntegerSetting alertExtraPercent = new IntegerSetting(settings,
            "Alert %%%%%%%%%help ,e", "what do i put here", 5, // todo
            -50, 100, 1
    );

    public static BooleanSetting logInfo = new BooleanSetting(settings,
            "Log Info", "Logs infos to a file for debugging purposes. Logs will be saved in .minecraft/logs/.farmingutils.log", false
    );

    static {
        settings.init();
    }
}
