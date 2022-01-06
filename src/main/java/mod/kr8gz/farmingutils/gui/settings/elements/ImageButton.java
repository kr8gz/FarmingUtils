package mod.kr8gz.farmingutils.gui.settings.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public abstract class ImageButton extends Button {
    ResourceLocation image;
    int texW;
    int texH;

    public ImageButton(ResourceLocation image, int xPosition, int yPosition, int width, int height, int texW, int texH) {
        this(image, xPosition, yPosition, width, height, texW, texH, () -> true);
    }

    public ImageButton(ResourceLocation image, int xPosition, int yPosition, int width, int height, int texW, int texH, Supplier<Boolean> enabledCondition) {
        super(xPosition, yPosition, width, height, enabledCondition);
        this.image = image;
        this.texW = texW;
        this.texH = texH;
    }

    @Override
    public void draw() {
        enabled = enabledCondition.get();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        if (!enabled) GlStateManager.color(0.6f, 0.6f, 0.6f, 0.6f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, width, height, texW, texH);
        if (!enabled) GlStateManager.color(1f, 1f, 1f, 1f);
    }
}
