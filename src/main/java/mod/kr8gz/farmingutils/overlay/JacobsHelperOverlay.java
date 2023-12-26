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

    private static final ArrayList<Integer> currentCropCountDifferencesPerSecond = new ArrayList<>();
    private int lastCropCount;
    private MedalTier lastAlertMedal;
    private int alertShownSinceTick = -1;

    public JacobsHelperOverlay() {
        super(ConfigManager.jacobPosX, ConfigManager.jacobPosY, "Jacob's Helper", Colors.YELLOW);
        reset();
    }

    private void reset() {
        currentCropCountDifferencesPerSecond.clear();
        lastCropCount = 0;
        lastAlertMedal = null;

        for (MedalTier medal : MedalTier.values()) {
            medal.resetData();
        }

        isResetScheduled = false;
    }

    private void scheduleResetAfterContest() {
        if (!isResetScheduled) {
            isResetScheduled = true;
            resetScheduler.schedule(this::reset, CONTEST_DURATION_SECONDS, TimeUnit.SECONDS);
        }
    }

    @Override
    protected boolean shouldRender() {
        ServerData serverData = mc.getCurrentServerData();
        return super.shouldRender()
            && ConfigManager.enableJacobsHelper.get()
            && serverData != null && serverData.serverIP.equals("hypixel.net")
            && ScoreboardHelper.stringList().contains("Jacob's Contest");
    }

    @Override
    protected List<OverlayElement> getElementList() {
        scheduleResetAfterContest();

        List<OverlayElement> list = new ArrayList<>();
        List<List<String>> strings = new ArrayList<>();

        MedalDataParser.Result currentMedalData = getCurrentMedalData().orElse(null);
        if (currentMedalData == null) return list;

        if (currentMedalData.crops > lastCropCount && lastCropCount > 0) {
            currentCropCountDifferencesPerSecond.add(currentMedalData.crops - lastCropCount);
        }
        lastCropCount = currentMedalData.crops;

        MedalTier.updateMedalData(currentMedalData);
        for (int i = MedalTier.values().length - 1; i >= 0; i--) {
            MedalTier medal = MedalTier.values()[i];
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
                double currentCropsPerSecond = currentCropCountDifferencesPerSecond.stream()
                        .mapToInt(val -> val)
                        .average()
                        .orElse(0.0);

                int timeDifference = Helper.round((double) cropsDifference / currentCropsPerSecond);
                strings.add(Arrays.asList("", color + (passedAlert ? "Time ahead" : "Time until alert"), color + sign + Helper.formatTime(timeDifference)));
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

    private static final Pattern CURRENT_CROPS_PATTERN = Pattern.compile(" (Collected|" + MedalDataParser.ALL_MEDAL_NAMES_SUBPATTERN + " with) " + MedalDataParser.CROP_COUNT_SUBPATTERN);
    private static Optional<MedalDataParser.Result> getCurrentMedalData() {
        return MedalDataParser.parseFrom(CURRENT_CROPS_PATTERN);
    }

    private static final Pattern REMAINING_TIME_PATTERN = Pattern.compile("[\\u25CB\\u2618].*(?<minutes>\\d{1,2})m(?<seconds>\\d{1,2})s");
    private static Optional<Integer> getElapsedSeconds() {
        return ScoreboardHelper.stringList().stream()
                .map(REMAINING_TIME_PATTERN::matcher)
                .filter(Matcher::find)
                .map(matcher -> CONTEST_DURATION_SECONDS - Integer.parseInt(matcher.group("minutes")) * 60 - Integer.parseInt(matcher.group("seconds")))
                .findFirst();
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

    public enum MedalTier {
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

        private static void updateMedalData(MedalDataParser.Result currentMedalData) {
            for (MedalTier medal : MedalTier.values()) {
                if (medal.dataState == DataState.ACTIVE) {
                    medal.dataState = DataState.OLD;
                }
            }

            boolean isHighestMedal = currentMedalData.medal == next(currentMedalData.medal);

            @SuppressWarnings("RegExpRepeatedSpace") // false warning
            // https://cdn.discordapp.com/attachments/1118231519060824097/1188838511932080168/image.png?ex=659bfb24&is=65898624&hm=2222ba64833eb7878ef0aa47b59cb0edb3a0e56ebcc769489584421528452c58
            Pattern pattern = Pattern.compile(isHighestMedal
                    ? " \\+" + MedalDataParser.CROP_COUNT_SUBPATTERN + " over " + MedalDataParser.MEDAL_NAME_SUBPATTERN.apply(prev(currentMedalData.medal).name())
                    : " " + MedalDataParser.MEDAL_NAME_SUBPATTERN.apply(next(currentMedalData.medal).name()) + " has \\+" + MedalDataParser.CROP_COUNT_SUBPATTERN
            );

            MedalDataParser.parseFrom(pattern).ifPresent(result -> getElapsedSeconds().ifPresent(seconds -> {
                int medalCrops = isHighestMedal ? currentMedalData.crops - result.crops : currentMedalData.crops + result.crops;
                result.medal.cropsAfterContest = Helper.round((double) medalCrops / seconds * CONTEST_DURATION_SECONDS);
                result.medal.dataState = DataState.ACTIVE;
            }));
        }

        public String formatNameWithColor(String color) {
            return (color.isEmpty() ? this.color : color) + "\u00A7l" + this.name() + "\u00A7r";
        }

        public static MedalTier next(MedalTier medal) {
            if (medal == null) return MedalTier.BRONZE;
            MedalTier[] values = MedalTier.values();
            return values[Math.min(medal.ordinal() + 1, values.length)];
        }

        public static MedalTier prev(MedalTier medal) {
            Objects.requireNonNull(medal);
            return MedalTier.values()[Math.max(medal.ordinal() - 1, 0)];
        }

        public static MedalTier fromString(String string) {
            try {
                return string == null ? null : MedalTier.valueOf(string.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    private static class MedalDataParser {
        static final String CROP_COUNT_SUBPATTERN = "(?<crops>\\d{1,3}(,\\d{3})*)";
        static final UnaryOperator<String> MEDAL_NAME_SUBPATTERN = medalName -> "(?<medal>(?i)" + medalName + ")";
        static final String ALL_MEDAL_NAMES_SUBPATTERN = MEDAL_NAME_SUBPATTERN.apply(
                Stream.of(MedalTier.values())
                        .map(MedalTier::name)
                        .collect(Collectors.joining("|"))
        );

        static class Result {
            final MedalTier medal;
            final int crops;

            private Result(MedalTier medal, int crops) {
                this.medal = medal;
                this.crops = crops;
            }
        }

        static Optional<Result> parseFrom(Pattern pattern) {
            return ScoreboardHelper.stringList().stream()
                    .map(pattern::matcher)
                    .filter(Matcher::find)
                    .map(matcher -> {
                        MedalTier medal = MedalTier.fromString(matcher.group("medal"));
                        int crops = Integer.parseInt(matcher.group("crops").replace(",", ""));
                        return new Result(medal, crops);
                    })
                    .findFirst();
        }
    }
}
