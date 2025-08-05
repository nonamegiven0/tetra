package se.mickelus.tetra.items.modular.impl.bow;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class ProjectileMotionPacket extends AbstractPacket {
	public static final CustomPacketPayload.Type<ProjectileMotionPacket> TYPE = CustomPacketPayload.createType("projectile_motion");
	public static final StreamCodec<FriendlyByteBuf, ProjectileMotionPacket> CODEC = StreamCodec.ofMember(ProjectileMotionPacket::toBytes, buf -> {
		ProjectileMotionPacket packet = new ProjectileMotionPacket();
		packet.fromBytes(buf);
		return packet;
	});
    private int entityId = -1;
    private float motionX;
    private float motionY;
    private float motionZ;

    public ProjectileMotionPacket() {
    }

    public ProjectileMotionPacket(Projectile target) {
        entityId = target.getId();

        Vec3 motion = target.getDeltaMovement();
        motionX = (float) motion.x;
        motionY = (float) motion.y;
        motionZ = (float) motion.z;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeFloat(motionX);
        buffer.writeFloat(motionY);
        buffer.writeFloat(motionZ);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        entityId = buffer.readVarInt();
        motionX = buffer.readFloat();
        motionY = buffer.readFloat();
        motionZ = buffer.readFloat();
    }

    @Override
    public void handle(Player player) {
        Optional.of(entityId)
                .filter(id -> id != -1)
                .map(id -> player.level().getEntity(id))
                .ifPresent(entity -> entity.setDeltaMovement(motionX, motionY, motionZ));
    }

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
