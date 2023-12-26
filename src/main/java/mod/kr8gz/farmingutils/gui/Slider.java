package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.screens.ModGuiScreen;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import test.kr8gz.settings.NumberSetting;

import java.util.function.Supplier;

public abstract class Slider<S extends NumberSetting<T>, T> extends ModGuiElement implements ModGuiElement.Toggleable, ModGuiElement.BoundToSetting {
    static final ResourceLocation sliderTexture = new ResourceLocation(FarmingUtils.MODID, "textures/gui/slider.png");

    float sliderPos;
    boolean mouseDown;

    T value;
    final S boundSetting;
    final SliderTextBox<S, T> sliderTextBox;

    final Supplier<Boolean> enabledCondition;

    public Slider(ModGuiScreen screen, S boundSetting, int x, int y, Supplier<Boolean> enabledCondition) {
        super(x, y, 100, 32);

        this.boundSetting = boundSetting;
        updateFromSetting();

        screen.elementList.add(this.sliderTextBox = new SliderTextBox<>(
                this,
                x - 62, y + 6,
                54, 20
        ));

        this.enabledCondition = enabledCondition;
    }

    @Override
    public boolean isEnabled() {
        return enabledCondition.get();
    }

    /**
     * example implementation
     * <pre> {@code
     * @Override
     * void updateSliderPos(T value) {
     *     this.sliderPos = (float) (value - min) / (max - min);
     * }
     *
     * @Override
     * void updateValue(float sliderPos) {
     *     this.value = min + (max - min) * sliderPos;
     * }
     * } </pre>
     * bottom text
     **/
    abstract void updateSliderPos(T value);
    abstract void updateValue(float sliderPos);

    @Override
    public void updateFromSetting() {
        this.value = boundSetting.get();
        updateSliderPos(this.value);
    }

    @Override
    public void draw() {
        Helper.renderWithColor(Colors.rgba(isEnabled() ? Colors.WHITE : Colors.GRAY), () -> {
            Minecraft.getMinecraft().getTextureManager().bindTexture(sliderTexture);
            drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, width, height, 108, 32);
        });
        drawModalRectWithCustomSizedTexture(xPosition - 4 + Helper.round(sliderPos * 100), yPosition, 100, 0, 8, 32, 108, 32);
    }

    @Override
    public void mousePressed(int mouseX, int mouseY) {
        mouseDown = true;
    }

    @Override
    public void mouseReleased() {
        mouseDown = false;
    }

    @Override
    public void mouseMovedGlobal(int mouseX, int mouseY) {
        if (isEnabled() && mouseDown) {
            sliderPos = MathHelper.clamp_float((mouseX - xPosition) / 100f, 0f, 1f);
            updateValue(sliderPos);
            sliderTextBox.value = value.toString();
            boundSetting.set(value);
        }
    }
}
