package mod.kr8gz.farmingutils.config;

import mod.kr8gz.farmingutils.FarmingUtils;
import test.kr8gz.settings.Settings;
import test.kr8gz.settings.types.*;

import java.math.BigDecimal;

public class ConfigManager {
    public static final Settings settings = new Settings(FarmingUtils.MODID + "/config/settings.txt");

    /** overlay settings */
    public static final BooleanSetting showOverlay = new BooleanSetting(settings,
            "Show Overlay", "Shows overlay.", true
    );
    
    public static final BooleanSetting showWarnings = new BooleanSetting(settings,
            "Show Warnings", "Shows warnings in overlay.", true
    );

    public static final IntegerSetting roudingPrecision = new IntegerSetting(settings,
            "Rounding Precision", "Precision for rounding floating-point numbers.", 2,
            1, 5, 1
    );

    public static final DecimalSetting overlayScale = new DecimalSetting(settings,
            "Overlay Scale", "Sets scale of the overlay.", new BigDecimal("1.3"),
            new BigDecimal("0.1"), new BigDecimal("3"), new BigDecimal("0.1")
    );

    public static final DecimalSetting overlayBackgroundOpacity = new DecimalSetting(settings,
            "Overlay Background Opacity", "Sets opacity of the overlay background.", new BigDecimal("0.5"),
            new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("0.01")
    );

    /** bps settings */
    public static final BooleanSetting showBPS = new BooleanSetting(settings,
            "Show BPS", "Shows Blocks Per Second overlay.", true
    );

    public static final ListSetting<Integer> bpsTimes = new ListSetting<Integer>(settings,
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
    public static final BooleanSetting showJacobsHelper = new BooleanSetting(settings,
            "Show Jacob's Contests Helper", "Shows crop counter for Jacob's Contests.", true
    );

    public static final BooleanSetting jacobsHelperAlert = new BooleanSetting(settings,
            "Jacob's Contests Helper Alert", "Shows an alert when required crop count after 20 minutes for a medal is reached.", true
    );

    public static final BooleanSetting showCropsUntilAlert = new BooleanSetting(settings,
            "Show Crops Until Alert", "Shows crops needed until alert.", true
    );

    public static final BooleanSetting showTimeUntilAlert = new BooleanSetting(settings,
            "Show Time Until Alert", "Shows time needed until alert.", true
    );

    public static final IntegerSetting alertExtraPercent = new IntegerSetting(settings,
            "Extra Percent Until Alert", "Alert will only be shown after reaching required crops + extra % specified here.", 5,
            0, 50, 1
    );

    /** angle helper settings */
    public static final BooleanSetting showAngleHelper = new BooleanSetting(settings,
            "Show Angle Helper", "Shows Angle Helper overlay.", true
    );

    public static final DecimalSetting angleHelperOpacity = new DecimalSetting(settings,
            "Angle Helper Opacity", "Sets opacity of the Angle Helper overlay.", new BigDecimal("0.33"),
            new BigDecimal("0.1"), new BigDecimal("1"), new BigDecimal("0.01")
    );

    public static final BooleanSetting showYaw = new BooleanSetting(settings,
            "Show Yaw", "Shows yaw in Angle Helper overlay.", true
    );

    public static final DecimalSetting angleHelperYaw = new DecimalSetting(settings,
            "Angle Helper Yaw", "Sets desired yaw.", new BigDecimal("0"),
            new BigDecimal("-180"), new BigDecimal("180"), new BigDecimal("0.1")
    );

    public static final DecimalSetting yawTolerance = new DecimalSetting(settings,
            "Yaw Tolerance", "Angle Helper will accept yaw +/- this setting.", new BigDecimal("0.5"),
            new BigDecimal("0.1"), new BigDecimal("10"), new BigDecimal("0.1")
    );

    public static final BooleanSetting oppositeYaw = new BooleanSetting(settings,
            "Enable Opposite Yaw", "Angle Helper will accept yaw + 180 degrees.", true
    );

    public static final BooleanSetting showPitch = new BooleanSetting(settings,
            "Show Pitch", "Shows pitch in Angle Helper overlay.", true
    );

    public static final DecimalSetting angleHelperPitch = new DecimalSetting(settings,
            "Angle Helper Pitch", "Sets desired pitch.", new BigDecimal("0"),
            new BigDecimal("-90"), new BigDecimal("90"), new BigDecimal("0.1")
    );

    public static final DecimalSetting pitchTolerance = new DecimalSetting(settings,
            "Pitch Tolerance", "Angle Helper will accept pitch +/- this setting.", new BigDecimal("0.5"),
            new BigDecimal("0.1"), new BigDecimal("10"), new BigDecimal("0.1")
    );

    /** misc settings */
    public static final BooleanSetting logInfo = new BooleanSetting(settings,
            "Log Info", "Logs infos to a file for debugging purposes. Logs will be saved in .minecraft/farmingutils/log.txt", false
    );

    /** overlay position settings */
    public static final OverlayPositionSetting BPSPosX = new OverlayPositionSetting(settings, "BPSPosX", -4);
    public static final OverlayPositionSetting BPSPosY = new OverlayPositionSetting(settings, "BPSPosY", 4);
    public static final OverlayPositionSetting jacobPosX = new OverlayPositionSetting(settings, "jacobPosX", -4);
    public static final OverlayPositionSetting jacobPosY = new OverlayPositionSetting(settings, "jacobPosY", -4);

    static {
        settings.init();
    }
}
