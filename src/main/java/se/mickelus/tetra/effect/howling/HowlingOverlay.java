package se.mickelus.tetra.effect.howling;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.neoforged.neoforge.event.TickEvent;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class HowlingOverlay implements IGuiOverlay {
    private final Minecraft mc;

    private final HowlingProgressGui gui;

    public HowlingOverlay(Minecraft mc) {
        this.mc = mc;

        gui = new HowlingProgressGui(mc);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.START == event.phase) {
            int amplifier = Optional.ofNullable(mc.player)
                    .map(player -> player.getEffect(HowlingPotionEffect.instance))
                    .map(MobEffectInstance::getAmplifier)
                    .orElse(-1);

            gui.updateAmplifier(amplifier);
        }
    }

    @Override
    public void render(ExtendedGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        this.gui.draw(graphics);
    }
}
