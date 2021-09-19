package mod.kr8gz.farmingutils.gui.overlay;

import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.util.Colors;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayHandler {
    private static final OverlaySection
            bpsOverlay          = new BPSOverlay(-4, 4, Colors.LIGHTBLUE),
            jacobsHelperOverlay = new JacobsHelperOverlay(-4, -4, Colors.YELLOW);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Text event) {
        bpsOverlay.draw();
        jacobsHelperOverlay.draw();
        AngleHelperOverlay.draw();
    }
}
