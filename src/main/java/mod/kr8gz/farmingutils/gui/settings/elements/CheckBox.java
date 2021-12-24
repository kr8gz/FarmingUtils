package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import test.kr8gz.settings.types.BooleanSetting;

import java.util.function.Supplier;

public class CheckBox extends ModToggleableGuiElement {
    static final ResourceLocation checkboxTexture = new ResourceLocation(FarmingUtils.MODID, "textures/gui/checkbox.png");

    boolean state;
    public final BooleanSetting boundSetting;

    public CheckBox(ModGuiScreen screen, BooleanSetting boundSetting, int x, int y) {
        this(screen, boundSetting, x, y, () -> true);
    }

    public CheckBox(ModGuiScreen screen, BooleanSetting boundSetting, int x, int y, Supplier<Boolean> enabledCondition) {
        super(screen, x, y, 32, 32, enabledCondition);
        this.boundSetting = boundSetting;
        this.updateStateFromBoundSetting();
    }

    @Override
    public void updateStateFromBoundSetting() {
        this.state = boundSetting.get();
    }

    @Override
    public void draw() {
        this.updateStateFromBoundSetting();
        enabled = enabledCondition.get();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        if (!enabled) GlStateManager.color(0.6f, 0.6f, 0.6f, 0.6f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(checkboxTexture);
        drawModalRectWithCustomSizedTexture(xPosition, yPosition, state ? 32 : 0, 0, width, height, 64, 32);
        if (!enabled) GlStateManager.color(1f, 1f, 1f, 1f);
    }

    @Override
    public void mouseReleased() {
        if (enabled && boundSetting.set(!boundSetting.get())) {
            state = !state;
        }
    }
}
