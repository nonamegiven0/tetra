package se.mickelus.tetra.items.modular.impl.toolbelt;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class OpenToolbeltItemPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<OpenToolbeltItemPacket> TYPE = CustomPacketPayload.createType("open_toolbelt_item");
	public static final StreamCodec<FriendlyByteBuf, OpenToolbeltItemPacket> CODEC = StreamCodec.ofMember(OpenToolbeltItemPacket::toBytes, buf -> {
		OpenToolbeltItemPacket packet = new OpenToolbeltItemPacket();
		packet.fromBytes(buf);
		return packet;
	});

    public OpenToolbeltItemPacket() {
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void handle(Player player) {
        ItemStack itemStack = ToolbeltHelper.findToolbelt(player);
        if (!itemStack.isEmpty()) {
//            NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) itemStack.getItem());
            player.openMenu((MenuProvider) itemStack.getItem());
        }
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
