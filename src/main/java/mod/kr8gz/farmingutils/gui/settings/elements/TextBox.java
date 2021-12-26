package mod.kr8gz.farmingutils.gui.settings.elements;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.function.Supplier;

public class TextBox extends ModToggleableGuiElement {
    int cursorPos = 0;
    boolean selected = false;
    boolean badInput = false;
    public String value;

    public TextBox(int xPosition, int yPosition, int width, int height) {
        this(xPosition, yPosition, width, height, () -> true);
    }

    public TextBox(int xPosition, int yPosition, int width, int height, Supplier<Boolean> enabledCondition) {
        super(xPosition, yPosition, width, height, enabledCondition);
    }

    protected void checkBadInput() {
    }

    float getTextScale() {
        return (float) (height - 8) / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public void draw() {
        enabled = enabledCondition.get();
        checkBadInput();

        int color;

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
        drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, Colors.rgba(Colors.BLACK));

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        float scale = getTextScale();
        Helper.glSetScale(scale);
        fr.drawStringWithShadow(value, (int) (xPosition / scale) + 4, (int) (yPosition / scale) + 4, enabled ? Colors.rgba(Colors.WHITE) : Colors.rgba(Colors.GRAY));
        if (selected) {
            int pos = fr.getStringWidth(value.substring(0, cursorPos));
            drawRect(
                    (int) (xPosition / scale) + 3 + pos,
                    (int) (yPosition / scale) + 3,
                    (int) (xPosition / scale) + 4 + pos,
                    (int) (yPosition / scale) + 3 + fr.FONT_HEIGHT,
                    Colors.rgba(Colors.WHITE)
            );
        }
        Helper.glResetScale();
    }

    void moveCursor(int delta) {
        cursorPos += delta;
        cursorPos = MathHelper.clamp_int(cursorPos, 0, value.length());
    }

    void moveCursorToEnd() {
        cursorPos = value.length();
    }

    @Override
    public boolean keyTyped(char character, int key) {
        if (selected && enabled) {
            if (key == Keyboard.KEY_BACK) {
                if (cursorPos != 0) {
                    value = value.substring(0, cursorPos - 1) + value.substring(cursorPos);
                    moveCursor(-1);
                }
            } else if (key == Keyboard.KEY_DELETE) {
                if (cursorPos != value.length()) {
                    value = value.substring(0, cursorPos) + value.substring(cursorPos + 1);
                }
            } else if (key == Keyboard.KEY_LEFT) {
                moveCursor(-1);
            } else if (key == Keyboard.KEY_RIGHT) {
                moveCursor(1);
            } else if (key == Keyboard.KEY_ESCAPE) {
                selected = false;
            } else if (character >= 32) {
                FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
                if (fr.getStringWidth(value + character) * getTextScale() <= width - 8) {
                    value = value.substring(0, cursorPos) + character + value.substring(cursorPos);
                    moveCursor(1);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased() {
        if (enabled) {
            selected = true;
            moveCursorToEnd();
        }
    }

    @Override
    public void mousePressedGlobal() {
        selected = false;
    }
}
