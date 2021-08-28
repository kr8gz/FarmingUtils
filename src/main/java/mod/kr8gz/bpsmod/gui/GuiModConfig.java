package mod.kr8gz.bpsmod.gui;

import mod.kr8gz.bpsmod.BPSMod;
import mod.kr8gz.bpsmod.config.ConfigManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class GuiModConfig extends GuiConfig {
    public GuiModConfig(GuiScreen parent) {
        super(
                parent,
                getConfigElements(),
                BPSMod.MODID, "general",
                false,
                false,
                BPSMod.NAME + " Settings"
        );
    }

    private static List<IConfigElement> getConfigElements() {
        return (new ConfigElement(ConfigManager.config.getCategory("general"))).getChildElements();
    }

    @Override
    protected void keyTyped(char chr, int key) {
        if (key == Keyboard.KEY_ESCAPE) {
            actionPerformed(new GuiButton(2000, 0, 0, null)); // same as "Done" button pressed
        }
        super.keyTyped(chr, key);
    }
}