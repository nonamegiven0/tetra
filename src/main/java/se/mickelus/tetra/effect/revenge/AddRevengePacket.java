package se.mickelus.tetra.effect.revenge;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class AddRevengePacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<AddRevengePacket> TYPE = CustomPacketPayload.createType("add_revenge");
	public static final StreamCodec<FriendlyByteBuf, AddRevengePacket> CODEC = StreamCodec.ofMember(AddRevengePacket::toBytes, buf -> {
		AddRevengePacket packet = new AddRevengePacket();
		packet.fromBytes(buf);
		return packet;
	});
	
    private int entityId = -1;

    public AddRevengePacket(Entity attacker) {
        this.entityId = attacker.getId();
    }

    public AddRevengePacket() {
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
        RevengeTracker.addEnemy(player, entityId);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
