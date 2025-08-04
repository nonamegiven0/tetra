package se.mickelus.tetra.loot;

import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

@ParametersAreNonnullByDefault
public class ReplaceTableModifier extends LootModifier {
  public static final Supplier<MapCodec<ReplaceTableModifier>> codec = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(instance -> instance.group(
  LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions),
  ResourceLocation.CODEC.fieldOf("table").forGetter(i -> i.table)
).apply(instance, ReplaceTableModifier::new)));

    public ResourceLocation table;

    protected ReplaceTableModifier(LootItemCondition[] conditions, ResourceLocation table) {
        super(conditions);

        this.table = table;
    }

    // todo 1.20 verify: bartering with piglins can grant all warforge schematics & can still get regular barter drops
    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootParams newParams = new LootParams.Builder(context.getLevel())
                .withLuck(context.getLuck())
                .create(LootContextParamSets.EMPTY);
        context.setQueriedLootTableId(table);

//        return context.getLevel()
//                .getServer()
//                .getLootData()
//                .getLootTable(table)
//                .getRandomItems(newParams);
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return codec.get();
    }
}
