package mod.kr8gz.farmingutils.overlay;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import mod.kr8gz.farmingutils.util.ScoreboardHelper;
import net.minecraft.client.multiplayer.ServerData;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnnecessaryUnicodeEscape") // they're actually necessary because otherwise it doesn't compile
public class JacobsHelperOverlay extends OverlaySection {
    private static final int CONTEST_DURATION_SECONDS = 20 * 60;

    private static final ScheduledExecutorService resetScheduler = Executors.newScheduledThreadPool(1);
    private static boolean isResetScheduled;

    private static final ArrayList<Integer> cropsDifferencesPerSecond = new ArrayList<>();
    private static int lastCropCount;
    private static MedalTier lastAlertMedal;
    private static int alertShownSinceTick = -1;

    public JacobsHelperOverlay() {
        super(ConfigManager.jacobPosX, ConfigManager.jacobPosY, "Jacob's Helper", Colors.YELLOW);
    }

    static {
        reset();
    }

    private static void reset() {
        cropsDifferencesPerSecond.clear();
        lastCropCount = 0;
        lastAlertMedal = null;

        for (MedalTier medal : MedalTier.values()) {
            medal.resetData();
        }

        isResetScheduled = false;
    }

    private static void scheduleResetAfterContest() {
        if (!isResetScheduled) {
            isResetScheduled = true;
            resetScheduler.schedule(JacobsHelperOverlay::reset, CONTEST_DURATION_SECONDS, TimeUnit.SECONDS);
        }
    }

    @Override
    protected boolean shouldRender() {
        ServerData serverData = mc.getCurrentServerData();
        boolean shouldRender = super.shouldRender()
                && ConfigManager.enableJacobsHelper.get()
                && serverData != null && serverData.serverIP.equals("hypixel.net")
                && ScoreboardHelper.stringList().contains("Jacob's Contest");

        if (shouldRender) scheduleResetAfterContest();
        return shouldRender;
    }

    @Override
    protected List<OverlayElement> getElementList() {
        List<OverlayElement> list = new ArrayList<>();
        List<List<String>> strings = new ArrayList<>();

        MedalDataParseResult currentMedalData = getCurrentMedalData().orElse(null);
        if (currentMedalData == null) return list;

        Optional<Integer> currentCropsPerSecond = updateCurrentCropsPerSecond(currentMedalData);
        updateOtherMedalData(currentMedalData);

        MedalTier[] allMedals = MedalTier.values();
        // reverse iterate (from highest to lowest) excluding last one (MedalTier.NONE)
        for (int i = allMedals.length - 1; i > 0; i--) {
            MedalTier medal = allMedals[i];
            if (medal.cropsAfterContest <= 0) continue;

            String color = medal.dataState.color;
            strings.add(Arrays.asList(medal.formatNameWithColor(color), color + "in 20 minutes", color + Helper.formatInt(medal.cropsAfterContest)));

            if (!ConfigManager.jacobsHelperAlert.get()) continue;

            int cropsRequiredForAlert = Helper.round(medal.cropsAfterContest * (100 + ConfigManager.alertExtraPercent.get()) / 100f);
            int cropsUntilAlert = cropsRequiredForAlert - currentMedalData.crops;
            int cropsDifference = Math.abs(cropsUntilAlert);

            boolean passedAlert = cropsUntilAlert <= 0;
            String sign = passedAlert ? "+" : "";

            if (ConfigManager.showCropsUntilAlert.get()) {
                strings.add(Arrays.asList("", color + (passedAlert ? "Crops ahead" : "Crops required"), color + sign + Helper.formatInt(cropsDifference)));
            }

            if (ConfigManager.showTimeUntilAlert.get()) {
                String timeDifferenceDisplay = currentCropsPerSecond
                        .map(cps -> Helper.round((double) cropsDifference / cps))
                        .map(Helper::formatTime)
                        .map(time -> sign + time)
                        .orElse("-");

                strings.add(Arrays.asList("", color + (passedAlert ? "Time ahead" : "Time until alert"), color + timeDifferenceDisplay));
            }

            if (!passedAlert) continue;

            if (lastAlertMedal == null || lastAlertMedal.ordinal() < medal.ordinal()) {
                lastAlertMedal = medal;
                alertShownSinceTick = getCurrentTick();
                Helper.playClientSound("random.orb");
            }

            int alertShowDuration = Helper.round(ConfigManager.alertShowDuration.get().floatValue() * 20);
            if (getCurrentTick() - alertShownSinceTick < alertShowDuration && lastAlertMedal == medal) {
                String alertText = medal.formatNameWithColor("") + " secured!";
                Helper.renderWithScale(4, () -> getFontRenderer().drawStringWithShadow(
                        alertText, (getScreenWidth() - mc.fontRendererObj.getStringWidth(alertText) * 4) / 8f, (getScreenHeight() - mc.fontRendererObj.FONT_HEIGHT * 4) / 8f, Colors.WHITE
                ));
            }
        }

        list.add(new OverlayElement(strings));
        return list;
    }

    static final UnaryOperator<String> MEDAL_NAME_SUBPATTERN = medalName -> "(?<medal>(?i)" + medalName + ")";
    static final String ALL_MEDAL_NAMES_SUBPATTERN = MEDAL_NAME_SUBPATTERN.apply(
            Stream.of(MedalTier.values())
                    .map(MedalTier::name)
                    .collect(Collectors.joining("|"))
    );
    static final String CROP_COUNT_SUBPATTERN = "(?<crops>\\d{1,3}(,\\d{3})*)";

