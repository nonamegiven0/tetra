package se.mickelus.tetra.items.modular.impl.toolbelt.booster;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.tetra.items.data.BoosterItemDataComponent;
import se.mickelus.tetra.items.data.TetraDataComponents;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;

@ParametersAreNonnullByDefault
public class UpdateBoosterPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<UpdateBoosterPacket> TYPE = CustomPacketPayload.createType("update_booster");
	public static final StreamCodec<FriendlyByteBuf, UpdateBoosterPacket> CODEC = StreamCodec.ofMember(UpdateBoosterPacket::toBytes, buf -> {
		UpdateBoosterPacket packet = new UpdateBoosterPacket();
		packet.fromBytes(buf);
		return packet;
	});

    private boolean active;
    private boolean charged;

    public UpdateBoosterPacket() {
    }

    public UpdateBoosterPacket(boolean active) {
        this(active, false);
    }

    public UpdateBoosterPacket(boolean active, boolean charged) {
        this.active = active;
        this.charged = charged;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(active);
        buffer.writeBoolean(charged);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        active = buffer.readBoolean();
        charged = buffer.readBoolean();
    }

    @Override
    public void handle(Player player) {
        ItemStack itemStack = ToolbeltHelper.findToolbelt(player);

        if (!itemStack.isEmpty() && UtilBooster.canBoost(itemStack)) {
            BoosterItemDataComponent data = itemStack.get(TetraDataComponents.BOOSTER_ITEM);
            UtilBooster.setActive(data, active, charged);
            itemStack.set(TetraDataComponents.BOOSTER_ITEM, data);
            // UtilToolbelt.updateBauble(player);
        }

    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

}
