package se.mickelus.tetra.blocks.scroll;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import se.mickelus.tetra.TetraMod;

@ParametersAreNonnullByDefault
public class ScrollDrops {
    Map<ResourceKey<LootTable>, ResourceKey<LootTable>> basicExtensions;

    public ScrollDrops() {
        basicExtensions = new HashMap<>();
        basicExtensions.put(BuiltInLootTables.BASTION_BRIDGE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "bastion_scrolls")));
        basicExtensions.put(BuiltInLootTables.BASTION_HOGLIN_STABLE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "bastion_scrolls")));
        basicExtensions.put(BuiltInLootTables.BASTION_OTHER, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "bastion_scrolls")));
        basicExtensions.put(BuiltInLootTables.BASTION_TREASURE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "bastion_scrolls")));
        basicExtensions.put(BuiltInLootTables.NETHER_BRIDGE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "chests/nether_bridge_extended")));
        basicExtensions.put(BuiltInLootTables.SIMPLE_DUNGEON, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "chests/simple_dungeon_extended")));
    }
    
    //TODO: verify functionality
    @SubscribeEvent
    public void onLootTableLoad(final LootTableLoadEvent event) {
        if (basicExtensions.containsKey(event.getKey())) {
            event.getTable().addPool(LootPool.lootPool()
                    .name(TetraMod.MOD_ID + ":" + event.getName().getPath() + "_extended")
                    .add(NestedLootTable.lootTableReference(basicExtensions.get(event.getKey()))).build());
        }
    }
}
