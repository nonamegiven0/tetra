package se.mickelus.tetra.blocks.forged.extractor;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.BlockPosPacket;
import se.mickelus.mutil.util.TileEntityOptional;

@ParametersAreNonnullByDefault
public class CoreExtractorPistonUpdatePacket extends BlockPosPacket {
	public static final CustomPacketPayload.Type<CoreExtractorPistonUpdatePacket> TYPE = CustomPacketPayload.createType("core_extractor_piston_update");
	public static final StreamCodec<FriendlyByteBuf, CoreExtractorPistonUpdatePacket> CODEC = StreamCodec.ofMember(CoreExtractorPistonUpdatePacket::toBytes, buf -> {
		CoreExtractorPistonUpdatePacket packet = new CoreExtractorPistonUpdatePacket();
		packet.fromBytes(buf);
		return packet;
	});
    private long timestamp;

    public CoreExtractorPistonUpdatePacket() {
    }

    public CoreExtractorPistonUpdatePacket(BlockPos pos, long timestamp) {
        super(pos);

        this.timestamp = timestamp;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeLong(timestamp);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        super.fromBytes(buffer);
        timestamp = buffer.readLong();
    }

    @Override
    public void handle(Player player) {
        TileEntityOptional.from(player.level(), pos, CoreExtractorPistonBlockEntity.class)
                .ifPresent(tile -> tile.setEndTime(timestamp));
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
