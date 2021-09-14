package mod.kr8gz.farmingutils.gui.elements;

import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import test.kr8gz.settings.types.Setting;

import java.util.function.Supplier;

public class TextBox extends ModInteractableGuiElement {
    public static final int
            RED     = -0x10000,
            DARKRED = -0x670000,
            WHITE   = -1,
            GRAY    = -0x666667,
            BLACK   = -0x1000000;

    int cursorPos = 0;
    boolean selected = false;
    boolean badInput = false;

    public String value = "";
    public Setting<?> boundSetting;

    public TextBox(Setting<?> boundSetting, int x, int y, int width, int height, Supplier<Boolean> enabledCondition) {
        super(x, y, width, height, enabledCondition);
    }

    public TextBox(Setting<?> boundSetting, int x, int y, int width, int height, String value, Supplier<Boolean> enabledCondition) {
        super(x, y, width, height, enabledCondition);
        this.value = value;
        this.boundSetting = boundSetting;
    }

    @Override
    public void draw() {
        int color;
        enabled = enabledCondition.get();
        if (enabled) {
            if (selected) {
                if (badInput) {
                    color = RED;
                } else {
                    color = WHITE;
                }
            } else {
                if (badInput) {
                    color = DARKRED;
                } else {
                    color = GRAY;
                }
            }
        } else {
            color = GRAY;
        }
        Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, color);
        Gui.drawRect(xPosition + 1, yPosition + 1, xPosition + width - 1, yPosition + height - 1, BLACK);

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        float scale = (float) (height - 8) / fr.FONT_HEIGHT;
        Helper.glSetScale(scale);
        fr.drawStringWithShadow(value, (int) (xPosition / scale) + 4, (int) (yPosition / scale) + 4, enabled ? WHITE : GRAY);
        if (selected) {
            int pos = fr.getStringWidth(value.substring(0, cursorPos));
            Gui.drawRect(
                    (int) (xPosition / scale) + 3 + pos,
                    (int) (yPosition / scale) + 3,
                    (int) (xPosition / scale) + 4 + pos,
                    (int) (yPosition / scale) + 3 + fr.FONT_HEIGHT,
                    WHITE
            );
        }
        Helper.glResetScale();
    }

    private void moveCursor(int delta) {
        cursorPos += delta;
        cursorPos = MathHelper.clamp_int(cursorPos, 0, value.length());
    }

    private void moveCursorToEnd() {
        cursorPos = value.length();
    }

    @Override
    public void keyTyped(char character, int key) {
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
            } else if (character >= 32) {
                value = value.substring(0, cursorPos) + character + value.substring(cursorPos);
                moveCursor(1);
            }
        }
        badInput = false;
        try {
            if (!boundSetting.setFromString(value)) {
                badInput = true;
            }
        } catch (NumberFormatException e) {
            badInput = true;
        }
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
