package mod.kr8gz.farmingutils.util;

public final class Colors {
    public static final int
            WHITE       = 0xffffff,
            GRAY        = 0x999999,
            BLACK       = 0,
            RED         = 0xff0000,
            DARKRED     = 0x990000,
            GREEN       = 0x00ff00,
            YELLOW      = 0xffff00,
            LIGHTBLUE   = 0x76a6c7;

    public static int rgba(int rgb) {
        return rgba(rgb, 1d);
    }

    public static int rgba(int rgb, double alpha) {
        return ((int) (alpha * 255) << 24) + rgb;
    }
}
