package mod.kr8gz.farmingutils.gui.overlay.elements;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.KeybindManager;
import mod.kr8gz.farmingutils.gui.settings.elements.ModGuiElement;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import test.kr8gz.settings.types.OverlayPositionSetting;

import java.util.List;

public abstract class OverlaySection extends ModGuiElement {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    final OverlayPositionSetting xSetting;
    final OverlayPositionSetting ySetting;
    public String name;
    public int color;

    boolean mouseDown;
    int clickOffsetX;
    int clickOffsetY;

    public OverlaySection(OverlayPositionSetting xSetting, OverlayPositionSetting ySetting, String name, int color) {
        super(0, 0, 0, 0); // these will always be set in draw()
        this.xSetting = xSetting;
        this.ySetting = ySetting;
        this.name = name;
        this.color = color;
    }

    protected abstract List<OverlayElement> getElementList();

    protected boolean shouldRender() {
        return ConfigManager.showOverlay.get() && !KeybindManager.overlayToggled.get();
    }

    protected boolean isPreviewMode() {
        return false;
    }

    @Override
    public void draw() {
        if (shouldRender() || isPreviewMode()) {
            float scale = getScale();

            int x = xSetting.get();
            int y = ySetting.get();
            xPosition = x < 0 ? screenWidth - width + x + 1 : x;
            yPosition = y < 0 ? screenHeight - height + y + 1 : y;
            width = (int) ((mc.fontRendererObj.getStringWidth(name) + 8) * scale);
            height = (int) (19 * scale);

            List<OverlayElement> elementList = getElementList();
            for (OverlayElement e : elementList) {
                if (e.getWidth() > width) width = e.getWidth();
                height += e.getHeight();
            }

            x = (int) (xPosition / scale);
            y = (int) (yPosition / scale);

            Helper.glSetScale(scale);
            drawRect(x, y, x + (int) (width / scale), y + (int) (height / scale), Colors.rgba(Colors.BLACK, ConfigManager.overlayBackgroundOpacity.get().doubleValue()));
            mc.fontRendererObj.drawStringWithShadow(name, x + 4, y + 4, color);

            int offset = y + 17;
            for (OverlayElement e : elementList) {
                e.draw(x, offset);
                offset += e.getHeight();
            }

            Helper.glResetScale();
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        mouseDown = true;
        clickOffsetX = mouseX - xPosition;
        clickOffsetY = mouseY - yPosition;
    }

    @Override
    public void mouseReleased() {
        mouseDown = false;
    }

    @Override
    public void mouseMovedGlobal(int mouseX, int mouseY) {
        if (mouseDown) {
            xSetting.set(screenWidth / 2 - xPosition < width / 2
                    ? Math.min(- screenWidth + mouseX - clickOffsetX + width - 1, -1)
                    : Math.max(0, mouseX - clickOffsetX));

            ySetting.set(screenHeight / 2 - yPosition < height / 2
                    ? Math.min(- screenHeight + mouseY - clickOffsetY + height - 1, -1)
                    : Math.max(0, mouseY - clickOffsetY));
        }
    }

    private static int currentTick;
    private static int screenWidth;
    private static int screenHeight;
    private static float scale;

    public static class EventHandler {
        @SuppressWarnings("unused")
        @SubscribeEvent
        public void setVariables(RenderGameOverlayEvent.Text event) {
            ScaledResolution scaledRes = new ScaledResolution(mc);
            screenWidth = scaledRes.getScaledWidth();
            screenHeight = scaledRes.getScaledHeight();
            scale = ConfigManager.overlayScale.get().floatValue();
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

    public static float getScale() {
        return scale;
    }
}
