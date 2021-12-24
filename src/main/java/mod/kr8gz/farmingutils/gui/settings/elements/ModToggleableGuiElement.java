package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.gui.settings.screens.ModGuiScreen;

import java.util.function.Supplier;

public abstract class ModToggleableGuiElement extends ModGuiElement {
    boolean enabled;
    Supplier<Boolean> enabledCondition;

    public ModToggleableGuiElement(ModGuiScreen screen, int xPosition, int yPosition, int width, int height, Supplier<Boolean> enabledCondition) {
        super(screen, xPosition, yPosition, width, height);
        this.enabledCondition = enabledCondition;
    }
}
