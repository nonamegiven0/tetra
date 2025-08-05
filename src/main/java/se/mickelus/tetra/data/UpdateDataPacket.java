package se.mickelus.tetra.data;

import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonElement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.data.AbstractUpdateDataPacket;

@ParametersAreNonnullByDefault
public class UpdateDataPacket extends AbstractUpdateDataPacket {
	public static final CustomPacketPayload.Type<UpdateDataPacket> TYPE = CustomPacketPayload.createType("update_data");
	public static final StreamCodec<FriendlyByteBuf, UpdateDataPacket> CODEC = StreamCodec.ofMember(UpdateDataPacket::toBytes, buf -> {
		UpdateDataPacket packet = new UpdateDataPacket();
		packet.fromBytes(buf);
		return packet;
	});
    public UpdateDataPacket() {
    }

    public UpdateDataPacket(String directory, Map<ResourceLocation, JsonElement> data) {
        super(directory, data);
    }

    @Override
    public void handle(Player player) {
        DataManager.instance.onDataRecieved(directory, data);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
