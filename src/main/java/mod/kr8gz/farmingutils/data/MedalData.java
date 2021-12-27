package mod.kr8gz.farmingutils.data;

public class MedalData {
    public final MedalTier medal;
    public final int crops;

    public MedalData(MedalTier medal, int crops) {
        this.medal = medal;
        this.crops = crops;
    }

    int parseGrayText(String line, int from) {
        return Integer.parseInt(line.substring(from).replace(",", ""));
    }

    public MedalData getMinRequiredForMedal(String line) {
        switch (medal) {
            case GOLD:
                if (line.startsWith(" +") && line.endsWith(" over silver"))
                    return new MedalData(MedalTier.GOLD, crops - Integer.parseInt(line.substring(2, line.indexOf(' ', 2)).replace(",", "")));
                break;
            case SILVER:
                if (line.startsWith(" gold has +"))
                    return new MedalData(MedalTier.GOLD, crops + parseGrayText(line, 10));
                break;
            case BRONZE:
                if (line.startsWith(" silver has +"))
                    return new MedalData(MedalTier.SILVER, crops + parseGrayText(line, 12));
                break;
            case NONE:
                if (line.startsWith(" Bronze has +"))
                    return new MedalData(MedalTier.BRONZE, crops + parseGrayText(line, 12));
                break;
        }
        return null;
    }

    @Override
    public String toString() {
        return "MedalData{" +
                "name='" + medal + '\'' +
                ", crops=" + crops +
                '}';
    }
}