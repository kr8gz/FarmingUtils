package mod.kr8gz.farmingutils.gui.overlay.elements;

import mod.kr8gz.farmingutils.util.Colors;

import java.util.*;

public class OverlayList extends OverlayElement {
    final List<String> stringList;
    int color;

    public OverlayList(String... strings) {
        this(Colors.WHITE, strings);
    }

    public OverlayList(int color, String... strings) {
        this.color = color;
        this.stringList = new ArrayList<>(Arrays.asList(strings));
    }

    @Override
    int getWidth() {
        return mc.fontRendererObj.getStringWidth(Collections.max(stringList, Comparator.comparing(String::length))) + 8;
    }

    @Override
    int getHeight() {
        return stringList.size() * 10;
    }

    @Override
    void draw(int x, int y) {
        for (String string : stringList) {
            mc.fontRendererObj.drawStringWithShadow(string, x + 4, y + stringList.indexOf(string) * 10, color);
        }
    }
}
