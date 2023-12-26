package mod.kr8gz.farmingutils.screens;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.FixedTextLabel;
import mod.kr8gz.farmingutils.gui.TextButton;
import mod.kr8gz.farmingutils.util.Colors;
import test.kr8gz.settings.Settings;

public class GuiResetAllConfirmation extends ModGuiScreen {
    public GuiResetAllConfirmation(ModGuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        elementList.add(new FixedTextLabel("Do you really want to reset all your settings?", width / 2, height / 2 - 37, 2f));
        elementList.add(new FixedTextLabel("This action is irreversible.", width / 2, height / 2 - 17, 2f));

        elementList.add(new TextButton(width / 4, height / 2 + 2, width / 4 - 4, 32, "Cancel", 1.3f, Colors.WHITE) {
            @Override
            protected void action() {
                mc.displayGuiScreen(parentScreen);
            }
        });

        elementList.add(new TextButton(width / 2 + 4, height / 2 + 2, width / 4 - 4, 32, "Continue", 1.3f, Colors.RED) {
            @Override
            protected void action() {
                ConfigManager.settings.settingsList.forEach(Settings.AbstractSetting::reset);
                elementList.stream()
                        .filter(e -> e instanceof BoundToSetting)
                        .forEach(e -> ((BoundToSetting) e).updateFromSetting());

                mc.displayGuiScreen(parentScreen);
            }
        });
    }
}