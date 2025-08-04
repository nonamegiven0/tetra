package se.mickelus.tetra.gui.stats.getter;

import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;
import se.mickelus.tetra.TetraItemAbilities;
import se.mickelus.tetra.effect.ItemEffect;

public class StatGetterStriking extends StatGetterEffectLevel {
    public StatGetterStriking(ItemAbility ItemAbility) {
        super(getEffect(ItemAbility), 1);
    }

    static ItemEffect getEffect(ItemAbility ItemAbility) {
        if (ItemAbility == ItemAbilities.AXE_DIG) {
            return ItemEffect.strikingAxe;
        } else if (ItemAbility == ItemAbilities.PICKAXE_DIG) {
            return ItemEffect.strikingPickaxe;
        } else if (ItemAbility == TetraItemAbilities.cut) {
            return ItemEffect.strikingCut;
        } else if (ItemAbility == ItemAbilities.SHOVEL_DIG) {
            return ItemEffect.strikingShovel;
        } else if (ItemAbility == ItemAbilities.HOE_DIG) {
            return ItemEffect.strikingHoe;
        }
        return ItemEffect.strikingPickaxe;
    }
}
