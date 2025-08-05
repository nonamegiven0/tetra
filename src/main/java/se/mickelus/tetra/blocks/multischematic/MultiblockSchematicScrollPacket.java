package se.mickelus.tetra.blocks.multischematic;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

public class MultiblockSchematicScrollPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<MultiblockSchematicScrollPacket> TYPE = CustomPacketPayload.createType("multiblock_schematic_scroll");
	public static final StreamCodec<FriendlyByteBuf, MultiblockSchematicScrollPacket> CODEC = StreamCodec.ofMember(MultiblockSchematicScrollPacket::toBytes, buf -> {
		MultiblockSchematicScrollPacket packet = new MultiblockSchematicScrollPacket();
		packet.fromBytes(buf);
		return packet;
	});
    boolean isIncrease;

    public MultiblockSchematicScrollPacket() {
    }

    public MultiblockSchematicScrollPacket(boolean isIncrease) {
        this.isIncrease = isIncrease;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isIncrease);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        isIncrease = buffer.readBoolean();
    }

    @Override
    public void handle(Player player) {
        MultiblockSchematicScrollHandler.shiftSchematic(player, isIncrease);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
