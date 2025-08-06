package se.mickelus.tetra.blocks.geode.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SparkleParticleType {
    public static final String identifier = "sparkle";

    public static DeferredHolder<ParticleType<?>, SimpleParticleType> instance;
}
