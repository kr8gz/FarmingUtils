package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import test.kr8gz.settings.types.NumberSetting;

import java.util.function.Supplier;

public abstract class Slider<S extends NumberSetting<T>, T> extends ModToggleableGuiElement {
    static final ResourceLocation sliderTexture = new ResourceLocation(FarmingUtils.MODID, "textures/gui/slider.png");

    float sliderPos;
    boolean mouseDown;

    T value;
    public final S boundSetting;
    SliderTextBox<S, T> sliderTextbox;

    public Slider(ModGuiScreen screen, S boundSetting, int x, int y) {
        this(screen, boundSetting, x, y, () -> true);
    }

    public Slider(ModGuiScreen screen, S boundSetting, int x, int y, Supplier<Boolean> enabledCondition) {
        super(screen, x, y, 100, 32, enabledCondition);
        this.value = boundSetting.get();
        this.boundSetting = boundSetting;
        this.sliderTextbox = new SliderTextBox<>(
                screen, this,
                x - 62, y + 6,
                54, 20,
                enabledCondition
        );
        screen.elementList.add(this.sliderTextbox);
        updateSliderPos(this.value);
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
    public void updateStateFromBoundSetting() {
        this.value = boundSetting.get();
        updateSliderPos(this.value);
    }

    @Override
    public void draw() {
        this.updateStateFromBoundSetting();
        enabled = enabledCondition.get();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        if (!enabled) GlStateManager.color(0.6f, 0.6f, 0.6f, 0.6f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(sliderTexture);
        drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, width, height, 108, 32);
        if (!enabled) GlStateManager.color(1f, 1f, 1f, 1f);
        drawModalRectWithCustomSizedTexture(xPosition - 4 + (int) (sliderPos * 100), yPosition, 100, 0, 8, 32, 108, 32);
    }

    @Override
    public void mousePressed() {
        mouseDown = true;
    }

    @Override
    public void mouseReleased() {
        mouseDown = false;
    }

    @Override
    public void mouseMovedGlobal(int mouseX, int mouseY) {
        if (enabled && mouseDown) {
            sliderPos = MathHelper.clamp_float((mouseX - xPosition) / 100f, 0f, 1f);
            updateValue(sliderPos);
            sliderTextbox.value = value.toString();
            sliderTextbox.badInput = false;
            boundSetting.set(value);
        }
    }
}
