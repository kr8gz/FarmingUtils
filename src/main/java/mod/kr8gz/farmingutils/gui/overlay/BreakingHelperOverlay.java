package mod.kr8gz.farmingutils.gui.overlay;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.KeybindManager;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlayElement;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.util.Collections;
import java.util.List;

public class BreakingHelperOverlay extends OverlaySection {
    float prevBPS = 0;
    int lockTick = -1;
    boolean active;

    final MouseHelper normalMouseHelper = new MouseHelper();
    final MouseHelper customMouseHelper = new MouseHelper() {
        @Override
        public void mouseXYChange() {
            deltaX = Mouse.getDX();
            deltaY = Mouse.getDY();
            if (AngleHelperOverlay.isValidAngles(0d, 0d)) {
                // copied from EntityRenderer:1062 and modified
                float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
                float f1 = f * f * f * 8.0F;
                float f2 = (float) deltaX * f1;
                float f3 = (float) deltaY * f1;

                if (!AngleHelperOverlay.isValidAngles(f2 * 0.15D, 0d)) {
                    deltaX = 0;
                }
                if (!AngleHelperOverlay.isValidAngles(0d, (mc.gameSettings.invertMouse ? f3 : -f3) * 0.15D)) {
                    deltaY = 0;
                }
            }
        }
    };

    public BreakingHelperOverlay() {
        super(ConfigManager.breakingHelperPosX, ConfigManager.breakingHelperPosY, "Breaking Helper", Colors.RED2);
    }

    @Override
    protected boolean shouldRender() {
        return super.shouldRender() && (ConfigManager.enableBlockBreakAlert.get() || ConfigManager.lockYawAndPitch.get());
    }

    void lockAndAlertStuff() {
        float bps = BPSOverlay.getBPS(Helper.round(ConfigManager.blockBreakAlertDelay.get().floatValue() * 20));

        active = !KeybindManager.breakingHelperToggled.get();
        color = active ? Colors.GREEN2 : Colors.RED2;

        if (active && super.shouldRender() && ConfigManager.lockYawAndPitch.get() && bps > 0 && ConfigManager.enableAngleHelper.get() && !KeybindManager.angleHelperToggled.get() && AngleHelperOverlay.isValidAngles(0d, 0d)) {
            if (lockTick != -1) {
                if (getCurrentTick() - lockTick >= Helper.round(ConfigManager.lockYawAndPitchDelay.get().floatValue() * 20)) {
                    mc.mouseHelper = customMouseHelper;
                }
            } else {
                lockTick = getCurrentTick();
            }
        } else {
            mc.mouseHelper = normalMouseHelper;
            lockTick = -1;
        }

        if (bps == 0f && prevBPS > bps && super.shouldRender()) {
            if (active && ConfigManager.enableBlockBreakAlert.get()) {
                Helper.playClientSound("random.orb");
            }
        }

        prevBPS = bps;
    }

    @Override
    protected List<OverlayElement> getElementList() {
        return Collections.singletonList(new OverlayElement(new String[] {active ? "Active" : "Deactivated"}));
    }

    @Override
    public void draw() {
        lockAndAlertStuff();

        if (ConfigManager.smallerBreakingHelperOverlayVersion.get() && (shouldRender() || isPreviewMode())) {
            width = height = Helper.round(8 * getScale());
            setXY();

            Helper.glSetScale(getScale());
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1f, 1f, 1f, 0.5f);
            mc.getTextureManager().bindTexture(new ResourceLocation(FarmingUtils.MODID, "textures/gui/toggle.png"));
            drawModalRectWithCustomSizedTexture((int) (xPosition / getScale()), (int) (yPosition / getScale()), active ? 8 : 0, 0, 8, 8, 16, 8);
            GlStateManager.color(1f, 1f, 1f, 1f);
            Helper.glResetScale();
        } else {
            super.draw();
        }
    }
}
