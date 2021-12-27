package mod.kr8gz.farmingutils.gui.overlay;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.data.JacobsHelperData;
import mod.kr8gz.farmingutils.data.MedalData;
import mod.kr8gz.farmingutils.data.MedalTier;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlayElement;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import mod.kr8gz.farmingutils.util.ScoreboardHelper;
import net.minecraft.client.multiplayer.ServerData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JacobsHelperOverlay extends OverlaySection {
    private MedalTier lastAlertMedal = MedalTier.NONE;
    private int alertShownSince = -1;
    private int lastCropCount;
    private final JacobsHelperData bronzeData = new JacobsHelperData(MedalTier.BRONZE, isPreviewMode());
    private final JacobsHelperData silverData = new JacobsHelperData(MedalTier.SILVER, isPreviewMode());
    private final JacobsHelperData goldData = new JacobsHelperData(MedalTier.GOLD, isPreviewMode());

    public JacobsHelperOverlay() {
        super(ConfigManager.jacobPosX, ConfigManager.jacobPosY, "Jacob's Helper", Colors.YELLOW);
    }

    private static void drawAlert(String string) {
        Helper.glSetScale(4);
        mc.fontRendererObj.drawStringWithShadow(string, (getScreenWidth() - mc.fontRendererObj.getStringWidth(string) * 4) / 8f, (getScreenHeight() - mc.fontRendererObj.FONT_HEIGHT * 4) / 8f, Colors.WHITE);
        Helper.glResetScale();
    }

    @Override
    protected boolean shouldRender() {
        ServerData serverData = mc.getCurrentServerData();
        return super.shouldRender() && ConfigManager.showJacobsHelper.get() && serverData != null && serverData.serverIP.equals("hypixel.net") && ScoreboardHelper.stringList().contains("Jacob's Contest");
    }

    @Override
    protected List<OverlayElement> getElementList() {
        List<OverlayElement> list = new ArrayList<>();
        List<List<String>> strings = new ArrayList<>();
        for (JacobsHelperData data : new JacobsHelperData[] {goldData, silverData, bronzeData}) {
            MedalData after20Minutes = data.getMedalDataAfter20Minutes();
            MedalData currentMedalData = data.getCurrentMedalData();
            String color = data.stateColor();

            if (currentMedalData != null) {
                // base jacob helper overlay
                strings.add(Arrays.asList(
                        after20Minutes.medal.formatColor(color),
                        color + "in 20 minutes",
                        color + (after20Minutes.crops == -1 ? "-" : Helper.formatInt(after20Minutes.crops))
                ));

                // extra alert-related overlays
                if (ConfigManager.jacobsHelperAlert.get()) {
                    int cropsRequiredForAlert = Helper.round(after20Minutes.crops * (100 + ConfigManager.alertExtraPercent.get()) / 100f);
                    int cropsUntilAlert = cropsRequiredForAlert - currentMedalData.crops;
                    boolean inv = false;
                    if (cropsUntilAlert < 0 && after20Minutes.crops != -1) {
                        inv = true;
                        cropsUntilAlert = -cropsUntilAlert;
                    }

                    if (ConfigManager.showCropsUntilAlert.get()) {
                        strings.add(Arrays.asList(
                                "",
                                color + (inv ? "Crops ahead" : "Crops required"),
                                color + (after20Minutes.crops == -1 ? "-" : (inv ? "+" : "") + Helper.formatInt(cropsUntilAlert))
                        ));
                    }
                    if (ConfigManager.showTimeUntilAlert.get()) {
                        int timeUntilAlert = Helper.round((double) cropsUntilAlert / (double) currentMedalData.crops * (double) data.getElapsedTime());
                        strings.add(Arrays.asList(
                                "",
                                color + (inv ? "Time ahead" : "Time until alert"),
                                color + (after20Minutes.crops == -1 ? "-" : (inv ? "+" : "") + Helper.formatTime(timeUntilAlert))
                        ));
                    }

                    if (currentMedalData.crops < lastCropCount) {
                        lastAlertMedal = MedalTier.NONE;
                    }
                    if (cropsRequiredForAlert > 0 && currentMedalData.crops >= cropsRequiredForAlert && !isPreviewMode()) {
                        if (lastAlertMedal.ordinal() < data.medal.ordinal()) {
                            lastAlertMedal = data.medal;
                            alertShownSince = getCurrentTick();
                            Helper.playClientSound("random.orb");
                        }
                        if (getCurrentTick() - alertShownSince < 60 && lastAlertMedal == data.medal) {
                            drawAlert(after20Minutes.medal.formatBold() + " secured!");
                        }
                    }
                    lastCropCount = currentMedalData.crops;
                }
            }
        }
        list.add(new OverlayElement(strings));
        return list;
    }
}
