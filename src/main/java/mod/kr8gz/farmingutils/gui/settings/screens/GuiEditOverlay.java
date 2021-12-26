package mod.kr8gz.farmingutils.gui.settings.screens;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.overlay.BPSOverlay;
import mod.kr8gz.farmingutils.gui.overlay.JacobsHelperOverlay;
import mod.kr8gz.farmingutils.gui.settings.elements.ModGuiElement;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.Minecraft;

public class GuiEditOverlay extends ModGuiScreen {
    public GuiEditOverlay(ModGuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        elementList.add(new BPSOverlay() {
            @Override
            protected boolean isPreviewMode() {
                return true;
            }
        });

        elementList.add(new JacobsHelperOverlay() {
            @Override
            protected boolean isPreviewMode() {
                return true;
            }
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (ModGuiElement e : elementList) e.draw();
        Helper.glSetScale(1.3f);
        drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Press Esc to finish", (int) (width / 2.6f), (int) ((height - 36) / 1.3f), Colors.WHITE);
        Helper.glResetScale();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ConfigManager.settings.init();
    }
}