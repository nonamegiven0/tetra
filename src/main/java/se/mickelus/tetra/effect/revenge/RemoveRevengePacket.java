package se.mickelus.tetra.effect.revenge;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class RemoveRevengePacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<RemoveRevengePacket> TYPE = CustomPacketPayload.createType("remove_revenge");
	public static final StreamCodec<FriendlyByteBuf, RemoveRevengePacket> CODEC = StreamCodec.ofMember(RemoveRevengePacket::toBytes, buf -> {
		RemoveRevengePacket packet = new RemoveRevengePacket();
		packet.fromBytes(buf);
		return packet;
	});
    private int entityId = -1;

    public RemoveRevengePacket(Entity attacker) {
        this.entityId = attacker.getId();
    }

    public RemoveRevengePacket() {
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        entityId = buffer.readVarInt();
    }

    @Override
    public void handle(Player player) {
        RevengeTracker.removeEnemy(player, entityId);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
