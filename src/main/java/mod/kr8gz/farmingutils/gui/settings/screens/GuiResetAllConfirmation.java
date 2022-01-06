package mod.kr8gz.farmingutils.gui.settings.screens;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.settings.elements.FixedTextLabel;
import mod.kr8gz.farmingutils.gui.settings.elements.ModGuiElement;
import mod.kr8gz.farmingutils.gui.settings.elements.TextButton;
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
                for (Settings.AbstractSetting<?> setting : ConfigManager.settings.settingsList) {
                    setting.reset();
                }
                for (ModGuiElement e : elementList) {
                    e.updateStateFromBoundSetting();
                }
                mc.displayGuiScreen(parentScreen);
            }
        });
    }
}