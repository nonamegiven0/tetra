package se.mickelus.tetra.effect;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@ParametersAreNonnullByDefault
public class ArmorPenetrationEffect {

    private static final UUID uuid = UUID.fromString("a43e0407-f070-4e2f-8813-a5e16328f1a5");

    /**
     * Applies the armor reduction effect before the damage value is calculated.
     * Note that some mods cause this to be called twice before onLivingDamage.
     *
     * @param event
     * @param effectLevel
     */
    public static void onLivingDamage(LivingDamageEvent event, int effectLevel) {
        Optional.of(event.getEntity())
                .map(LivingEntity::getAttributes)
                .filter(manager -> manager.hasAttribute(Attributes.ARMOR))
                .map(manager -> manager.getInstance(Attributes.ARMOR))
                .filter(instance -> instance.getModifier(uuid) == null)
                .ifPresent(instance -> instance.addTransientModifier(
                        new AttributeModifier(uuid, "tetra_armor_pen", effectLevel * -0.01, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)));
    }

    public static void onLivingDamage(LivingDamageEvent event) {
        Optional.of(event.getEntity())
                .map(LivingEntity::getAttributes)
                .filter(manager -> manager.hasAttribute(Attributes.ARMOR))
                .map(manager -> manager.getInstance(Attributes.ARMOR))
                .ifPresent(instance -> instance.removeModifier(uuid));
    }
}
