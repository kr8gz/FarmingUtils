package mod.kr8gz.farmingutils.data;

import mod.kr8gz.farmingutils.util.Helper;
import mod.kr8gz.farmingutils.util.ScoreboardHelper;

public class JacobsHelperData {
    public final MedalTier medal;
    private final boolean isPreviewMode;
    private DataState currentState = DataState.NONE;
    private float prevMinRequiredForMedalPerSecond = -1f;

    enum DataState {
        NONE    ("\u00A78"),
        OLD     ("\u00A77"),
        ACTIVE  ("");

        public final String color;

        DataState(String color) {
            this.color = color;
        }
    }

    public JacobsHelperData(MedalTier medal, boolean isPreviewMode) {
        this.medal = medal;
        this.isPreviewMode = isPreviewMode;
    }

    public String stateColor() {
        return currentState.color;
    }

    public static boolean isMedalName(String name) {
        return  name.equalsIgnoreCase("NONE")   ||
                name.equalsIgnoreCase("BRONZE") ||
                name.equalsIgnoreCase("SILVER") ||
                name.equalsIgnoreCase("GOLD");
    }

    public int getElapsedTime() {
        if (isPreviewMode) {
            return 750;
        }

        for (String line : ScoreboardHelper.stringList()) {
            String[] parts = line.split(" ");
            if (line.length() > 0 && line.charAt(0) == '\u25CB' && parts.length >= 3) {
                String time = parts[parts.length - 1];
                if (time.matches("\\d{1,2}m\\d{1,2}s")) {
                    int minutes = Integer.parseInt(time.substring(0, time.indexOf('m')));
                    int seconds = Integer.parseInt(time.substring(time.indexOf('m') + 1, time.indexOf('s')));
                    return 1200 - minutes * 60 - seconds;
                }
            }
        }
        return -1;
    }

    public MedalData getCurrentMedalData() {
        if (isPreviewMode) {
//            return new MedalData(MedalTier.BRONZE, 2000);
            return new MedalData(MedalTier.SILVER, 69696);
//            return new MedalData(MedalTier.GOLD, 314159);
//            return new MedalData(MedalTier.GOLD, 420420);
        }

        for (String line : ScoreboardHelper.stringList()) {
            String[] parts = line.split(" ");

            if (parts.length == 4 && parts[0].equals("") && isMedalName(parts[1]) && parts[2].equals("with")) {
                return new MedalData(MedalTier.valueOf(parts[1]), Integer.parseInt(parts[3].replace(",", "")));
            }
            else if (parts.length == 3 && parts[0].equals("") && parts[1].equals("Collected")) {
                return new MedalData(MedalTier.NONE, Integer.parseInt(parts[2].replace(",", "")));
            }
        }
        return null;
    }

    public int getMinRequiredForMedal() {
        if (isPreviewMode) {
//            if (medal == MedalTier.BRONZE) {
//                return 1188;

            if (medal == MedalTier.SILVER) {
                return 45000;
            } else if (medal == MedalTier.BRONZE) {
                prevMinRequiredForMedalPerSecond = 5.3f;
                return -1;

//            if (medal == MedalTier.GOLD) {
//                return 234567;
//            } else if (medal == MedalTier.SILVER) {
//                prevMinRequiredForMedalPerSecond = 61f;
//                return -1;

            } else {
                return -1;
            }
        }

        MedalData currentMedalData = getCurrentMedalData();
        if (currentMedalData != null) {
            for (String line : ScoreboardHelper.stringList()) {
                MedalData minRequiredForMedal = currentMedalData.getMinRequiredForMedal(line);
                if (minRequiredForMedal != null && minRequiredForMedal.medal == medal) {
                    int elapsedTime = getElapsedTime();
                    if (elapsedTime == -1) {
                        return -1;
                    }
                    prevMinRequiredForMedalPerSecond = minRequiredForMedal.crops / (float) elapsedTime;
                    return minRequiredForMedal.crops;
                }
            }
        }
        return -1;
    }

    public MedalData getMedalDataAfter20Minutes() {
        int elapsedTime = getElapsedTime();
        int minRequiredForMedal = getMinRequiredForMedal();
        if (elapsedTime != -1) {
            if (minRequiredForMedal != -1) {
                currentState = DataState.ACTIVE;
                return new MedalData(medal, Helper.round(1200f / elapsedTime * minRequiredForMedal));
            } else if (prevMinRequiredForMedalPerSecond != -1f) {
                currentState = DataState.OLD;
                return new MedalData(medal, Helper.round(1200f * prevMinRequiredForMedalPerSecond));
            } else {
                currentState = DataState.NONE;
                return new MedalData(medal, -1);
            }
        } else {
            return null;
        }
    }
}
