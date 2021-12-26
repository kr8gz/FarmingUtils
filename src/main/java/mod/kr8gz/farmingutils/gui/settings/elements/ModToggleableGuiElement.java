package mod.kr8gz.farmingutils.gui.settings.elements;

import java.util.function.Supplier;

public abstract class ModToggleableGuiElement extends ModGuiElement {
    boolean enabled;
    Supplier<Boolean> enabledCondition;

    public ModToggleableGuiElement(int xPosition, int yPosition, int width, int height, Supplier<Boolean> enabledCondition) {
        super(xPosition, yPosition, width, height);
        this.enabledCondition = enabledCondition;
    }
}
