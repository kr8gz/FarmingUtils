package mod.kr8gz.farmingutils.command;

import mod.kr8gz.farmingutils.screens.GuiEditOverlay;
import mod.kr8gz.farmingutils.screens.GuiModConfig;
import mod.kr8gz.farmingutils.screens.ModGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandModConfig implements ICommand {
    private static ModGuiScreen screen = null;

    @Override
    public String getCommandName() {
        return "farmingutils";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <o|overlay>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("fu");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            screen = new GuiModConfig(null);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("o") || args[0].equalsIgnoreCase("overlay")) {
                screen = new GuiEditOverlay(null);
            } else {
                throw new WrongUsageException(getCommandUsage(sender));
            }
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> list = new ArrayList<>();
        list.add("o");
        list.add("overlay");
        return list;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@Nonnull ICommand o) {
        return this.getCommandName().compareTo(o.getCommandName());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void oneTickDelay(TickEvent.ClientTickEvent event) {
        if (screen != null) {
            Minecraft.getMinecraft().displayGuiScreen(screen);
            screen = null;
        }
    }
}
