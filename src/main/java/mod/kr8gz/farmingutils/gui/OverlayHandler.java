package mod.kr8gz.farmingutils.gui;

import mod.kr8gz.farmingutils.FarmingUtils;
import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.data.MedalData;
import mod.kr8gz.farmingutils.util.Helper;
import mod.kr8gz.farmingutils.util.ScoreboardHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IWorldAccess;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class OverlayHandler {
    private final ArrayList<Integer> breaks = new ArrayList<>();
    private int current;
    private final String[] BPSWarningStrings = new String[] {
        "More than 1 player",
        "in current world"
    };
    private int alertTick = -1;
    private final Path logPath = Paths.get("logs/." + FarmingUtils.MODID + ".log");

    /** calculating methods */
    private int getBPS() {
        int s = 0;
        for (int b : breaks) {
            if (current - b < 20) {
                s++;
            }
        }
        return s;
    }

    private float getBPS(int ticks) {
        int s = 0;
        for (int b : breaks) {
            if (current - b < ticks) {
                s++;
            }
        }
        return Helper.round(s / (ticks / 20f), ConfigManager.roudingPrecision.get());
    }

    private MedalData getMedalDataAfter20Minutes() {
        int elapsedTime = getElapsedTime();
        MedalData minRequiredForMedal = getMinRequiredForMedal();
        return elapsedTime == -1 || minRequiredForMedal == null ? null : new MedalData(minRequiredForMedal.name, (int) (1200f / elapsedTime * minRequiredForMedal.crops));
    }

    /** checking methods */
    private boolean isAlone() {
        ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
        return Minecraft.getMinecraft().theWorld.playerEntities.size() == (serverData != null && serverData.serverIP.equals("hypixel.net") ? 2 : 1);
    }

    private boolean isJacobsContest() {
        ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
        return serverData != null && serverData.serverIP.equals("hypixel.net") && ScoreboardHelper.stringList().contains("Jacob's Contest");
    }

    /** parsing methods */
    private MedalData getCurrentMedalData() {
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

    private MedalData getMinRequiredForMedal() {
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

    private int getElapsedTime() {
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

    /** rendering methods */
    private void drawInTopRightCorner(String string, int offset) {
        drawInTopRightCorner(string, offset, 0xffffff);
    }

    private void drawInTopRightCorner(String string, int offset, int color) {
        float padding = ConfigManager.padding.get().floatValue();
        float scale = ConfigManager.overlayScale.get().floatValue();
        Helper.drawOverlayString(
                string,
                -padding,
                scale * (padding + offset * (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + padding)),
                scale,
                color
        );
    }

    private void drawInMiddle(String string, int color) {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        Helper.drawOverlayString(string,
                (scaledRes.getScaledWidth() - 4 * Minecraft.getMinecraft().fontRendererObj.getStringWidth(string)) / 2f,
                (scaledRes.getScaledHeight() - 4 * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) / 2f,
                4,
                color
        );
    }

    /** subscribe events */
    @SubscribeEvent
    public void renderText(RenderGameOverlayEvent.Text event) {
        if (ConfigManager.showOverlay.get()) {
            int offset = 0;

            if (ConfigManager.showBPS.get()) {
                if (isAlone()) {
                    for (int time : ConfigManager.bpsTimes.get()) {
                        String string;
                        if (time == 1) {
                            string = String.format("BPS (%ss): %s", time, getBPS());
                        } else {
                            string = String.format(
                                    time % 60 == 0 ? "BPS (%sm): %s" : "BPS (%ss): %s",
                                    time % 60 == 0 ? time / 60 : time,
                                    getBPS(20 * time)
                            );
                        }
                        drawInTopRightCorner(string, offset);
                        offset++;
                    }
                } else if (ConfigManager.showWarnings.get()) {
                    for (String string : BPSWarningStrings) {
                        drawInTopRightCorner(string, offset, 0xffff00);
                        offset++;
                    }
                }
                offset++;
            }

            if (ConfigManager.showJacobsHelper.get() && isJacobsContest()) {
                MedalData after20Minutes = getMedalDataAfter20Minutes();
                if (after20Minutes != null) {
                    String s = String.format("%s (20m): %s", after20Minutes.name, Helper.formatInt(after20Minutes.crops));
                    drawInTopRightCorner(s, offset);
                    if (ConfigManager.jacobsHelperAlert.get()) {
                        MedalData currentMedalData = getCurrentMedalData();
                        if (currentMedalData != null && currentMedalData.crops >= after20Minutes.crops * (100 + ConfigManager.alertExtraPercent.get()) / 100f) {
                            if (alertTick == -1) {
                                alertTick = current;
                                Helper.playClientSound("random.orb");
                            }
                            if (alertTick - current < 60) {
                                drawInMiddle(EnumChatFormatting.BOLD + after20Minutes.name + EnumChatFormatting.RESET + " secured!", after20Minutes.getColor());
                            } else if (alertTick - current >= 60) {
                                alertTick = -1;
                            }
                        }
                    }
                } else if (ConfigManager.showWarnings.get()) {
                    drawInTopRightCorner("Couldn't parse data for Jacob's Contest", offset, 0xffff00);
                }
            }
        }
    }

    private void logInfo() {
        try {
            try {
                Files.createFile(logPath);
            } catch (FileAlreadyExistsException ignored) {}

            List<String> lines = Files.readAllLines(logPath);

            lines.add("--- client tick " + current + " ---");
            lines.add("scoreboard: " + ScoreboardHelper.stringList());
            lines.add("jacob: " + isJacobsContest());
            if (isJacobsContest()) {
                lines.add("time:  " + getElapsedTime());
                lines.add("have:  " + getCurrentMedalData());
                lines.add("need:  " + getMinRequiredForMedal());
                lines.add("20min: " + getMedalDataAfter20Minutes());
            }

            Files.write(logPath, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            current++;

            if (ConfigManager.logInfo.get()) {
                if (current == 1) {
                    try {
                        try {
                            Files.createFile(logPath);
                        } catch (FileAlreadyExistsException ignored) {}
                        Files.write(logPath, new ArrayList<>());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (current % 200 == 0) {
                    logInfo();
                }
            }
        }
    }

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        breaks.clear();
    }

    @SubscribeEvent
    public void registerCustomWorldAccess(WorldEvent.Load event) {
        if (event.world.isRemote) {
            event.world.addWorldAccess(new CustomWorldAccess());
        }
    }

    private class CustomWorldAccess implements IWorldAccess {
        public void markBlockForUpdate(BlockPos pos) {}
        public void notifyLightSet(BlockPos pos) {}
        public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {}
        public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {}
        public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {}
        public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_180442_15_) {}
        public void onEntityAdded(Entity entityIn) {}
        public void onEntityRemoved(Entity entityIn) {}
        public void playRecord(String recordName, BlockPos blockPosIn) {}
        public void broadcastSound(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_) {}
        public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {}

        @Override
        public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int p_180439_4_) {
            if (isAlone() && sfxType == 2001) { // block break
                breaks.add(current);
                if (current - breaks.get(0) > 3600) {
                    breaks.remove(0);
                }
            }
        }
    }
}