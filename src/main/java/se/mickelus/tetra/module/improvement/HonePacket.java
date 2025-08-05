package se.mickelus.tetra.module.improvement;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.tetra.util.PacketUtil;

@ParametersAreNonnullByDefault
public class HonePacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<HonePacket> TYPE = CustomPacketPayload.createType("hone");
	public static final StreamCodec<FriendlyByteBuf, HonePacket> CODEC = StreamCodec.ofMember(HonePacket::toBytes, buf -> {
		HonePacket packet = new HonePacket();
		packet.fromBytes(buf);
		return packet;
	});

    ItemStack itemStack;

    public HonePacket() {
    }

    public HonePacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
//        buffer.writeItem(itemStack);
    	PacketUtil.writeItem(buffer, itemStack);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
//        itemStack = buffer.readItem();
    	itemStack = PacketUtil.readItem(buffer);
    }

    @Override
    public void handle(Player player) {
        ProgressionHelper.showHoneToastClient(itemStack);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
