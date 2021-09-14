package mod.kr8gz.farmingutils.data;

import java.util.Locale;

public class MedalData {
    public final String name;
    public final int crops;

    public MedalData(String name, int crops) {
        this.name = isMedalName(name) ? name.toUpperCase(Locale.ROOT) : "NONE";
        this.crops = crops;
    }

    public static boolean isMedalName(String name) {
        return  name.equalsIgnoreCase("NONE")   ||
                name.equalsIgnoreCase("BRONZE") ||
                name.equalsIgnoreCase("SILVER") ||
                name.equalsIgnoreCase("GOLD");
    }

    public int getColor() {
        switch (name) {
            case "GOLD":
                return 0xffaa00;
            case "SILVER":
                return 0xaaaaaa;
            case "BRONZE":
                return 0xff5555;
        }
        return 0xffffff;
    }

    int parseGrayText(String line, int from) {
        return Integer.parseInt(line.substring(from).replace(",", ""));
    }

    public MedalData getMinRequiredForMedal(String line) {
        switch (name) {
            case "GOLD":
                if (line.startsWith(" +") && line.endsWith(" over silver"))
                    return new MedalData("GOLD", crops - Integer.parseInt(line.substring(2, line.indexOf(' ', 2)).replace(",", "")));
                break;
            case "SILVER":
                if (line.startsWith(" gold has +"))
                    return new MedalData("GOLD", crops + parseGrayText(line, 10));
                break;
            case "BRONZE":
                if (line.startsWith(" silver has +"))
                    return new MedalData("SILVER", crops + parseGrayText(line, 12));
                break;
            case "NONE":
                if (line.startsWith(" Bronze has +"))
                    return new MedalData("BRONZE", crops + parseGrayText(line, 12));
                break;
        }
        return null;
    }

    @Override
    public String toString() {
        return "MedalData{" +
                "name='" + name + '\'' +
                ", crops=" + crops +
                '}';
    }
}