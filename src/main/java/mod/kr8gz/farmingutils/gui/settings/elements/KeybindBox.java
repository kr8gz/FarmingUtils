package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;
import test.kr8gz.settings.types.KeybindSetting;

import java.util.function.Supplier;

public class KeybindBox extends ModInteractableGuiElement {
    KeybindSetting boundSetting;
    boolean selected = false;
    boolean badInput = false;

    public KeybindBox(KeybindSetting boundSetting, int x, int y, int width, int height, Supplier<Boolean> enabledCondition) {
        super(x, y, width, height, enabledCondition);
        this.boundSetting = boundSetting;
    }

    @Override
    public void draw() {
        int color;
        enabled = enabledCondition.get();
        if (enabled) {
            if (selected) {
                if (badInput) {
                    color = Colors.rgba(Colors.RED);
                } else {
                    color = Colors.rgba(Colors.WHITE);
                }
            } else {
                if (badInput) {
                    color = Colors.rgba(Colors.DARKRED);
                } else {
                    color = Colors.rgba(Colors.GRAY);
                }
            }
        } else {
            color = Colors.rgba(Colors.GRAY);
        }
        drawRect(xPosition, yPosition, xPosition + width, yPosition + height, color);
        drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, Colors.rgba(Colors.BLACK, selected ? 2/3d : 1d));

        String text = Keyboard.getKeyName(boundSetting.get());
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        float scale = Math.min((height - 8) / (float) fr.FONT_HEIGHT, (width - 8) / (float) fr.getStringWidth(text));
        Helper.glSetScale(scale);
        fr.drawStringWithShadow(text, (xPosition + width / 2f) / scale - fr.getStringWidth(text) / 2f, (yPosition + height / 2f) / scale - 4, enabled ? Colors.rgba(Colors.WHITE) : Colors.rgba(Colors.GRAY));
        Helper.glResetScale();
    }

    @Override
    public boolean keyTyped(char character, int key) {
        if (selected && enabled) {
            if (key == Keyboard.KEY_ESCAPE) {
                key = Keyboard.KEY_NONE;
            }
            badInput = !boundSetting.set(key);
            selected = false;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased() {
        if (enabled) {
            selected = true;
        }
    }

    @Override
    public void mousePressedGlobal() {
        selected = false;
    }
}
