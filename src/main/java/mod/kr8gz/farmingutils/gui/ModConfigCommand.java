package mod.kr8gz.farmingutils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ModConfigCommand implements ICommand {
    private static boolean tickFlag;

    @Override
    public String getCommandName() {
        return "farmingutils";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("fu");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        tickFlag = true;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@Nonnull ICommand o) {
        return this.getCommandName().compareTo(o.getCommandName());
    }

    public static class EventHandler {
        @SuppressWarnings("unused")
        @SubscribeEvent
        public void oneTickDelay(TickEvent.ClientTickEvent event) {
            if (tickFlag) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiModConfig());
                tickFlag = false;
            }
        }
    }
}
