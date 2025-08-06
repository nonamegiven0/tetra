package se.mickelus.tetra.items.modular.impl.toolbelt;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StoreToolbeltItemPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<StoreToolbeltItemPacket> TYPE = CustomPacketPayload.createType("store_toolbelt_item");
	public static final StreamCodec<FriendlyByteBuf, StoreToolbeltItemPacket> CODEC = StreamCodec.ofMember(StoreToolbeltItemPacket::toBytes, buf -> {
		StoreToolbeltItemPacket packet = new StoreToolbeltItemPacket();
		packet.fromBytes(buf);
		return packet;
	});
	
    public StoreToolbeltItemPacket() {
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void handle(Player player) {
        ToolbeltHelper.storeItemInToolbelt(player);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
