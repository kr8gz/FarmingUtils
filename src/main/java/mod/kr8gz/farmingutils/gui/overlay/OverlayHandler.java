package mod.kr8gz.farmingutils.gui.overlay;

import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.gui.settings.screens.GuiEditOverlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayHandler {
    private static final OverlaySection
            BPSOverlay = new BPSOverlay(),
            jacobsHelperOverlay = new JacobsHelperOverlay();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Text event) {
        AngleHelperOverlay.draw();

        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiEditOverlay)) {
            // overlays with editable position
            BPSOverlay.draw();
            jacobsHelperOverlay.draw();
        }
    }
}
