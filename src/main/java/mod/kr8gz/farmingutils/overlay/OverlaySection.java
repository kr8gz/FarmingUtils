package mod.kr8gz.farmingutils.overlay;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.ModGuiElement;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import test.kr8gz.settings.OverlayPositionSetting;

import java.util.List;

public abstract class OverlaySection extends ModGuiElement {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    protected final OverlayPositionSetting xSetting;
    protected final OverlayPositionSetting ySetting;
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
        return ConfigManager.enableOverlay.get() && ToggleKeybindHandler.showOverlay.get();
    }

    /** call only after setting {@code width} and {@code height} */
    protected void setXY() {
        int x = xSetting.get();
        int y = ySetting.get();
        xPosition = x < 0 ? screenWidth - width + x + 1 : x;
        yPosition = y < 0 ? screenHeight - height + y + 1 : y;
    }

    @Override
    public void draw() {
        if (!shouldRender()) return;

        width = Helper.round((getFontRenderer().getStringWidth(name) + 8) * scale);
        height = Helper.round(19 * scale);
        List<OverlayElement> elementList = getElementList();
        for (OverlayElement e : elementList) {
            if (e.getWidth() > width) width = e.getWidth();
            height += e.getHeight();
        }
        setXY();

        int x = Helper.round(xPosition / scale);
        int y = Helper.round(yPosition / scale);

        Helper.renderWithScale(scale, () -> {
            int bgColor = Colors.rgba(Colors.BLACK, ConfigManager.overlayBackgroundOpacity.get().doubleValue());
            drawRect(x, y, x + Helper.round(width / scale), y + Helper.round(height / scale), bgColor);
            getFontRenderer().drawStringWithShadow(name, x + 4, y + 4, color);

            int offset = y + 17;
            for (OverlayElement e : elementList) {
                e.draw(x, offset);
                offset += e.getHeight();
            }
        });
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
