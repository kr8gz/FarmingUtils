package mod.kr8gz.farmingutils.gui.overlay.elements;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.*;

public class OverlayElement {
    final static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    final List<List<String>> strings;
    final List<List<String>> stringsByColumn = new ArrayList<>();
    int color;

    public OverlayElement(List<List<String>> strings) {
        this(Colors.WHITE, strings);
    }

    public OverlayElement(String[]... strings) {
        this.strings = new ArrayList<>();
        for (String[] row : strings) {
            this.strings.add(Arrays.asList(row));
        }
        initStringsByColumn();
    }

    public OverlayElement(int color, String... strings) {
        this.color = color;
        this.strings = new ArrayList<>();
        for (String row : strings) {
            this.strings.add(Collections.singletonList(row));
        }
        initStringsByColumn();
    }

    public OverlayElement(int color, List<List<String>> strings) {
        this.color = color;
        this.strings = strings;
        initStringsByColumn();
    }

    private void initStringsByColumn() {
        for (int i = 0; i < strings.get(0).size(); i++) {
            this.stringsByColumn.add(new ArrayList<>());
        }
        int i;
        for (List<String> row : strings) {
            i = 0;
            for (String s : row) {
                stringsByColumn.get(i).add(s);
                i++;
            }
        }
    }

    int getWidth() {
        int width = 2;
        for (List<String> col : stringsByColumn) {
            width += fr.getStringWidth(Collections.max(col, Comparator.comparing(fr::getStringWidth))) + 6;
        }
        return Helper.round(OverlaySection.getScale() * width);
    }

    int getHeight() {
        return Helper.round(strings.size() * 10 * OverlaySection.getScale());
    }

    void draw(int xStart, int yStart) {
        int x;
        int xi;
        int y = yStart;
        for (List<String> row : strings) {
            x = xStart + 4;
            xi = 0;
            for (String s : row) {
                fr.drawStringWithShadow(s, x, y, color);
                x += fr.getStringWidth(Collections.max(stringsByColumn.get(xi), Comparator.comparing(fr::getStringWidth))) + 6;
                xi++;
            }
            y += 10;
        }
    }
}
