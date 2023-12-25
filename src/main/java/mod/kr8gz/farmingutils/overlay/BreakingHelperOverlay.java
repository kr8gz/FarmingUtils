package mod.kr8gz.farmingutils.overlay;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.overlay.elements.OverlayElement;
import mod.kr8gz.farmingutils.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.util.Collections;
import java.util.List;

public class BreakingHelperOverlay extends OverlaySection {
    float prevBPS = 0;
    int lockTick = -1;
    boolean active;

    // TODO probably better to do with a mixin
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

                // TODO set delta to difference between edge of valid area and current pos instead of just cancelling the movement
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
        super(ConfigManager.breakingHelperPosX, ConfigManager.breakingHelperPosY, "Breaking Helper", Colors.LIGHTRED);
    }

    @Override
    protected boolean shouldRender() {
        return super.shouldRender() && (ConfigManager.enableBlockBreakAlert.get() || ConfigManager.lockYawAndPitch.get());
    }

    // TODO fix this mess
    void handleRotationLockAndAlert() {
        float bps = OverlayHandler.BPSOverlay.getBPS(Helper.round(ConfigManager.blockBreakAlertDelay.get().floatValue() * 20));

        active = ToggleKeybindHandler.showBreakingHelper.get();
        color = active ? Colors.LIGHTGREEN : Colors.LIGHTRED;

        if (super.shouldRender() && active && ConfigManager.lockYawAndPitch.get() && bps > 0 && AngleHelperOverlay.shouldRender() && AngleHelperOverlay.isValidAngles(0d, 0d)) {
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
        return Collections.singletonList(new OverlayElement(active ? "Active" : "Deactivated"));
    }

    @Override
    public void draw() {
        handleRotationLockAndAlert();

        if (ConfigManager.smallerBreakingHelperOverlayVersion.get() && shouldRender()) {
            width = height = Helper.round(8 * getScale());
            setXY();

            Helper.renderWithScale(getScale(), () -> Helper.renderWithColor(Colors.rgba(Colors.WHITE, 0.5f), () -> {
                mc.getTextureManager().bindTexture(new ResourceLocation(FarmingUtils.MODID, "textures/gui/toggle.png"));
                drawModalRectWithCustomSizedTexture((int) (xPosition / getScale()), (int) (yPosition / getScale()), active ? 8 : 0, 0, 8, 8, 16, 8);
            }));
        } else {
            super.draw();
        }
    }
}
