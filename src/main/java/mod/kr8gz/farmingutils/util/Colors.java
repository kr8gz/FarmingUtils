package mod.kr8gz.farmingutils.util;

// TODO this sucks, is javas Color class any better?
public final class Colors {
    public static final int
            WHITE       = 0xFFFFFF,
            GRAY        = 0x999999,
            BLACK       = 0x000000,

            RED         = 0xFF0000,
            LIGHTRED    = 0xD96868,
            DARKRED     = 0x990000,

            YELLOW      = 0xFFFF00,
            DARKYELLOW  = 0xE3D27B,

            GREEN       = 0x00FF00,
            LIGHTGREEN  = 0x81F081,

            LIGHTBLUE   = 0x76A6C7;

    public static int rgba(int rgb) {
        return rgba(rgb, 1d);
    }

    public static int rgba(int rgb, double alpha) {
        return (Helper.round(alpha * 255) << 24) + rgb;
    }
}
