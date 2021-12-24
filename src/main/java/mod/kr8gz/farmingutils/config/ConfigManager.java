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

    /** overlay settings */
    public static BooleanSetting showOverlay = new BooleanSetting(settings,
            "Show Overlay", "Shows overlay.", true
    );
    
    public static BooleanSetting showWarnings = new BooleanSetting(settings,
            "Show Warnings", "Shows warnings in overlay.", true
    );

    public static IntegerSetting roudingPrecision = new IntegerSetting(settings,
            "Rounding Precision", "Precision for rounding floating-point numbers.", 2,
            1, 5, 1
    );

    public static DecimalSetting overlayScale = new DecimalSetting(settings,
            "Overlay Scale", "Sets scale of the overlay.", new BigDecimal("1.3"),
            new BigDecimal("0.1"), new BigDecimal("3"), new BigDecimal("0.1")
    );

    public static DecimalSetting overlayBackgroundOpacity = new DecimalSetting(settings,
            "Overlay Background Opacity", "Sets opacity of the overlay background.", new BigDecimal("0.5"),
            new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("0.01")
    );

    /** bps settings */
    public static BooleanSetting showBPS = new BooleanSetting(settings,
            "Show BPS", "Shows BPS overlay.", true
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

    /** jacobs helper settings */
    public static BooleanSetting showJacobsHelper = new BooleanSetting(settings,
            "Show Jacob's Contests Helper", "Shows crop counter for Jacob's Contests.", true
    );

    public static BooleanSetting jacobsHelperAlert = new BooleanSetting(settings,
            "Jacob's Contests Helper Alert", "Shows alert when required crop count after 20 minutes for gold medal is reached.", true
    );

    public static BooleanSetting showCropsUntilAlert = new BooleanSetting(settings,
            "Show Crops Until Alert", "Shows crops needed until alert will be shown.", true
    );

    public static BooleanSetting showTimeUntilAlert = new BooleanSetting(settings,
            "Show Time Until Alert", "Shows time needed until alert will be shown.", true
    );

    public static IntegerSetting alertExtraPercent = new IntegerSetting(settings,
            "Extra Percent Until Alert", "Alert will only be shown after reaching required crops + extra % specified here.", 5,
            -50, 100, 1
    );

    /** angle helper settings */
    public static BooleanSetting showAngleHelper = new BooleanSetting(settings,
            "Show Angle Helper", "Shows Angle Helper overlay.", true
    );

    public static DecimalSetting angleHelperOpacity = new DecimalSetting(settings,
            "Angle Helper Opacity", "Sets opacity of the Angle Helper overlay.", new BigDecimal("0.33"),
            new BigDecimal("0.1"), new BigDecimal("1"), new BigDecimal("0.01")
    );

    public static BooleanSetting showYaw = new BooleanSetting(settings,
            "Show Yaw", "Shows yaw in Angle Helper overlay.", true
    );

    public static DecimalSetting angleHelperYaw = new DecimalSetting(settings,
            "Angle Helper Yaw", "Sets desired yaw.", new BigDecimal("0"),
            new BigDecimal("-180"), new BigDecimal("180"), new BigDecimal("0.1")
    );

    public static DecimalSetting yawTolerance = new DecimalSetting(settings,
            "Yaw Tolerance", "Angle Helper will accept yaw +/- this setting.", new BigDecimal("0.5"),
            new BigDecimal("0.1"), new BigDecimal("10"), new BigDecimal("0.1")
    );

    public static BooleanSetting oppositeYaw = new BooleanSetting(settings,
            "Enable Opposite Yaw", "Angle Helper will accept yaw + 180 degrees.", true
    );

    public static BooleanSetting showPitch = new BooleanSetting(settings,
            "Show Pitch", "Shows pitch in Angle Helper overlay.", true
    );

    public static DecimalSetting angleHelperPitch = new DecimalSetting(settings,
            "Angle Helper Pitch", "Sets desired pitch.", new BigDecimal("0"),
            new BigDecimal("-90"), new BigDecimal("90"), new BigDecimal("0.1")
    );

    public static DecimalSetting pitchTolerance = new DecimalSetting(settings,
            "Pitch Tolerance", "Angle Helper will accept pitch +/- this setting.", new BigDecimal("0.5"),
            new BigDecimal("0.1"), new BigDecimal("10"), new BigDecimal("0.1")
    );

    /** misc settings */
    public static BooleanSetting logInfo = new BooleanSetting(settings,
            "Log Info", "Logs infos to a file for debugging purposes. Logs will be saved in .minecraft/logs/.farmingutils.log", false
    );

    static {
        settings.init();
    }
}
