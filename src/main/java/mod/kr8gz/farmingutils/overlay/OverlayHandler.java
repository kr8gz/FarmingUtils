package mod.kr8gz.farmingutils.overlay;

import mod.kr8gz.farmingutils.gui.screens.GuiEditOverlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayHandler {
    public static final BPSOverlay BPSOverlay = new BPSOverlay();
    public static final JacobsHelperOverlay jacobsHelperOverlay = new JacobsHelperOverlay();
    public static final BreakingHelperOverlay breakingHelperOverlay = new BreakingHelperOverlay();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Text event) {
        AngleHelperOverlay.draw();

        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiEditOverlay)) {
            // overlays with editable position
            BPSOverlay.draw();
            jacobsHelperOverlay.draw();
            breakingHelperOverlay.draw();
        }
    }
}
