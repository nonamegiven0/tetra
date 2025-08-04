package se.mickelus.tetra.blocks;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import se.mickelus.mutil.network.PacketHandler;

public interface InitializableBlock {

    @OnlyIn(Dist.CLIENT)
    default void clientInit() {
    }

    default void commonInit(PacketHandler packetHandler) {
    }
}
