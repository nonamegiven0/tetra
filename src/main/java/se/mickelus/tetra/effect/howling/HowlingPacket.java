package se.mickelus.tetra.effect.howling;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class HowlingPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<HowlingPacket> TYPE = CustomPacketPayload.createType("howling");
	public static final StreamCodec<FriendlyByteBuf, HowlingPacket> CODEC = StreamCodec.ofMember(HowlingPacket::toBytes, buf -> {
		HowlingPacket packet = new HowlingPacket();
		packet.fromBytes(buf);
		return packet;
	});
	
    public HowlingPacket() {
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void handle(Player player) {
        ItemStack itemStack = player.getMainHandItem();
        if (player.getAttackStrengthScale(0.5f) > 0.9f) {
            int effectLevel = EffectHelper.getEffectLevel(itemStack, ItemEffect.howling);
            if (effectLevel > 0) {
                HowlingEffect.trigger(itemStack, player, effectLevel);
            }
        }
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
