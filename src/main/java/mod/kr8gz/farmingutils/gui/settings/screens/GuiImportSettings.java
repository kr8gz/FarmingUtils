package mod.kr8gz.farmingutils.gui.settings.screens;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.settings.elements.Button;
import mod.kr8gz.farmingutils.gui.settings.elements.FixedTextLabel;
import mod.kr8gz.farmingutils.util.Colors;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GuiImportSettings extends ModGuiScreen {
    public GuiImportSettings(ModGuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        elementList.add(new FixedTextLabel(this, "Import Settings", width / 2, height / 2 - 17, 2f));

        elementList.add(new Button(this, width / 4, height / 2 + 2, width / 4 - 4, 32, "Open Folder", 1.3f, Colors.YELLOW2) {
            @Override
            protected void action() {
                try {
                    Desktop.getDesktop().open(new File(FarmingUtils.MODID + "/config"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        elementList.add(new Button(this, width / 2 + 4, height / 2 + 2, width / 4 - 4, 32, "Paste into File", 1.3f, Colors.LIGHTBLUE) {
            @Override
            protected void action() {
                try {
                    Desktop.getDesktop().open(new File(FarmingUtils.MODID + "/config/settings.txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        elementList.add(new Button(this, width / 4, height / 2 + 42, width / 2, 32, "Done", 1.3f, Colors.GREEN2) {
            @Override
            protected void action() {
                ConfigManager.settings.init();
                Minecraft.getMinecraft().displayGuiScreen(parentScreen);
            }
        });
    }
}
