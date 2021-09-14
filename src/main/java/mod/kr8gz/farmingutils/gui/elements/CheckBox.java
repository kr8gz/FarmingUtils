package mod.kr8gz.farmingutils.gui.elements;

import mod.kr8gz.farmingutils.FarmingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import test.kr8gz.settings.types.BooleanSetting;

import java.util.function.Supplier;

public class CheckBox extends ModInteractableGuiElement {
    static final ResourceLocation checkboxTexture = new ResourceLocation(FarmingUtils.MODID, "textures/gui/checkbox.png");

    boolean state;
    public final BooleanSetting boundSetting;

    public CheckBox(BooleanSetting boundSetting, int x, int y) {
        this(boundSetting, x, y, () -> true);
    }

    public CheckBox(BooleanSetting boundSetting, int x, int y, Supplier<Boolean> enabledCondition) {
        super(x, y, 32, 32, enabledCondition);
        this.state = boundSetting.get();
        this.boundSetting = boundSetting;
    }

    @Override
    public void draw() {
        enabled = enabledCondition.get();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        if (!enabled) GL11.glColor4f(0.6f, 0.6f, 0.6f, 0.6f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(checkboxTexture);
        Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, state ? 32 : 0, 0, width, height, 64, 32);
        if (!enabled) GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void mouseReleased() {
        if (enabled && boundSetting.set(!boundSetting.get())) {
            state = !state;
        }
    }
}
