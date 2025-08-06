package se.mickelus.tetra.blocks;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import se.mickelus.mutil.network.PacketHandler;

public interface InitializableBlock {

    @OnlyIn(Dist.CLIENT)
    default void clientInit() {
    }
    
    @OnlyIn(Dist.CLIENT)
    default void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
    }

    default void commonInit(PacketHandler packetHandler) {
    }
}
