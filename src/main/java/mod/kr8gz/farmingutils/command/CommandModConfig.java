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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandModConfig implements ICommand {
    private static final String COMMAND_NAME = "farmingutils";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList("fu");

    private static final List<String> ARGUMENT_EDIT_OVERLAY_ALIASES = Arrays.asList("overlay", "o");

    private static ModGuiScreen screen = null;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return String.format("/%s <%s>", getCommandName(), String.join("|", ARGUMENT_EDIT_OVERLAY_ALIASES));
    }

    @Override
    public List<String> getCommandAliases() {
        return COMMAND_ALIASES;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        switch (args.length) {
            case 0:
                screen = new GuiModConfig(null);
                break;
            case 1:
                if (ARGUMENT_EDIT_OVERLAY_ALIASES.stream().anyMatch(args[0]::equalsIgnoreCase)) {
                    screen = new GuiEditOverlay(null);
                    break;
                }
            default:
                throw new WrongUsageException(getCommandUsage(sender));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>(ARGUMENT_EDIT_OVERLAY_ALIASES);
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
