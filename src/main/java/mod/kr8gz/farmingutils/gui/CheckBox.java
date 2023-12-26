package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import test.kr8gz.settings.BooleanSetting;

public abstract class CheckBox extends ModGuiElement implements ModGuiElement.Toggleable, ModGuiElement.BoundToSetting {
    static final ResourceLocation checkboxTexture = new ResourceLocation(FarmingUtils.MODID, "textures/gui/checkbox.png");

    boolean state;
    final BooleanSetting boundSetting;

    public CheckBox(BooleanSetting boundSetting, int x, int y) {
        super(x, y, 32, 32);
        this.boundSetting = boundSetting;
        updateFromSetting();
    }

    @Override
    public void updateFromSetting() {
        this.state = boundSetting.get();
    }

    @Override
    public void draw() {
        Helper.renderWithColor(Colors.rgba(isEnabled() ? Colors.WHITE : Colors.GRAY), () -> {
            Minecraft.getMinecraft().getTextureManager().bindTexture(checkboxTexture);
            drawModalRectWithCustomSizedTexture(xPosition, yPosition, state ? 32 : 0, 0, width, height, 64, 32);
        });
    }

    @Override
    public void mouseReleased() {
        if (isEnabled() && boundSetting.set(!boundSetting.get())) {
            state = !state;
        }
    }
}
