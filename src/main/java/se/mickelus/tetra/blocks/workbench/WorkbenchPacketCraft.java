package se.mickelus.tetra.blocks.workbench;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.BlockPosPacket;

@ParametersAreNonnullByDefault
public class WorkbenchPacketCraft extends BlockPosPacket {
	public static final CustomPacketPayload.Type<WorkbenchPacketCraft> TYPE = CustomPacketPayload.createType("workbench_craft");
	public static final StreamCodec<FriendlyByteBuf, WorkbenchPacketCraft> CODEC = StreamCodec.ofMember(WorkbenchPacketCraft::toBytes, buf -> {
		WorkbenchPacketCraft packet = new WorkbenchPacketCraft();
		packet.fromBytes(buf);
		return packet;
	});

    public WorkbenchPacketCraft() {
    }

    public WorkbenchPacketCraft(BlockPos pos) {
        super(pos);
    }

    @Override
    public void handle(Player player) {
        WorkbenchTile workbench = (WorkbenchTile) player.level().getBlockEntity(pos);
        if (workbench != null) {
            workbench.craft(player);
        }
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
