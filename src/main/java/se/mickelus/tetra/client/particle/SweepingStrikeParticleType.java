package se.mickelus.tetra.client.particle;

import com.mojang.serialization.Codec;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SweepingStrikeParticleType extends ParticleType<SweepingStrikeParticleOption> {
    public static final String identifier = "sweeping_strike";

    public static DeferredHolder<ParticleType<?>, ParticleType<SweepingStrikeParticleOption>> instance;

    public SweepingStrikeParticleType() {
        super(true, SweepingStrikeParticleOption.DESERIALIZER);
    }

    @Override
    public Codec<SweepingStrikeParticleOption> codec() {
        return SweepingStrikeParticleOption.CODEC;
    }
}
