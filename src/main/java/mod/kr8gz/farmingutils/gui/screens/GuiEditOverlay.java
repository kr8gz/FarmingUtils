package mod.kr8gz.farmingutils.gui.screens;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.elements.ModGuiElement;
import mod.kr8gz.farmingutils.overlay.BPSOverlay;
import mod.kr8gz.farmingutils.overlay.BreakingHelperOverlay;
import mod.kr8gz.farmingutils.overlay.JacobsHelperOverlay;
import mod.kr8gz.farmingutils.overlay.elements.OverlayElement;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiEditOverlay extends ModGuiScreen {
    public GuiEditOverlay(ModGuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        elementList.add(new BPSOverlay() {
            @Override
            protected boolean shouldRender() {
                return true;
            }

            @Override
            public float getBPS(int ticks) {
                return ticks == 20 ? 12 : Helper.round(12.34567f, ConfigManager.roudingPrecision.get());
            }
        });

        elementList.add(new JacobsHelperOverlay() {
            @Override
            protected boolean shouldRender() {
                return true;
            }

            // TODO find a better way to do this shit i just want a working version first
            // best if merged with actual JacobsHelperOverlay::getElementList logic
            @Override
            protected List<OverlayElement> getElementList() {
                List<OverlayElement> list = new ArrayList<>();
                List<List<String>> strings = new ArrayList<>();

                int elapsedSeconds = 15 * 60;
                int currentCropsPerSecond = 700;
                int currentCrops = elapsedSeconds * currentCropsPerSecond;

                int diamondCropsAfterContest = 700000;
                int platinumCropsAfterContest = 600000;
                int goldCropsAfterContest = 500000;

                int diamondCropDifference = Math.abs(diamondCropsAfterContest - currentCrops);
                int platinumCropDifference = Math.abs(platinumCropsAfterContest - currentCrops);
                int goldCropDifference = Math.abs(goldCropsAfterContest - currentCrops);

                int diamondTimeDifference = Helper.round((double) diamondCropDifference / currentCropsPerSecond);
                int platinumTimeDifference = Helper.round((double) platinumCropDifference / currentCropsPerSecond);
                int goldTimeDifference = Helper.round((double) goldCropDifference / currentCropsPerSecond);

                String color = DataState.ACTIVE.color;
                addPreviewData(strings,
                        Arrays.asList(MedalTier.DIAMOND.formatNameWithColor(color), color + "in 20 minutes", color + Helper.formatInt(diamondCropsAfterContest)),
                        Arrays.asList("", color + "Crops required", color + Helper.formatInt(diamondCropDifference)),
                        Arrays.asList("", color + "Time until alert", color + Helper.formatTime(diamondTimeDifference))
                );

                addPreviewData(strings,
                        Arrays.asList(MedalTier.PLATINUM.formatNameWithColor(color), color + "in 20 minutes", color + Helper.formatInt(platinumCropsAfterContest)),
                        Arrays.asList("", color + "Crops ahead", color + "+" + Helper.formatInt(platinumCropDifference)),
                        Arrays.asList("", color + "Time ahead", color + "+" + Helper.formatTime(platinumTimeDifference))
                );

                color = DataState.OLD.color;
                addPreviewData(strings,
                        Arrays.asList(MedalTier.GOLD.formatNameWithColor(color), color + "in 20 minutes", color + Helper.formatInt(goldCropsAfterContest)),
                        Arrays.asList("", color + "Crops ahead", color + "+" + Helper.formatInt(goldCropDifference)),
                        Arrays.asList("", color + "Time ahead", color + "+" + Helper.formatTime(goldTimeDifference))
                );

                list.add(new OverlayElement(strings));
                return list;
            }

            private void addPreviewData(List<List<String>> strings, List<String> in20Minutes, List<String> cropsUntilAlert, List<String> timeUntilAlert) {
                strings.add(in20Minutes);
                if (ConfigManager.showCropsUntilAlert.get()) strings.add(cropsUntilAlert);
                if (ConfigManager.showTimeUntilAlert.get()) strings.add(timeUntilAlert);
            }
        });

        elementList.add(new BreakingHelperOverlay() {
            @Override
            protected boolean shouldRender() {
                return true;
            }
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (ModGuiElement e : elementList) e.draw();
        float scale = 1.3f;
        Helper.renderWithScale(scale, () -> drawCenteredString(mc.fontRendererObj, "Press Esc to finish", Helper.round(width / (2 * scale)), Helper.round((height - 36) / scale), Colors.WHITE));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ConfigManager.settings.load();
    }
}
