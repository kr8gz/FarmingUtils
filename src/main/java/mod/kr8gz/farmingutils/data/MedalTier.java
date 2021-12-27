package mod.kr8gz.farmingutils.data;

public enum MedalTier {
    NONE    (""),
    BRONZE  ("\u00A7c\u00A7l"),
    SILVER  ("\u00A77\u00A7l"),
    GOLD    ("\u00A76\u00A7l");

    public final String color;

    MedalTier(String color) {
        this.color = color;
    }

    public String formatBold() {
        return this.color + this.name() + "\u00A7r\u00A7l";
    }

    public String formatColor(String color) {
        return (color.equals("") ? this.color : color) + "\u00A7l" + this.name();
    }
}
