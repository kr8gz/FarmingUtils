package mod.kr8gz.farmingutils.gui.overlay.elements;

import net.minecraft.client.Minecraft;

public abstract class OverlayElement {
    final static Minecraft mc = Minecraft.getMinecraft();

    abstract int getWidth();
    abstract int getHeight();
    abstract void draw(int x, int y);
}
