package mod.kr8gz.farmingutils.gui.elements;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class ImageButton extends Button {
    final ResourceLocation image;
    final int textureWidth;
    final int textureHeight;

    public ImageButton(ResourceLocation image, int xPosition, int yPosition, int width, int height, int textureWidth, int textureHeight) {
        super(xPosition, yPosition, width, height);
        this.image = image;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void draw() {
        Helper.renderWithColor(Colors.rgba(isEnabled() ? Colors.WHITE : Colors.GRAY), () -> {
            Minecraft.getMinecraft().getTextureManager().bindTexture(image);
            drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, width, height, textureWidth, textureHeight);
        });
    }
}
