package se.mickelus.tetra.data.provider;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import se.mickelus.tetra.blocks.multischematic.MultiblockSchematicBlock;
import se.mickelus.tetra.items.forged.MetalScrapItem;

public class MultiblockSchematicLootProvider extends BlockLootSubProvider {
    protected MultiblockSchematicLootProvider(Set<Item> pExplosionResistant, FeatureFlagSet pEnabledFeatures, HolderLookup.Provider registries) {
        super(pExplosionResistant, pEnabledFeatures, registries);
    }

    public static List<LootTableProvider.SubProviderEntry> getLootTables() {
        return ImmutableList.of(
                new LootTableProvider.SubProviderEntry(registries -> getMultiBlockSchematics("stonecutter", 3, 2, true), LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(registries -> getMultiBlockSchematics("earthpiercer", 2, 2, true), LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(registries -> getMultiBlockSchematics("extractor", 3, 3, true), LootContextParamSets.BLOCK)
        );
    }

    private static LootTableSubProvider getMultiBlockSchematics(String identifier, int width, int height, boolean ruinable) {
        return consumer -> {
            for (int h = 0; h < width; h++) {
                for (int v = 0; v < height; v++) {
                    String id = String.format(MultiblockSchematicBlock.Builder.format, identifier, h, v);
                    consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("tetra", MultiblockSchematicBlock.Builder.pryTablePrefix + id)),
                            getMultiBlockSchematicPryTable(id));

                    consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("tetra", "blocks/" + id)),
                            getMultiBlockSchematicDropTable(id));

                    if (ruinable) {
                        id = String.format(MultiblockSchematicBlock.Builder.ruinedFormat, identifier, h, v);
                        consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("tetra", MultiblockSchematicBlock.Builder.pryTablePrefix + id)),
                                getMultiBlockSchematicPryTable(id));

                        consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("tetra", "blocks/" + id)), getMultiBlockSchematicDropTable(id));
                    }
                }
            }
        };
    }

    private static LootTable.Builder getMultiBlockSchematicPryTable(String identifier) {
        ResourceLocation rl = ResourceLocation.fromNamespaceAndPath("tetra", identifier);
        return LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(BuiltInRegistries.BLOCK.get(rl))));
    }

    private static LootTable.Builder getMultiBlockSchematicDropTable(String identifier) {
        ResourceLocation rl = ResourceLocation.fromNamespaceAndPath("tetra", identifier);

        return createSilkTouchDispatchTable(BuiltInRegistries.BLOCK.get(rl),
                LootItem.lootTableItem(MetalScrapItem.instance.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)));
    }

    @Override
    protected void generate() {
    }
}
