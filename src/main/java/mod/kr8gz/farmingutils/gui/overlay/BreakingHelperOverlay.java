package mod.kr8gz.farmingutils.gui.overlay;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.KeybindManager;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlayElement;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;

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
            deltaX = deltaY = 0;
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

        if (active && ConfigManager.lockYawAndPitch.get() && bps > 0 && ConfigManager.enableAngleHelper.get() && !KeybindManager.angleHelperToggled.get() && AngleHelperOverlay.isValidAngles()) {
            if (lockTick != -1) {
                if (getCurrentTick() - lockTick >= Helper.round(ConfigManager.lockYawAndPitchDelay.get().floatValue() * 20)) {
                    mc.mouseHelper = customMouseHelper;
                }
            } else {
                lockTick = getCurrentTick();
            }
        } else {
            lockTick = -1;
        }

        if (bps == 0f && prevBPS > bps) {
            if (active) {
                Helper.playClientSound("random.orb");
            }
            mc.mouseHelper = normalMouseHelper;
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
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(FarmingUtils.MODID, "textures/gui/toggle.png"));
            drawModalRectWithCustomSizedTexture((int) (xPosition / getScale()), (int) (yPosition / getScale()), active ? 8 : 0, 0, 8, 8, 16, 8);
            GlStateManager.color(1f, 1f, 1f, 1f);
            Helper.glResetScale();
        } else {
            super.draw();
        }
    }
}
