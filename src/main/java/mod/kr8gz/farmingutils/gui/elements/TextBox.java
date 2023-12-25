package mod.kr8gz.farmingutils.gui.elements;

import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public abstract class TextBox extends ModGuiElement implements ModGuiElement.Toggleable {
    int cursorPos = 0;
    boolean selected = false;

    public String value = "";

    public TextBox(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    protected boolean checkValidInput() {
        return true;
    }

    float getTextScale() {
        return height / 15f;
    }

    @Override
    public void draw() {
        boolean enabled = isEnabled();
        boolean isValidInput = checkValidInput();

        int boxColor;
        if (enabled && selected && !isValidInput) {
            boxColor = Colors.rgba(Colors.RED);
        } else if (enabled && selected) {
            boxColor = Colors.rgba(Colors.WHITE);
        } else if (enabled && !isValidInput) {
            boxColor = Colors.rgba(Colors.DARKRED);
        } else {
            boxColor = Colors.rgba(Colors.GRAY);
        }

        drawRect(xPosition, yPosition, xPosition + width, yPosition + height, boxColor);
        drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, Colors.rgba(Colors.BLACK));

        FontRenderer fr = getFontRenderer();
        float scale = getTextScale();
        Helper.renderWithScale(scale, () -> {
            int textColor = enabled ? Colors.rgba(Colors.WHITE) : Colors.rgba(Colors.GRAY);
            fr.drawStringWithShadow(value, Helper.round(xPosition / scale) + 4, Helper.round(yPosition / scale) + 4, textColor);

            // cursor
            if (selected) {
                int pos = fr.getStringWidth(value.substring(0, cursorPos));
                drawRect(
                        Helper.round(xPosition / scale) + 3 + pos,
                        Helper.round(yPosition / scale) + 3,
                        Helper.round(xPosition / scale) + 4 + pos,
                        Helper.round(yPosition / scale) + 3 + fr.FONT_HEIGHT,
                        Colors.rgba(Colors.WHITE)
                );
            }
        });
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
        if (!selected) return true;

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
            if (getFontRenderer().getStringWidth(value + character) * getTextScale() <= width - 8) {
                value = value.substring(0, cursorPos) + character + value.substring(cursorPos);
                moveCursor(1);
            }
        }

        return false;
    }

    @Override
    public void mouseReleased() {
        if (isEnabled()) {
            selected = true;
            moveCursorToEnd();
        }
    }

    @Override
    public void mousePressedGlobal() {
        selected = false;
    }
}
