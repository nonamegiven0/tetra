package se.mickelus.tetra.items.modular.impl.crossbow;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import se.mickelus.tetra.blocks.forged.chthonic.ChthonicExtractorBlock;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class ShootableDummyItem extends ProjectileWeaponItem {
    public static final String identifier = "shootable_dummy";

    public static final Predicate<ItemStack> ammoPredicate = ARROW_OR_FIREWORK
            .or(stack -> stack.getItem() == ChthonicExtractorBlock.item.get())
            .or(stack -> stack.getItem() == ChthonicExtractorBlock.usedItem.get());

    public ShootableDummyItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ammoPredicate;
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }

	@Override
	protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity,
			float inaccuracy, float angle, LivingEntity target) {
		//TODO: verify functionality
		projectile.shootFromRotation(shooter, 0, 0, 0, velocity, inaccuracy);
	}
}
