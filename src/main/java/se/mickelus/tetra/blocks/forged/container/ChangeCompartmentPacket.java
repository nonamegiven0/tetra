package se.mickelus.tetra.blocks.forged.container;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.mutil.util.CastOptional;

@ParametersAreNonnullByDefault
public class ChangeCompartmentPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<ChangeCompartmentPacket> TYPE = CustomPacketPayload.createType("change_compartment");
	public static final StreamCodec<FriendlyByteBuf, ChangeCompartmentPacket> CODEC = StreamCodec.ofMember(ChangeCompartmentPacket::toBytes, buf -> {
		ChangeCompartmentPacket packet = new ChangeCompartmentPacket();
		packet.fromBytes(buf);
		return packet;
	});

    private int compartmentIndex;

    public ChangeCompartmentPacket() {
    }

    public ChangeCompartmentPacket(int compartmentIndex) {
        this.compartmentIndex = compartmentIndex;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(compartmentIndex);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        compartmentIndex = buffer.readInt();
    }

    @Override
    public void handle(Player player) {
        CastOptional.cast(player.containerMenu, ForgedContainerMenu.class)
                .ifPresent(container -> container.changeCompartment(compartmentIndex));
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
