package mod.kr8gz.farmingutils.gui.overlay.elements;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.KeybindManager;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public abstract class OverlaySection extends Gui {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    public int xPosition;
    public int yPosition;
    private int width;
    private int height;

    public String name;
    public int color;

    public OverlaySection(int xPosition, int yPosition, String name, int color) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.name = name;
        this.color = color;
    }

    protected abstract List<OverlayElement> getElementList();

    private void resetWidthAndHeight() {
        width = mc.fontRendererObj.getStringWidth(name) + 8;
        height = 19;
    }

    protected boolean shouldRender() {
        return true;
    }

    public void draw() {
        if (ConfigManager.showOverlay.get() && KeybindManager.showOverlay && shouldRender()) {
            resetWidthAndHeight();
            float scale = ConfigManager.overlayScale.get().floatValue();
            Helper.glSetScale(scale);

            List<OverlayElement> elementList = getElementList();
            for (OverlayElement e : elementList) {
                if (e.getWidth() > width) width = e.getWidth();
                height += e.getHeight();
            }

            int x = xPosition < 0 ? (int) (screenWidth / scale - width + xPosition) : xPosition;
            int y = yPosition < 0 ? (int) (screenHeight / scale - height + yPosition) : yPosition;

            drawRect(x, y, x + width, y + height, Colors.rgba(Colors.BLACK, ConfigManager.overlayBackgroundOpacity.get().doubleValue()));
            mc.fontRendererObj.drawStringWithShadow(name, x + 4, y + 4, color);

            int offset = y + 17;
            for (OverlayElement e : elementList) {
                e.draw(x, offset);
                offset += e.getHeight();
            }

            Helper.glResetScale();
        }
    }

    private static int currentTick;
    private static int screenWidth;
    private static int screenHeight;

    public static class EventHandler {
        @SuppressWarnings("unused")
        @SubscribeEvent
        public void setVariables(RenderGameOverlayEvent.Text event) {
            ScaledResolution scaledRes = new ScaledResolution(mc);
            screenWidth = scaledRes.getScaledWidth();
            screenHeight = scaledRes.getScaledHeight();
        }

        @SuppressWarnings("unused")
        @SubscribeEvent
        public void tickEvent(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                currentTick++;
            }
        }
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getCurrentTick() {
        return currentTick;
    }
}
