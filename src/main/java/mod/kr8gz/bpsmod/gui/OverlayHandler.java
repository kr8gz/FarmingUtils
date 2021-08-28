package mod.kr8gz.bpsmod.gui;

import mod.kr8gz.bpsmod.config.ConfigManager;
import mod.kr8gz.bpsmod.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IWorldAccess;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class OverlayHandler {
    private static final ArrayList<Integer> breaks = new ArrayList<>();
    private static int current;
    private static final String[] warningStrings = new String[] {
        "More than 1 player",
        "in current world"
    };

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
        return Helper.round(s / (ticks / 20f), ConfigManager.roudingPrecision);
    }

    private void drawInTopRightCorner(String string, int i) {
        drawInTopRightCorner(string, i, 0xffffff);
    }

    private void drawInTopRightCorner(String string, int i, int color) {
        Helper.drawOverlayString(
                string,
                -ConfigManager.padding,
                ConfigManager.padding + i * (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + ConfigManager.padding),
                ConfigManager.overlayScale,
                color
        );
    }

    @SubscribeEvent
    public void renderText(RenderGameOverlayEvent.Text event) {
        if (!ConfigManager.disableOverlay) {
            int i = 0;
            ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
            int playerLimit = serverData != null && serverData.serverIP.equals("hypixel.net") ? 2 : 1; // hypixel -1 because watchdog
            if (Minecraft.getMinecraft().theWorld.playerEntities.size() == playerLimit) {
                for (int time : ConfigManager.bpsTimes) {
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
                    drawInTopRightCorner(string, i);
                    i++;
                }
            } else if (!ConfigManager.disableWarning) {
                for (String string : warningStrings) {
                    drawInTopRightCorner(string, i, 0xffff00);
                    i++;
                }
            }
        }
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            current++;
        }
    }

    @SubscribeEvent
    public void registerCustomWorldAccess(WorldEvent.Load event) {
        if (event.world.isRemote) {
            event.world.addWorldAccess(new CustomWorldAccess());
        }
    }

    private static class CustomWorldAccess implements IWorldAccess {
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
            if (sfxType == 2001) { // block break
                breaks.add(current);
                if (current - breaks.get(0) > 3600) {
                    breaks.remove(0);
                }
            }
        }
    }
}