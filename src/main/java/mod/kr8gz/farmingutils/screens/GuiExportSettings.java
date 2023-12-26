package mod.kr8gz.farmingutils.screens;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.gui.FixedTextLabel;
import mod.kr8gz.farmingutils.gui.TextButton;
import mod.kr8gz.farmingutils.util.Colors;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GuiExportSettings extends ModGuiScreen {
    public GuiExportSettings(ModGuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        elementList.add(new FixedTextLabel("Export Settings", width / 2, height / 2 - 17, 2f));

        elementList.add(new TextButton(width / 4, height / 2 + 2, width / 4 - 4, 32, "Show File", 1.3f, Colors.DARKYELLOW) {
            @Override
            protected void action() {
                try {
                    Desktop.getDesktop().open(new File(FarmingUtils.MODID + "/config"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        elementList.add(new TextButton(width / 2 + 4, height / 2 + 2, width / 4 - 4, 32, "Copy to Clipboard", 1.3f, Colors.LIGHTBLUE) {
            @Override
            protected void action() {
                try {
                    List<String> lines = Files.readAllLines(Paths.get(FarmingUtils.MODID + "/config/settings.txt"));
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(String.join("\n", lines)), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        elementList.add(new TextButton(width / 4, height / 2 + 42, width / 2, 32, "Done", 1.3f, Colors.LIGHTGREEN) {
            @Override
            protected void action() {
                mc.displayGuiScreen(parentScreen);
            }
        });
    }
}
