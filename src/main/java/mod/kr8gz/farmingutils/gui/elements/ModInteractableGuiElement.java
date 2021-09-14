package mod.kr8gz.farmingutils.gui.elements;

import java.util.function.Supplier;

public abstract class ModInteractableGuiElement extends ModGuiElement {
    boolean enabled;
    Supplier<Boolean> enabledCondition;

    public ModInteractableGuiElement(int xPosition, int yPosition, int width, int height, Supplier<Boolean> enabledCondition) {
        super(xPosition, yPosition, width, height);
        this.enabledCondition = enabledCondition;
    }
}
