package mod.kr8gz.farmingutils.gui.overlay.elements;

import mod.kr8gz.farmingutils.util.Colors;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class OverlayTable extends OverlayElement {
    final HashMap<String, String> stringMap;
    int color;

    public OverlayTable(HashMap<String, String> strings) {
        this(Colors.WHITE, strings);
    }

    public OverlayTable(int color, HashMap<String, String> stringMap) {
        this.color = color;
        this.stringMap = stringMap;
    }

    @Override
    int getWidth() {
        return mc.fontRendererObj.getStringWidth(Collections.max(stringMap.keySet(), Comparator.comparing(String::length))) +
                mc.fontRendererObj.getStringWidth(Collections.max(stringMap.values(), Comparator.comparing(String::length))) + 14;
    }

    @Override
    int getHeight() {
        return stringMap.size() * 10;
    }

    @Override
    void draw(int x, int y) {
        int i = 0;
        int split = mc.fontRendererObj.getStringWidth(Collections.max(stringMap.keySet(), Comparator.comparing(String::length))) + 10;
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            mc.fontRendererObj.drawStringWithShadow(entry.getKey(), x + 4, y + i * 10, Colors.WHITE);
            mc.fontRendererObj.drawStringWithShadow(entry.getValue(), x + split, y + i * 10, Colors.WHITE);
            i++;
        }
    }
}
