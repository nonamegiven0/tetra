package se.mickelus.tetra.items.modular.impl.toolbelt.suspend;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class ToggleSuspendPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<ToggleSuspendPacket> TYPE = CustomPacketPayload.createType("toggle_suspend");
	public static final StreamCodec<FriendlyByteBuf, ToggleSuspendPacket> CODEC = StreamCodec.ofMember(ToggleSuspendPacket::toBytes, buf -> {
		ToggleSuspendPacket packet = new ToggleSuspendPacket();
		packet.fromBytes(buf);
		return packet;
	});

    boolean toggleOn;

    public ToggleSuspendPacket() {
    }

    public ToggleSuspendPacket(boolean toggleOn) {
        this.toggleOn = toggleOn;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(toggleOn);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        toggleOn = buffer.readBoolean();
    }

    @Override
    public void handle(Player player) {
        SuspendEffect.toggleSuspend(player, toggleOn);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
