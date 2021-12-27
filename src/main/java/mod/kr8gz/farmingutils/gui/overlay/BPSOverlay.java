package mod.kr8gz.farmingutils.gui.overlay;

import mod.kr8gz.farmingutils.config.ConfigManager;
import mod.kr8gz.farmingutils.gui.KeybindManager;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlayElement;
import mod.kr8gz.farmingutils.gui.overlay.elements.OverlaySection;
import mod.kr8gz.farmingutils.util.Colors;
import mod.kr8gz.farmingutils.util.Helper;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IWorldAccess;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BPSOverlay extends OverlaySection {
    private static final ArrayList<Integer> breaks = new ArrayList<>();

    public BPSOverlay() {
        super(ConfigManager.BPSPosX, ConfigManager.BPSPosY, "BPS", Colors.LIGHTBLUE);
    }

    private static int getBPS() {
        int s = 0;
        for (int b : breaks) {
            if (getCurrentTick() - b < 20) {
                s++;
            }
        }
        return s;
    }

    private static float getBPS(int ticks) {
        int s = 0;
        for (int b : breaks) {
            if (getCurrentTick() - b < ticks) {
                s++;
            }
        }
        return Helper.round(s / (ticks / 20f), ConfigManager.roudingPrecision.get());
    }

    private static boolean isAlone() {
        ServerData serverData = mc.getCurrentServerData();
        int limit = serverData != null && serverData.serverIP.equals("hypixel.net") ? 2 : 1;
        return mc.theWorld.playerEntities.stream().filter(e -> e.getUniqueID().version() != 2).count() == limit;
    }

    @SuppressWarnings("unused")
    public static class EventHandler {
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
                if (isAlone() && sfxType == 2001) { // block break
                    breaks.add(getCurrentTick());
                    if (getCurrentTick() - breaks.get(0) > 3600) {
                        breaks.remove(0);
                    }
                }
            }
        }
    }

    @Override
    protected boolean shouldRender() {
        return super.shouldRender() && ConfigManager.showBPS.get() && !KeybindManager.BPSToggled.get();
    }

    @Override
    protected List<OverlayElement> getElementList() {
        List<OverlayElement> list = new ArrayList<>();
        if (isAlone() || isPreviewMode()) {
            List<List<String>> strings = new ArrayList<>();
            for (int time : ConfigManager.bpsTimes.get()) {
                if (time == 1) {
                    strings.add(Arrays.asList(
                            Helper.formatTime(time),
                            String.valueOf(isPreviewMode() ? 12 : getBPS())
                    ));
                } else {
                    strings.add(Arrays.asList(
                            Helper.formatTime(time),
                            String.valueOf(isPreviewMode() ? Helper.round(12.34567f, ConfigManager.roudingPrecision.get()) : getBPS(20 * time))
                    ));
                }
            }
            list.add(new OverlayElement(strings));
        } else if (ConfigManager.showWarnings.get()) {
             list.add(new OverlayElement(Colors.YELLOW, "More than 1 player", "in current world"));
        }
        return list;
    }
}
