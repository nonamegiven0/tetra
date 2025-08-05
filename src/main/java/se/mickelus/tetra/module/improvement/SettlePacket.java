package se.mickelus.tetra.module.improvement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.tetra.util.PacketUtil;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SettlePacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<SettlePacket> TYPE = CustomPacketPayload.createType("settle");
	public static final StreamCodec<FriendlyByteBuf, SettlePacket> CODEC = StreamCodec.ofMember(SettlePacket::toBytes, buf -> {
		SettlePacket packet = new SettlePacket();
		packet.fromBytes(buf);
		return packet;
	});
	
    ItemStack itemStack;
    String slot;

    public SettlePacket() {
    }

    public SettlePacket(ItemStack itemStack, String slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        PacketUtil.writeItem(buffer, itemStack);
        buffer.writeUtf(slot);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        itemStack = PacketUtil.readItem(buffer);
        slot = buffer.readUtf();
    }

    @Override
    public void handle(Player player) {
        ProgressionHelper.showSettleToastClient(itemStack, slot);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
