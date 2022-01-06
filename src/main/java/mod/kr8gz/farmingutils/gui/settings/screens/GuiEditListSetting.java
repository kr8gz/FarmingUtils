package mod.kr8gz.farmingutils.gui.settings.screens;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.gui.settings.elements.*;
import mod.kr8gz.farmingutils.util.Colors;
import net.minecraft.util.ResourceLocation;
import test.kr8gz.settings.types.ListSetting;

import java.util.List;

public class GuiEditListSetting<T> extends ModGuiScreen {
    static final ResourceLocation removeTexture = new ResourceLocation(FarmingUtils.MODID, "textures/gui/remove.png");
    static final ResourceLocation addTexture = new ResourceLocation(FarmingUtils.MODID, "textures/gui/add.png");

    public final ListSetting<T> boundListSetting;
    int rows;
    int listYOffset;

    public GuiEditListSetting(ModGuiScreen parentScreen, ListSetting<T> boundListSetting) {
        super(parentScreen);
        this.boundListSetting = boundListSetting;
    }

    class EditListTextBox extends TextBox {
        int row;

        public EditListTextBox(int xPosition, int yPosition, int width, int height, int row) {
            super(xPosition, yPosition, width, height);
            this.row = row;
            this.updateStateFromBoundSetting();
        }

        @Override
        public void updateStateFromBoundSetting() {
            if (rows > boundListSetting.get().size()) {
                boundListSetting.add(row, boundListSetting.defaultValues.get(0));
            }
            this.value = boundListSetting.getAsString(row);
        }

        @Override
        protected void checkBadInput() {
            row = elementList.indexOf(this) / 3;

            badInput = false;
            try {
                if (!boundListSetting.set(row, boundListSetting.parseSingleElementFromString(value))) {
                    badInput = true;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        }
    }

    private void removeRow(int row, boolean reset) {
        if (rows - 1 >= boundListSetting.minValues) {
            try {
                for (int i = 0; i < 3; i++) {
                    nextElements.remove(row * 3);
                }
                rows--;
                if (!reset) {
                    boundListSetting.remove(row);
                }
                updateRowPositions(nextElements);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void addRow(int row, boolean init) {
        if (rows + 1 <= boundListSetting.maxValues) {
            rows++;
            List<ModGuiElement> list = init ? elementList : nextElements;

            list.add(row * 3, new EditListTextBox(width / 4, listYOffset + row * 40, width / 2 - 80, 32, row));
            list.add(row * 3, new ImageButton(removeTexture, width * 3 / 4 - 72, listYOffset + row * 40, 32, 32, 32, 32,
                    () -> rows - 1 >= boundListSetting.minValues) {
                @Override
                protected void action() {
                    removeRow(elementList.indexOf(this) / 3, false);
                }
            });
            list.add(row * 3, new ImageButton(addTexture, width * 3 / 4 - 32, listYOffset + row * 40, 32, 32, 32, 32,
                    () -> rows + 1 <= boundListSetting.maxValues) {
                @Override
                protected void action() {
                    addRow(elementList.indexOf(this) / 3 + 1, false);
                }
            });

            updateRowPositions(list);
        }
    }

    private void updateRowPositions(List<ModGuiElement> list) {
        int i = 0;
        for (ModGuiElement e : list) {
            if (!(e instanceof TextLabel)) {
                e.yPosition = listYOffset + i / 3 * 40 + amountScrolled;
                i++;
            }
        }
        maxScrollHeight = height * 7 / 8 - listYOffset - rows * 40 - 32;
        updateAmountScrolled(amountScrolled); // keep same scroll pos but clamp
    }

    @Override
    public void initGui() {
        super.initGui();

        TextLabel name;
        TextLabel desc;
        elementList.add(name = new TextLabel(boundListSetting.key, width / 4, height / 8, 2f, width / 2));
        elementList.add(desc = new TextLabel(boundListSetting.description, width / 4, height / 8 + name.height + 3, 1.3f, width / 2, Colors.GRAY));

        listYOffset = height / 8 + name.height + desc.height + 11;
        rows = 0;
        int r = boundListSetting.get().size();
        for (int row = 0; row < r; row++) {
            addRow(row, true);
        }

        elementList.add(new TextButton(width / 4, listYOffset + rows * 40, width / 4 - 4, 32, "Reset", 1.3f, Colors.RED2) {
            @Override
            protected void action() {
                boundListSetting.reset();
                int r = rows;
                int remove = -1;
                int size = boundListSetting.get().size();
                for (int row = 0; row < r; row++) {
                    if (row >= size) {
                        if (remove == -1) {
                            remove = row;
                        }
                        removeRow(remove, true);
                    }
                }
                if (r < size) {
                    for (int row = r; row < size; row++) {
                        addRow(row, false);
                    }
                }
                for (ModGuiElement e : nextElements) {
                    e.updateStateFromBoundSetting();
                }
            }
        });

        elementList.add(new TextButton(width / 2 + 4, listYOffset + rows * 40, width / 4 - 4, 32, "Done", 1.3f, Colors.GREEN2) {
            @Override
            protected void action() {
                mc.displayGuiScreen(parentScreen);
            }
        });

        maxScrollHeight = height * 7 / 8 - listYOffset - rows * 40 - 32;
    }
}
