package se.mickelus.tetra.blocks.workbench.action;

import java.io.IOException;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.BlockPosPacket;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;

@ParametersAreNonnullByDefault
public class WorkbenchActionPacket extends BlockPosPacket {
	public static final CustomPacketPayload.Type<WorkbenchActionPacket> TYPE = CustomPacketPayload.createType("workbench_action");
	public static final StreamCodec<FriendlyByteBuf, WorkbenchActionPacket> CODEC = StreamCodec.ofMember(WorkbenchActionPacket::toBytes, buf -> {
		WorkbenchActionPacket packet = new WorkbenchActionPacket();
		packet.fromBytes(buf);
		return packet;
	});

    private String actionKey;

    public WorkbenchActionPacket() {
    }

    public WorkbenchActionPacket(BlockPos pos, String actionKey) {
        super(pos);
        this.actionKey = actionKey;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        super.toBytes(buffer);
        try {
            writeString(actionKey, buffer);
        } catch (IOException exception) {
            System.err.println("An error occurred when writing action name to packet buffer");
        }
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        super.fromBytes(buffer);

        try {
            actionKey = readString(buffer);
        } catch (IOException exception) {
            System.err.println("An error occurred when reading action name from packet buffer");
        }
    }

    @Override
    public void handle(Player player) {
        WorkbenchTile workbench = (WorkbenchTile) player.level().getBlockEntity(pos);
        if (workbench != null) {
            workbench.performAction(player, actionKey);
        }
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
