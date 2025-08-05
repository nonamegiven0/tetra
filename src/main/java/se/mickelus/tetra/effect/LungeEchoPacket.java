package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class LungeEchoPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<LungeEchoPacket> TYPE = CustomPacketPayload.createType("lunge_echo");
	public static final StreamCodec<FriendlyByteBuf, LungeEchoPacket> CODEC = StreamCodec.ofMember(LungeEchoPacket::toBytes, buf -> {
		LungeEchoPacket packet = new LungeEchoPacket();
		packet.fromBytes(buf);
		return packet;
	});
    boolean isVertical;

    public LungeEchoPacket() {
    }

    public LungeEchoPacket(boolean isVertical) {
        this.isVertical = isVertical;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isVertical);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        isVertical = buffer.readBoolean();
    }

    @Override
    public void handle(Player player) {
        LungeEffect.receiveEchoPacket(player, isVertical);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
