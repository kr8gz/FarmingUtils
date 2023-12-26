package mod.kr8gz.farmingutils.overlay;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO i dont like this file either
public class OverlayElement {
    final static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    final List<List<String>> strings;
    final List<List<String>> stringsByColumn = new ArrayList<>();

    public OverlayElement(List<List<String>> strings) {
        this.strings = strings;
        initStringsByColumn();
    }

    public OverlayElement(String... strings) {
        this.strings = new ArrayList<>();
        for (String row : strings) {
            this.strings.add(Collections.singletonList(row));
        }
        initStringsByColumn();
    }

    private void initStringsByColumn() {
        if (strings.isEmpty()) return;

        // strings.get(0).forEach(column -> stringsByColumn.add(new ArrayList<>()));
        // for (List<String> row : strings) {
        //     int i = 0;
        //     for (String s : row) {
        //         stringsByColumn.get(i).add(s);
        //         i++;
        //     }
        // }

        // TODO i didnt test it yet; if this works, then remove this and the above comment
        IntStream.range(0, strings.get(0).size()).forEach(i ->
                stringsByColumn.add(strings.stream()
                        .map(row -> row.get(i))
                        .collect(Collectors.toList())
                )
        );
    }

    int getWidth() {
        int width = 2;
        for (List<String> col : stringsByColumn) {
            width += fontRenderer.getStringWidth(Collections.max(col, Comparator.comparing(fontRenderer::getStringWidth))) + 6;
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
                fontRenderer.drawStringWithShadow(s, x, y, Colors.WHITE);
                x += fontRenderer.getStringWidth(Collections.max(stringsByColumn.get(xi), Comparator.comparing(fontRenderer::getStringWidth))) + 6;
                xi++;
            }
            y += 10;
        }
    }
}
