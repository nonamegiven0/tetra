package se.mickelus.tetra.items.loot;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class DragonSinewItem extends TetraItem {
    public static final String identifier = "dragon_sinew";
    static final Component tooltip = Component.translatable("item.tetra." + identifier + ".description")
            .withStyle(ChatFormatting.GRAY);
    private static final ResourceKey<LootTable> dragonLootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("entities/ender_dragon"));
    private static final ResourceKey<LootTable> sinewLootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "entities/ender_dragon_extended"));

    public DragonSinewItem() {
        super(new Properties());

        NeoForge.EVENT_BUS.register(new LootTableHandler());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(DragonSinewItem.tooltip);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level world, Entity entity, ItemStack itemstack) {
        entity.setNoGravity(true);

        return null;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.8f));
        if (entity.level().isClientSide && entity.getAge() % 20 == 0) {
            entity.level().addParticle(ParticleTypes.DRAGON_BREATH, entity.getRandomX(.2d), entity.getRandomY() + 0.2, entity.getRandomZ(0.2),
                    entity.level().getRandom().nextFloat() * 0.02f - 0.01f, -0.01f - entity.level().getRandom().nextFloat() * 0.01f, entity.level().getRandom().nextFloat() * 0.02f - 0.01f);
        }
        return false;
    }

    public static class LootTableHandler {
        @SubscribeEvent
        public void onLootTableLoad(final LootTableLoadEvent event) {
            if (event.getName().equals(dragonLootTable)) {
                event.getTable().addPool(LootPool.lootPool()
                        .name(TetraMod.MOD_ID + ":" + identifier)
                        .add(NestedLootTable.lootTableReference(sinewLootTable)).build());
            }
        }
    }
}
