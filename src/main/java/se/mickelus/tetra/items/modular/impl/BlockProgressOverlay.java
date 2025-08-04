package se.mickelus.tetra.items.modular.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.neoforged.neoforge.event.TickEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BlockProgressOverlay implements IGuiOverlay {
    private final Minecraft mc;

    private final GuiBlockProgress gui;

    public BlockProgressOverlay(Minecraft mc) {
        this.mc = mc;

        gui = new GuiBlockProgress(mc);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.START == event.phase && mc.player != null) {
            ItemStack activeStack = mc.player.getUseItem();

            gui.setProgress(
                    CastOptional.cast(activeStack.getItem(), ItemModularHandheld.class)
                            .map(item -> item.getBlockProgress(activeStack, mc.player))
                            .orElse(0f));
        }
    }

    @Override
    public void render(ExtendedGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        this.gui.draw(guiGraphics);
    }
}