    static Optional<MedalDataParseResult> parseMedalData(Pattern pattern) {
        return ScoreboardHelper.stringList().stream()
                .map(pattern::matcher)
                .filter(Matcher::find)
                .map(matcher -> {
                    MedalTier medal = MedalTier.fromString(matcher.group("medal"));
                    int crops = Integer.parseInt(matcher.group("crops").replace(",", ""));
                    return new MedalDataParseResult(medal, crops);
                })
                .findFirst();
    }

    private static final Pattern CURRENT_CROPS_PATTERN = Pattern.compile(" (Collected|" + ALL_MEDAL_NAMES_SUBPATTERN + " with) " + CROP_COUNT_SUBPATTERN);
    private static Optional<MedalDataParseResult> getCurrentMedalData() {
        return parseMedalData(CURRENT_CROPS_PATTERN);
    }

    private static Optional<Integer> updateCurrentCropsPerSecond(MedalDataParseResult currentMedalData) {
        if (currentMedalData.crops > lastCropCount && lastCropCount > 0) {
            cropsDifferencesPerSecond.add(currentMedalData.crops - lastCropCount);
            cropsDifferencesPerSecond.sort(Comparator.naturalOrder());
        }
        lastCropCount = currentMedalData.crops;

        return cropsDifferencesPerSecond.isEmpty()
                ? Optional.empty()
                // approximating median; list is sorted above
                : Optional.of(cropsDifferencesPerSecond.get(cropsDifferencesPerSecond.size() / 2));
    }

    private static void updateOtherMedalData(MedalDataParseResult currentMedalData) {
        for (MedalTier medal : MedalTier.values()) {
            if (medal.dataState == DataState.ACTIVE) {
                medal.dataState = DataState.OLD;
            }
        }

        boolean isHighestMedal = currentMedalData.medal == currentMedalData.medal.next();

        @SuppressWarnings("RegExpRepeatedSpace") // false warning
        // https://cdn.discordapp.com/attachments/1118231519060824097/1188838511932080168/image.png?ex=659bfb24&is=65898624&hm=2222ba64833eb7878ef0aa47b59cb0edb3a0e56ebcc769489584421528452c58
        Pattern pattern = Pattern.compile(isHighestMedal
                ? " \\+" + CROP_COUNT_SUBPATTERN + " over " + MEDAL_NAME_SUBPATTERN.apply(currentMedalData.medal.prev().name())
                : " " + MEDAL_NAME_SUBPATTERN.apply(currentMedalData.medal.next().name()) + " has \\+" + CROP_COUNT_SUBPATTERN
        );

        parseMedalData(pattern).ifPresent(result -> getElapsedSeconds().ifPresent(elapsedSeconds -> {
            int medalCrops = isHighestMedal ? currentMedalData.crops - result.crops : currentMedalData.crops + result.crops;
            result.medal.cropsAfterContest = Helper.round((double) medalCrops / elapsedSeconds * CONTEST_DURATION_SECONDS);
            result.medal.dataState = DataState.ACTIVE;
        }));
    }

    private static final Pattern REMAINING_TIME_PATTERN = Pattern.compile("[\\u25CB\\u2618]\\D*(?<minutes>\\d{1,2})m(?<seconds>\\d{1,2})s");
    private static Optional<Integer> getElapsedSeconds() {
        return ScoreboardHelper.stringList().stream()
                .map(REMAINING_TIME_PATTERN::matcher)
                .filter(Matcher::find)
                .map(matcher -> CONTEST_DURATION_SECONDS - Integer.parseInt(matcher.group("minutes")) * 60 - Integer.parseInt(matcher.group("seconds")))
                .findFirst();
    }

    static class MedalDataParseResult {
        final MedalTier medal;
        final int crops;

        private MedalDataParseResult(MedalTier medal, int crops) {
            this.medal = medal;
            this.crops = crops;
        }
    }

    public enum MedalTier {
        NONE(""),
        BRONZE("\u00A7c"),
        SILVER("\u00A77"),
        GOLD("\u00A76"),
        PLATINUM("\u00A73"),
        DIAMOND("\u00A7b");

        private final String color;

        private int cropsAfterContest;
        private DataState dataState;

        MedalTier(String color) {
            this.color = color;
            resetData();
        }

        private void resetData() {
            cropsAfterContest = 0;
            dataState = DataState.UNKNOWN;
        }

        public String formatNameWithColor(String color) {
            return (color.isEmpty() ? this.color : color) + "\u00A7l" + this.name() + "\u00A7r";
        }

        public MedalTier next() {
            MedalTier[] values = MedalTier.values();
            return values[Math.min(this.ordinal() + 1, values.length)];
        }

        public MedalTier prev() {
            return MedalTier.values()[Math.max(this.ordinal() - 1, 0)];
        }

        public static MedalTier fromString(String string) {
            try {
                return string == null ? MedalTier.NONE : MedalTier.valueOf(string.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public enum DataState {
        UNKNOWN("\u00A78"),
        OLD("\u00A77"),
        ACTIVE("");

        public final String color;

        DataState(String color) {
            this.color = color;
        }
    }
}
