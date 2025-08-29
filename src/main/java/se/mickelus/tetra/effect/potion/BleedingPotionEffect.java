package se.mickelus.tetra.effect.potion;

import java.util.function.Consumer;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.tetra.TetraDamageTypes;
import se.mickelus.tetra.effect.gui.EffectUnRenderer;

@ParametersAreNonnullByDefault
public class BleedingPotionEffect extends MobEffect {
    public static final String identifier = "bleeding";
    public static DeferredHolder<MobEffect, BleedingPotionEffect> instance;

    public BleedingPotionEffect() {
        super(MobEffectCategory.HARMFUL, 0x880000);

//        instance = this;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // todo 1.20 verify: bleeding effect (serrated blades) deal damage properly
        DamageSource source = entity.level().damageSources().source(TetraDamageTypes.bleeding);
        entity.hurt(source, amplifier);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(EffectUnRenderer.INSTANCE);
    }
}
