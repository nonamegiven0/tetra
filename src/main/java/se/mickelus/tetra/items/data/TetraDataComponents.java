package se.mickelus.tetra.items.data;

import com.mojang.serialization.Codec;

import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import se.mickelus.tetra.TetraMod;

public class TetraDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister
	    .createDataComponents(Registries.DATA_COMPONENT_TYPE, TetraMod.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TagKey<Structure>>> FORGED_RUINS_MARKER = REGISTRAR
	    .registerComponentType("forged_ruins_marker",
		    builder -> builder.persistent(TagKey.hashedCodec(Registries.STRUCTURE)));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemModuleDataComponent>> ITEM_MODULE = REGISTRAR
	    .registerComponentType("item_module_slot_tag",
		    builder -> builder.persistent(ItemModuleDataComponent.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ModularItemDataComponent>> MODULAR_ITEM = REGISTRAR
	    .registerComponentType("modular_item", builder -> builder.persistent(ModularItemDataComponent.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BoosterItemDataComponent>> BOOSTER_ITEM = REGISTRAR
	    .registerComponentType("booster_item", builder -> builder.persistent(BoosterItemDataComponent.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ARCHETYPE = REGISTRAR
	    .registerComponentType("archetype", builder -> builder.persistent(Codec.STRING));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> TOOLBELT_INV = REGISTRAR
	    .registerComponentType("toolbelt", builder -> builder.persistent(CompoundTag.CODEC));

    public static void register(IEventBus bus) {
	REGISTRAR.register(bus);
    }
}
