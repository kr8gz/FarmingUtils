package mod.kr8gz.farmingutils.gui.overlay;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.data.MedalData;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlayElement;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlayList;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlayTable;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import mod.kr8gz.farmingutils.util.ScoreboardHelper;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JacobsHelperOverlay extends OverlaySection {
    private int alertTick = -1;

    public JacobsHelperOverlay() {
        super(ConfigManager.jacobPosX, ConfigManager.jacobPosY, "Jacob's Helper", Colors.YELLOW);
    }

    private static boolean isJacobsContest() {
        ServerData serverData = mc.getCurrentServerData();
        return serverData != null && serverData.serverIP.equals("hypixel.net") && ScoreboardHelper.stringList().contains("Jacob's Contest");
    }

    private static MedalData getCurrentMedalData() {
        for (String line : ScoreboardHelper.stringList()) {
            String[] parts = line.split(" ");
            if (parts.length == 4 && parts[0].equals("") && MedalData.isMedalName(parts[1]) && parts[2].equals("with")) {
                return new MedalData(parts[1], Integer.parseInt(parts[3].replace(",", "")));
            } else if (parts.length == 3 && parts[0].equals("") && parts[1].equals("Collected")) {
                return new MedalData("NONE", Integer.parseInt(parts[2].replace(",", "")));
            }
        }
        return null;
    }

    private static MedalData getMinRequiredForMedal() {
        MedalData currentMedalData = getCurrentMedalData();
        if (currentMedalData != null) {
            for (String line : ScoreboardHelper.stringList()) {
                MedalData minRequiredForMedal = currentMedalData.getMinRequiredForMedal(line);
                if (minRequiredForMedal != null) {
                    return minRequiredForMedal;
                }
            }
        }
        return null;
    }

    private static int getElapsedTime() {
        for (String line : ScoreboardHelper.stringList()) {
            String[] parts = line.split(" ");
            if (line.length() > 0 && line.charAt(0) == '\u25CB' && parts.length >= 3) {
                String time = parts[parts.length - 1];
                if (time.matches("\\d{1,2}m\\d{1,2}s")) {
                    int minutes = Integer.parseInt(time.substring(0, time.indexOf('m')));
                    int seconds = Integer.parseInt(time.substring(time.indexOf('m') + 1, time.indexOf('s')));
                    return 1200 - minutes * 60 - seconds;
                }
            }
        }
        return -1;
    }

    private static MedalData getMedalDataAfter20Minutes() {
        int elapsedTime = getElapsedTime();
        MedalData minRequiredForMedal = getMinRequiredForMedal();
        return elapsedTime == -1 || minRequiredForMedal == null ? null : new MedalData(minRequiredForMedal.name, (int) (1200f / elapsedTime * minRequiredForMedal.crops));
    }

    private static void drawAlert(int color, String string) {
        Helper.glSetScale(4);
        mc.fontRendererObj.drawStringWithShadow(string, (getScreenWidth() - mc.fontRendererObj.getStringWidth(string)) / 8f, (getScreenHeight() - mc.fontRendererObj.FONT_HEIGHT) / 8f, color);
        Helper.glResetScale();
    }

    @Override
    protected boolean shouldRender() {
        return super.shouldRender() && ConfigManager.showJacobsHelper.get() && isJacobsContest();
    }

    @Override
    protected List<OverlayElement> getElementList() {
        List<OverlayElement> list = new ArrayList<>();
        MedalData after20Minutes = isPreviewMode() ? new MedalData("GOLD", 314159) : getMedalDataAfter20Minutes();
        MedalData currentMedalData = isPreviewMode() ? new MedalData("GOLD", 206410) : getCurrentMedalData();

        if (currentMedalData != null && after20Minutes != null) {
            // base jacob helper overlay
            HashMap<String, String> map = new HashMap<>();
            map.put(after20Minutes.name + " (20m)",  Helper.formatInt(after20Minutes.crops));

            // extra alert-related overlays
            int cropsRequiredForAlert = (int) (after20Minutes.crops * (100 + ConfigManager.alertExtraPercent.get()) / 100f);
            int cropsUntilAlert = cropsRequiredForAlert - currentMedalData.crops;

            if (ConfigManager.showCropsUntilAlert.get()) {
                map.put("Crops until alert", Helper.formatInt(cropsUntilAlert));
            }
            if (ConfigManager.showTimeUntilAlert.get()) {
                map.put("Time until alert", Helper.formatTime((int) ((double) cropsUntilAlert / (double) currentMedalData.crops * (double) (isPreviewMode() ? 750 : getElapsedTime()))));
            }

            list.add(new OverlayTable(map));

            // alert logic
            if (ConfigManager.jacobsHelperAlert.get()) {
                if (currentMedalData.crops >= cropsRequiredForAlert) {
                    if (alertTick == -1) {
                        alertTick = getCurrentTick();
                        Helper.playClientSound("random.orb");
                    }
                    if (getCurrentTick() - alertTick < 60) {
                        drawAlert(after20Minutes.getColor(), EnumChatFormatting.BOLD + after20Minutes.name + EnumChatFormatting.RESET + " secured!");
                    }
                } else {
                    alertTick = -1;
                }
            }

        // probably "Updating ranking..."
        } else if (ConfigManager.showWarnings.get()) {
            list.add(new OverlayList(Colors.YELLOW, "Couldn't parse data", "for Jacob's Contest"));
        }
        return list;
    }
}
