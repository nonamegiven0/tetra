package se.mickelus.tetra.items.modular;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class SecondaryAbilityPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<SecondaryAbilityPacket> TYPE = CustomPacketPayload.createType("secondary_ability");
	public static final StreamCodec<FriendlyByteBuf, SecondaryAbilityPacket> CODEC = StreamCodec.ofMember(SecondaryAbilityPacket::toBytes, buf -> {
		SecondaryAbilityPacket packet = new SecondaryAbilityPacket();
		packet.fromBytes(buf);
		return packet;
	});
	
    private int targetId = -1;
    private InteractionHand hand;

    public SecondaryAbilityPacket() {
    }

    public SecondaryAbilityPacket(@Nullable LivingEntity target, InteractionHand hand) {
        targetId = Optional.ofNullable(target)
                .map(Entity::getId)
                .orElse(-1);

        this.hand = hand;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(targetId);
        buffer.writeInt(hand.ordinal());
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        targetId = buffer.readInt();
        hand = InteractionHand.values()[buffer.readInt()];
    }

    @Override
    public void handle(Player player) {
        LivingEntity target = Optional.of(targetId)
                .filter(id -> id != -1)
                .map(id -> player.level().getEntity(id))
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .orElse(null);

        ItemModularHandheld.handleSecondaryAbility(player, hand, target);
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
