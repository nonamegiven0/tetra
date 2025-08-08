package se.mickelus.tetra;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import se.mickelus.tetra.advancements.BlockInteractionCriterion;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.advancements.ImprovementCraftCriterion;
import se.mickelus.tetra.advancements.ModuleCraftCriterion;
import se.mickelus.tetra.blocks.InitializableBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.blocks.forged.ForgedCrateBlock;
import se.mickelus.tetra.blocks.forged.ForgedPillarBlock;
import se.mickelus.tetra.blocks.forged.ForgedPlatformBlock;
import se.mickelus.tetra.blocks.forged.ForgedPlatformSlabBlock;
import se.mickelus.tetra.blocks.forged.ForgedVentBlock;
import se.mickelus.tetra.blocks.forged.ForgedWallBlock;
import se.mickelus.tetra.blocks.forged.ForgedWorkbenchBlock;
import se.mickelus.tetra.blocks.forged.chthonic.ChthonicExtractorBlock;
import se.mickelus.tetra.blocks.forged.chthonic.ChthonicExtractorTile;
import se.mickelus.tetra.blocks.forged.chthonic.DepletedBedrockBlock;
import se.mickelus.tetra.blocks.forged.chthonic.ExtractorProjectileEntity;
import se.mickelus.tetra.blocks.forged.chthonic.FracturedBedrockBlock;
import se.mickelus.tetra.blocks.forged.chthonic.FracturedBedrockTile;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerBlock;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerBlockEntity;
import se.mickelus.tetra.blocks.forged.container.ForgedContainerMenu;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorBaseBlock;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorBaseBlockEntity;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorPipeBlock;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorPistonBlock;
import se.mickelus.tetra.blocks.forged.extractor.CoreExtractorPistonBlockEntity;
import se.mickelus.tetra.blocks.forged.extractor.SeepingBedrockBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlockEntity;
import se.mickelus.tetra.blocks.forged.hammer.HammerHeadBlock;
import se.mickelus.tetra.blocks.forged.hammer.HammerHeadBlockEntity;
import se.mickelus.tetra.blocks.forged.transfer.TransferUnitBlock;
import se.mickelus.tetra.blocks.forged.transfer.TransferUnitBlockEntity;
import se.mickelus.tetra.blocks.geode.GeodeBlock;
import se.mickelus.tetra.blocks.geode.GeodeItem;
import se.mickelus.tetra.blocks.geode.PristineAmethystItem;
import se.mickelus.tetra.blocks.geode.PristineDiamondItem;
import se.mickelus.tetra.blocks.geode.PristineEmeraldItem;
import se.mickelus.tetra.blocks.geode.PristineLapisItem;
import se.mickelus.tetra.blocks.geode.PristineQuartzItem;
import se.mickelus.tetra.blocks.geode.particle.SparkleParticleType;
import se.mickelus.tetra.blocks.holo.HolosphereBlock;
import se.mickelus.tetra.blocks.holo.HolosphereBlockEntity;
import se.mickelus.tetra.blocks.multischematic.MultiblockSchematicBlock;
import se.mickelus.tetra.blocks.rack.RackBlock;
import se.mickelus.tetra.blocks.rack.RackTile;
import se.mickelus.tetra.blocks.salvage.InteractiveBlockOverlay;
import se.mickelus.tetra.blocks.scroll.OpenScrollBlock;
import se.mickelus.tetra.blocks.scroll.RolledScrollBlock;
import se.mickelus.tetra.blocks.scroll.ScrollItem;
import se.mickelus.tetra.blocks.scroll.ScrollTile;
import se.mickelus.tetra.blocks.scroll.WallScrollBlock;
import se.mickelus.tetra.blocks.workbench.BasicWorkbenchBlock;
import se.mickelus.tetra.blocks.workbench.WorkbenchContainer;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.client.particle.SweepingStrikeParticleType;
import se.mickelus.tetra.effect.howling.HowlingPotionEffect;
import se.mickelus.tetra.effect.potion.BleedingPotionEffect;
import se.mickelus.tetra.effect.potion.EarthboundPotionEffect;
import se.mickelus.tetra.effect.potion.ExhaustedPotionEffect;
import se.mickelus.tetra.effect.potion.MiningSpeedPotionEffect;
import se.mickelus.tetra.effect.potion.PriedPotionEffect;
import se.mickelus.tetra.effect.potion.PuncturedPotionEffect;
import se.mickelus.tetra.effect.potion.SeveredPotionEffect;
import se.mickelus.tetra.effect.potion.SmallAbsorbPotionEffect;
import se.mickelus.tetra.effect.potion.SmallHealthPotionEffect;
import se.mickelus.tetra.effect.potion.SmallStrengthPotionEffect;
import se.mickelus.tetra.effect.potion.SteeledPotionEffect;
import se.mickelus.tetra.effect.potion.StunPotionEffect;
import se.mickelus.tetra.effect.potion.UnwaveringPotionEffect;
import se.mickelus.tetra.gui.stats.sorting.StatSorters;
import se.mickelus.tetra.items.InitializableItem;
import se.mickelus.tetra.items.cell.ThermalCellItem;
import se.mickelus.tetra.items.forged.BeamItem;
import se.mickelus.tetra.items.forged.BoltItem;
import se.mickelus.tetra.items.forged.CombustionChamberItem;
import se.mickelus.tetra.items.forged.EarthpiercerItem;
import se.mickelus.tetra.items.forged.InsulatedPlateItem;
import se.mickelus.tetra.items.forged.LubricantDispenserItem;
import se.mickelus.tetra.items.forged.MeshItem;
import se.mickelus.tetra.items.forged.MetalScrapItem;
import se.mickelus.tetra.items.forged.PlanarStabilizerItem;
import se.mickelus.tetra.items.forged.QuickLatchItem;
import se.mickelus.tetra.items.forged.StonecutterItem;
import se.mickelus.tetra.items.loot.DragonSinewItem;
import se.mickelus.tetra.items.modular.ThrownModularItemEntity;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ShootableDummyItem;
import se.mickelus.tetra.items.modular.impl.dynamic.DynamicModularItem;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ModularToolbeltItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltContainer;
import se.mickelus.tetra.items.modular.impl.toolbelt.suspend.SuspendPotionEffect;
import se.mickelus.tetra.levelgen.ForgedContainerProcessor;
import se.mickelus.tetra.levelgen.ForgedCrateProcessor;
import se.mickelus.tetra.levelgen.ForgedHammerProcessor;
import se.mickelus.tetra.levelgen.MultiblockSchematicProcessor;
import se.mickelus.tetra.levelgen.TransferUnitProcessor;
import se.mickelus.tetra.loot.FortuneBonusCondition;
import se.mickelus.tetra.loot.ReplaceTableModifier;
import se.mickelus.tetra.loot.ScrollDataFunction;

public class TetraRegistries {
	public static final DeferredRegister<Block> blocks = DeferredRegister.create(BuiltInRegistries.BLOCK,
			TetraMod.MOD_ID);
	public static final DeferredRegister<Item> items = DeferredRegister.create(BuiltInRegistries.ITEM, TetraMod.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> blockEntities = DeferredRegister
			.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TetraMod.MOD_ID);
	public static final DeferredRegister<MenuType<?>> containers = DeferredRegister.create(BuiltInRegistries.MENU,
			TetraMod.MOD_ID);
	public static final DeferredRegister<EntityType<?>> entities = DeferredRegister
			.create(BuiltInRegistries.ENTITY_TYPE, TetraMod.MOD_ID);
	public static final DeferredRegister<ParticleType<?>> particles = DeferredRegister
			.create(BuiltInRegistries.PARTICLE_TYPE, TetraMod.MOD_ID);
	public static final DeferredRegister<MobEffect> effects = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT,
			TetraMod.MOD_ID);
	public static final DeferredRegister<AttachmentType<?>> attachments = DeferredRegister
			.create(NeoForgeRegistries.ATTACHMENT_TYPES, TetraMod.MOD_ID);
	public static final DeferredRegister<SoundEvent> sounds = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT,
			TetraMod.MOD_ID);
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> lootModifiers = DeferredRegister
			.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TetraMod.MOD_ID);

	public static final DeferredRegister<LootItemConditionType> lootConditions = DeferredRegister
			.create(Registries.LOOT_CONDITION_TYPE, TetraMod.MOD_ID);
	public static final DeferredRegister<LootItemFunctionType<?>> lootFunctions = DeferredRegister
			.create(Registries.LOOT_FUNCTION_TYPE, TetraMod.MOD_ID);
	public static final DeferredRegister<StructureProcessorType<?>> structureProcessors = DeferredRegister
			.create(Registries.STRUCTURE_PROCESSOR, TetraMod.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> creativeTabs = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, TetraMod.MOD_ID);

	public static final TagKey<Block> forgeHammerBreakTag = BlockTags
			.create(ResourceLocation.parse("tetra:needs_forge_hammer_tool"));
//    public static final Tier forgeHammerTier = TierSortingRegistry.registerTier(new SimpleTier(Tiers.NETHERITE.getLevel() + 1, 0, 0, 0, 0,
//            forgeHammerBreakTag, () -> Ingredient.EMPTY), ResourceLocation.parse("tetra:maxed_forge_hammer"), List.of(Tiers.NETHERITE), List.of());
	public static final Tier forgeHammerTier = new SimpleTier(forgeHammerBreakTag, Tiers.NETHERITE.getUses() + 1, 0, 0f,
			0, () -> Ingredient.EMPTY);

	private static Item.Properties itemProperties;
	public static DeferredHolder<CreativeModeTab, CreativeModeTab> defaultCreativeTabs;
	public static DeferredHolder<Block, BasicWorkbenchBlock> basicWorkbench;
	public static DeferredHolder<Block, SeepingBedrockBlock> seepingBedrock;
	public static DeferredHolder<Block, RackBlock> rack;
	public static DeferredHolder<Block, ChthonicExtractorBlock> chthonicExtractor;
	public static DeferredHolder<Item, BlockItem> chthonicExtractorItem;
	public static DeferredHolder<Block, FracturedBedrockBlock> fracturedBedrock;
	public static DeferredHolder<Block, ForgedWallBlock> forgedWall;
	public static DeferredHolder<Block, ForgedPillarBlock> forgedPillar;
	public static DeferredHolder<Block, ForgedPlatformBlock> forgedPlatform;
	public static DeferredHolder<Block, ForgedPlatformSlabBlock> forgedPlatformSlab;
	public static DeferredHolder<Block, ForgedVentBlock> forgedVent;
	public static DeferredHolder<Block, HammerBaseBlock> forgeHammer;
	public static DeferredHolder<Block, ForgedWorkbenchBlock> forgedWorkbench;
	public static DeferredHolder<Block, ForgedCrateBlock> forgedCrate;
	public static DeferredHolder<Block, TransferUnitBlock> transferUnit;
	public static DeferredHolder<Block, OpenScrollBlock> openScroll;
	public static DeferredHolder<Block, WallScrollBlock> wallScroll;
	public static DeferredHolder<Block, RolledScrollBlock> rolledScroll;
	public static DeferredHolder<Item, BoltItem> bolt;
	public static DeferredHolder<Item, DragonSinewItem> dragonSinew;
	public static DeferredHolder<Item, StonecutterItem> stonecutter;
	public static DeferredHolder<Item, EarthpiercerItem> earthpiercer;
	public static DeferredHolder<Item, ModularHolosphereItem> modularHolosphere;
	public static DeferredHolder<Item, PlanarStabilizerItem> planarStabilizer;
	public static DeferredHolder<Item, InsulatedPlateItem> insulatedPlate;
	public static DeferredHolder<Item, QuickLatchItem> quickLatch;
	public static DeferredHolder<Item, MeshItem> mesh;
	public static DeferredHolder<Item, BeamItem> beam;
	public static DeferredHolder<Item, PristineDiamondItem> pristineDiamond;
	public static DeferredHolder<Item, PristineEmeraldItem> pristineEmerald;
	public static DeferredHolder<Item, PristineLapisItem> pristineLapis;
	public static DeferredHolder<Item, PristineAmethystItem> pristineAmethyst;
	public static DeferredHolder<Item, PristineQuartzItem> pristineQuartz;
	public static DeferredHolder<Item, GeodeItem> geode;
	
	public static DeferredHolder<AttachmentType<?>, AttachmentType<ItemStackHandler>> stackHandlerAttachment;
	public static DeferredHolder<AttachmentType<?>, AttachmentType<ItemStackHandler>> rackAttachment;

	public static void init(IEventBus bus) {
		bus.register(TetraRegistries.class);

		blocks.register(bus);
		items.register(bus);
		blockEntities.register(bus);
		entities.register(bus);
		particles.register(bus);
		containers.register(bus);
		effects.register(bus);
		attachments.register(bus);
		sounds.register(bus);
		lootConditions.register(bus);
		lootFunctions.register(bus);
//        lootModifiers.register(bus);
		structureProcessors.register(bus);
		creativeTabs.register(bus);

		itemProperties = new Item.Properties();

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// CREATIVE TABS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		defaultCreativeTabs = TetraRegistries.creativeTabs.register("default",
				() -> CreativeModeTab.builder().icon(() -> new ItemStack(GeodeItem.instance))
						.title(Component.translatable("itemGroup.tetra")).build());

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// BLOCKS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// crafting
		basicWorkbench = blocks.register(BasicWorkbenchBlock.identifier, BasicWorkbenchBlock::new);
		registerBlockItem(basicWorkbench);
		HolosphereBlock.instance = blocks.register(HolosphereBlock.identifier, HolosphereBlock::new);
		rack = blocks.register(RackBlock.identifier, RackBlock::new);
		registerBlockItem(rack);

		// scrolls
		rolledScroll = blocks.register(RolledScrollBlock.identifier, RolledScrollBlock::new);
		wallScroll = blocks.register(WallScrollBlock.identifier, WallScrollBlock::new);
		openScroll = blocks.register(OpenScrollBlock.identifier, OpenScrollBlock::new);

		// base ruins
		forgedWall = blocks.register(ForgedWallBlock.identifier, ForgedWallBlock::new);
		registerBlockItem(forgedWall);
		forgedPillar = blocks.register(ForgedPillarBlock.identifier, ForgedPillarBlock::new);
		registerBlockItem(forgedPillar);
		forgedPlatform = blocks.register(ForgedPlatformBlock.identifier, ForgedPlatformBlock::new);
		registerBlockItem(forgedPlatform);
		forgedPlatformSlab = blocks.register(ForgedPlatformSlabBlock.identifier, ForgedPlatformSlabBlock::new);
		registerBlockItem(forgedPlatformSlab);
		forgedVent = blocks.register(ForgedVentBlock.identifier, ForgedVentBlock::new);
		registerBlockItem(forgedVent);
		HammerHeadBlock.instance = blocks.register(HammerHeadBlock.identifier, HammerHeadBlock::new);
		forgeHammer = blocks.register(HammerBaseBlock.identifier, HammerBaseBlock::new);
		registerBlockItem(forgeHammer);
		forgedWorkbench = blocks.register(ForgedWorkbenchBlock.identifier, ForgedWorkbenchBlock::new);
		registerBlockItem(forgedWorkbench);
		ForgedContainerBlock.instance = blocks.register(ForgedContainerBlock.identifier, ForgedContainerBlock::new);
		registerBlockItem(ForgedContainerBlock.instance);
		forgedCrate = blocks.register(ForgedCrateBlock.identifier, () -> new ForgedCrateBlock());
		registerBlockItem(forgedCrate);
		transferUnit = blocks.register(TransferUnitBlock.identifier, TransferUnitBlock::new);
		registerBlockItem(transferUnit);

		// chthonic extractor
		chthonicExtractor = blocks.register(ChthonicExtractorBlock.identifier, ChthonicExtractorBlock::new);
		chthonicExtractorItem = ChthonicExtractorBlock.registerItems(items);
		fracturedBedrock = blocks.register(FracturedBedrockBlock.identifier, FracturedBedrockBlock::new);
		DepletedBedrockBlock.instance = blocks.register(DepletedBedrockBlock.identifier, DepletedBedrockBlock::new);

		// thermal extractor
		CoreExtractorBaseBlock.instance = blocks.register(CoreExtractorBaseBlock.identifier,
				CoreExtractorBaseBlock::new);
		registerBlockItem(CoreExtractorBaseBlock.instance);
		CoreExtractorPistonBlock.instance = blocks.register(CoreExtractorPistonBlock.identifier,
				CoreExtractorPistonBlock::new);
		CoreExtractorPipeBlock.instance = blocks.register(CoreExtractorPipeBlock.identifier,
				CoreExtractorPipeBlock::new);
		registerBlockItem(CoreExtractorPipeBlock.instance);
		seepingBedrock = blocks.register(SeepingBedrockBlock.identifier, SeepingBedrockBlock::new);
		registerBlockItem(seepingBedrock);

		// multiblock schematics
		new MultiblockSchematicBlock.Builder("stonecutter", 3, 2, ForgedBlockCommon.propertiesSolid).build(blocks,
				items);

		new MultiblockSchematicBlock.Builder("earthpiercer", 2, 2, ForgedBlockCommon.propertiesSolid).build(blocks,
				items);

		new MultiblockSchematicBlock.Builder("extractor", 3, 3, ForgedBlockCommon.propertiesSolid).build(blocks, items);

		// misc
		GeodeBlock.instance = blocks.register(GeodeBlock.identifier, GeodeBlock::new);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ITEMS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// modular items
		ModularBladedItem.instance = items.register(ModularBladedItem.identifier, ModularBladedItem::new);
		ModularDoubleHeadedItem.instance = items.register(ModularDoubleHeadedItem.identifier,
				ModularDoubleHeadedItem::new);
		ModularBowItem.instance = items.register(ModularBowItem.identifier, ModularBowItem::new);
		DeferredHolder<Item, ShootableDummyItem> shootableDummy = items.register(ShootableDummyItem.identifier,
				ShootableDummyItem::new);
		ModularCrossbowItem.instance = items.register(ModularCrossbowItem.identifier,
				() -> new ModularCrossbowItem(shootableDummy.get()));
		ModularSingleHeadedItem.instance = items.register(ModularSingleHeadedItem.identifier,
				ModularSingleHeadedItem::new);
		ModularShieldItem.instance = items.register(ModularShieldItem.identifier, ModularShieldItem::new);
		ModularToolbeltItem.instance = items.register(ModularToolbeltItem.identifier, ModularToolbeltItem::new);
		modularHolosphere = items.register(ModularHolosphereItem.identifier, ModularHolosphereItem::new);
		items.register(DynamicModularItem.identifier, DynamicModularItem::new);

		// random loot
		geode = items.register(GeodeItem.identifier, GeodeItem::new);
		pristineLapis = items.register(PristineLapisItem.identifier, PristineLapisItem::new);
		pristineEmerald = items.register(PristineEmeraldItem.identifier, PristineEmeraldItem::new);
		pristineDiamond = items.register(PristineDiamondItem.identifier, PristineDiamondItem::new);
		pristineAmethyst = items.register(PristineAmethystItem.identifier, PristineAmethystItem::new);
		pristineQuartz = items.register(PristineQuartzItem.identifier, PristineQuartzItem::new);
		dragonSinew = items.register(DragonSinewItem.identifier, DragonSinewItem::new);

		// ruins loot
		bolt = items.register(BoltItem.identifier, BoltItem::new);
		beam = items.register(BeamItem.identifier, BeamItem::new);
		mesh = items.register(MeshItem.identifier, MeshItem::new);
		quickLatch = items.register(QuickLatchItem.identifier, QuickLatchItem::new);
		MetalScrapItem.instance = items.register(MetalScrapItem.identifier, MetalScrapItem::new);
		insulatedPlate = items.register(InsulatedPlateItem.identifier, InsulatedPlateItem::new);
		planarStabilizer = items.register(PlanarStabilizerItem.identifier, PlanarStabilizerItem::new);
		ThermalCellItem.instance = items.register(ThermalCellItem.identifier, ThermalCellItem::new);
		CombustionChamberItem.instance = items.register(CombustionChamberItem.identifier, CombustionChamberItem::new);
		LubricantDispenserItem.instance = items.register(LubricantDispenserItem.identifier,
				LubricantDispenserItem::new);
		earthpiercer = items.register(EarthpiercerItem.identifier, EarthpiercerItem::new);
		stonecutter = items.register(StonecutterItem.identifier, StonecutterItem::new);

		ScrollItem.instance = items.register(ScrollItem.identifier, () -> new ScrollItem(rolledScroll.get()));

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// BLOCK ENTITIES
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		WorkbenchTile.type = blockEntities.register(WorkbenchTile.identifier, () -> BlockEntityType.Builder
				.of(WorkbenchTile::new, basicWorkbench.get(), forgedWorkbench.get()).build(null));
		ChthonicExtractorTile.type = blockEntities.register(ChthonicExtractorBlock.identifier,
				() -> BlockEntityType.Builder.of(ChthonicExtractorTile::new, chthonicExtractor.get()).build(null));
		FracturedBedrockTile.type = blockEntities.register(FracturedBedrockBlock.identifier,
				() -> BlockEntityType.Builder.of(FracturedBedrockTile::new, fracturedBedrock.get()).build(null));
		RackTile.type = blockEntities.register(RackBlock.identifier,
				() -> BlockEntityType.Builder.of(RackTile::new, rack.get()).build(null));
		ScrollTile.type = blockEntities.register(ScrollTile.identifier, () -> BlockEntityType.Builder
				.of(ScrollTile::new, openScroll.get(), wallScroll.get(), rolledScroll.get()).build(null));

		HammerBaseBlockEntity.type = blockEntities.register(HammerBaseBlock.identifier, () -> BlockEntityType.Builder
				.of(HammerBaseBlockEntity::new, HammerBaseBlock.instance.get()).build(null));
		HammerHeadBlockEntity.type = blockEntities.register(HammerHeadBlock.identifier, () -> BlockEntityType.Builder
				.of(HammerHeadBlockEntity::new, HammerHeadBlock.instance.get()).build(null));
		TransferUnitBlockEntity.type = blockEntities.register(TransferUnitBlock.identifier,
				() -> BlockEntityType.Builder.of(TransferUnitBlockEntity::new, transferUnit.get()).build(null));
		CoreExtractorBaseBlockEntity.type = blockEntities.register(CoreExtractorBaseBlock.identifier,
				() -> BlockEntityType.Builder
						.of(CoreExtractorBaseBlockEntity::new, CoreExtractorBaseBlock.instance.get()).build(null));
		CoreExtractorPistonBlockEntity.type = blockEntities.register(CoreExtractorPistonBlock.identifier,
				() -> BlockEntityType.Builder
						.of(CoreExtractorPistonBlockEntity::new, CoreExtractorPistonBlock.instance.get()).build(null));
		ForgedContainerBlockEntity.type = blockEntities.register(ForgedContainerBlock.identifier,
				() -> BlockEntityType.Builder.of(ForgedContainerBlockEntity::new, ForgedContainerBlock.instance.get())
						.build(null));
		HolosphereBlockEntity.type = blockEntities.register(HolosphereBlock.identifier, () -> BlockEntityType.Builder
				.of(HolosphereBlockEntity::new, HolosphereBlock.instance.get()).build(null));

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ENTITIES
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ThrownModularItemEntity.type = entities.register(ThrownModularItemEntity.unlocalizedName,
				() -> EntityType.Builder.<ThrownModularItemEntity>of(ThrownModularItemEntity::new, MobCategory.MISC)
//                        .setCustomClientFactory(ThrownModularItemEntity::new)
						.sized(0.5F, 0.5F).build(ThrownModularItemEntity.unlocalizedName));

		ExtractorProjectileEntity.type = entities.register(ExtractorProjectileEntity.unlocalizedName,
				() -> EntityType.Builder.<ExtractorProjectileEntity>of(ExtractorProjectileEntity::new, MobCategory.MISC)
//                        .setCustomClientFactory(ExtractorProjectileEntity::new)
						.sized(0.5F, 0.5F).build(ExtractorProjectileEntity.unlocalizedName));

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// PARTICLES
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		SparkleParticleType.instance = particles.register(SparkleParticleType.identifier,
				() -> new SimpleParticleType(false));
		SweepingStrikeParticleType.instance = particles.register(SweepingStrikeParticleType.identifier,
				SweepingStrikeParticleType::new);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// CONTAINERS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// toolbelt
		ToolbeltContainer.type = containers.register(ModularToolbeltItem.identifier,
				() -> IMenuTypeExtension.create(((windowId, inv, data) -> ToolbeltContainer.create(windowId, inv))));

		// workbench
		WorkbenchContainer.containerType = containers.register(WorkbenchTile.identifier, () -> IMenuTypeExtension
				.create(((windowId, inv, data) -> WorkbenchContainer.create(windowId, data.readBlockPos(), inv))));

		// forged container
		ForgedContainerMenu.type = containers.register(ForgedContainerBlock.identifier, () -> IMenuTypeExtension
				.create(((windowId, inv, data) -> ForgedContainerMenu.create(windowId, data.readBlockPos(), inv))));

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// EFFECTS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		effects.register(BleedingPotionEffect.identifier, BleedingPotionEffect::new);
		effects.register(EarthboundPotionEffect.identifier, EarthboundPotionEffect::new);
		effects.register(StunPotionEffect.identifier, StunPotionEffect::new);
		effects.register(HowlingPotionEffect.identifier, HowlingPotionEffect::new);
		effects.register(SeveredPotionEffect.identifier, SeveredPotionEffect::new);
		effects.register(PuncturedPotionEffect.identifier, PuncturedPotionEffect::new);
		effects.register(PriedPotionEffect.identifier, PriedPotionEffect::new);
		effects.register(ExhaustedPotionEffect.identifier, ExhaustedPotionEffect::new);
		effects.register(SteeledPotionEffect.identifier, SteeledPotionEffect::new);
		effects.register(SmallStrengthPotionEffect.identifier, SmallStrengthPotionEffect::new);
		effects.register(UnwaveringPotionEffect.identifier, UnwaveringPotionEffect::new);
		effects.register(SmallHealthPotionEffect.identifier, SmallHealthPotionEffect::new);
		effects.register(SmallAbsorbPotionEffect.identifier, SmallAbsorbPotionEffect::new);
		effects.register(SuspendPotionEffect.identifier, SuspendPotionEffect::new);
		effects.register(MiningSpeedPotionEffect.identifier, MiningSpeedPotionEffect::new);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// DATA ATTACHMENTS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		stackHandlerAttachment = attachments.register("item_stack_handler",
				() -> AttachmentType.serializable(() -> (ItemStackHandler)null).build());

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// SOUNDS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		sounds.register(TetraSounds.scanHit.getLocation().getPath(), () -> TetraSounds.scanHit);
		sounds.register(TetraSounds.scanMiss.getLocation().getPath(), () -> TetraSounds.scanMiss);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// LOOT CONDITIONS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		FortuneBonusCondition.type = lootConditions.register(FortuneBonusCondition.identifier,
				() -> new LootItemConditionType(new FortuneBonusCondition.ConditionSerializer()));

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// LOOT FUNCTIONS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ScrollDataFunction.type = lootFunctions.register(ScrollDataFunction.identifier,
				() -> new LootItemFunctionType(new ScrollDataFunction.Serializer()));

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// LOOT MODIFIERS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		lootModifiers.register("replace_table", ReplaceTableModifier.codec);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// STRUCTURE PROCESSORS
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ForgedHammerProcessor.type = registerStructureProcessor("hammer", () -> ForgedHammerProcessor.codec);
		ForgedCrateProcessor.type = registerStructureProcessor("crate", () -> ForgedCrateProcessor.codec);
		ForgedContainerProcessor.type = registerStructureProcessor("container", () -> ForgedContainerProcessor.codec);
		TransferUnitProcessor.type = registerStructureProcessor("transfer_unit", () -> TransferUnitProcessor.codec);
		MultiblockSchematicProcessor.type = registerStructureProcessor("multiblock_schematic",
				() -> MultiblockSchematicProcessor.codec);

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// INGREDIENT SERIALIZERS TODO
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        CraftingHelper.register(ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "scroll"), ScrollIngredient.Serializer.instance);
//        CraftingHelper.register(ResourceLocation.fromNamespaceAndPath(TetraMod.MOD_ID, "tool_action"), ItemAbilityIngredient.Serializer.instance);
	}

	public static <B extends Block> DeferredHolder<Item, BlockItem> registerBlockItem(DeferredHolder<Block, B> block) {
		return items.register(block.getId().getPath(), () -> new BlockItem(block.get(), itemProperties));
	}

	public static <P extends StructureProcessor> Supplier<StructureProcessorType<?>> registerStructureProcessor(
			String id, StructureProcessorType<P> type) {
		return structureProcessors.register(id, () -> type);
	}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			// enqueueWork swallows exceptions without logging
			try {
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// ADVANCEMENT CRITERIA
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				CriteriaTriggers.register("block_use", BlockUseCriterion.trigger);
				CriteriaTriggers.register("block_interaction", BlockInteractionCriterion.trigger);
				CriteriaTriggers.register("craft_module", ModuleCraftCriterion.trigger);
				CriteriaTriggers.register("craft_improvement", ImprovementCraftCriterion.trigger);

				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// ITEM PREDICATES TODO
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                ItemPredicate.register(ResourceLocation.parse("tetra:modular_item"), ItemPredicateModular::new);
//                ItemPredicate.register(ResourceLocation.parse("tetra:item_effect"), EffectItemPredicate::new);
//                ItemPredicate.register(ResourceLocation.parse("tetra:material"), MaterialItemPredicate::new);
//                ItemPredicate.register(ResourceLocation.parse("tetra:loose"), LooseItemPredicate::new);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		blocks.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof InitializableBlock)
				.map(block -> (InitializableBlock) block).forEach(block -> block.commonInit(TetraMod.packetHandler));
		items.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof InitializableItem)
				.map(item -> (InitializableItem) item).forEach(item -> item.commonInit(TetraMod.packetHandler));
	}

	@SubscribeEvent
	public static void buildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == defaultCreativeTabs.getKey()) {
			event.accept(basicWorkbench.get());
			event.accept(ModularHolosphereItem.getCreativeItemStack());
			event.accept(rack.get());
			event.acceptAll(ModularDoubleHeadedItem.getCreativeTabItemStacks());
			event.acceptAll(ModularBladedItem.getCreativeTabItemStacks());
			event.acceptAll(ModularToolbeltItem.getCreativeTabItemStacks());

			event.accept(geode.get());
			event.accept(pristineLapis.get());
			event.accept(pristineEmerald.get());
			event.accept(pristineDiamond.get());
			event.accept(pristineAmethyst.get());
//            event.accept(pristineQuartz);
			event.accept(dragonSinew.get());

			event.acceptAll(ScrollItem.instance.get().getCreativeTabItems());

			event.accept(bolt.get());
			event.accept(beam.get());
			event.accept(mesh.get());
			event.accept(quickLatch.get());
			event.accept(MetalScrapItem.instance.get());
			event.accept(insulatedPlate.get());
			event.accept(planarStabilizer.get());
			event.accept(CombustionChamberItem.instance.get());
			event.accept(LubricantDispenserItem.instance.get());
			event.accept(ThermalCellItem.instance.get());
			event.accept(earthpiercer.get());
			event.accept(stonecutter.get());
			event.accept(chthonicExtractorItem.get());
			event.accept(forgedWall.get());
			event.accept(forgedPillar.get());
			event.accept(forgedPlatform.get());
			event.accept(forgedPlatformSlab.get());
			event.accept(forgedVent.get());
			event.accept(forgeHammer.get());
			event.accept(forgedWorkbench.get());
			event.accept(ForgedContainerBlock.instance.get());
			event.accept(forgedCrate.get());
			event.accept(transferUnit.get());
			event.accept(CoreExtractorBaseBlock.instance.get());
			event.accept(CoreExtractorPipeBlock.instance.get());
			event.accept(seepingBedrock.get());
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			// enqueueWork swallows exceptions without logging
			try {
				blocks.getEntries().stream().map(DeferredHolder::get)
						.filter(block -> block instanceof InitializableBlock).map(block -> (InitializableBlock) block)
						.forEach(InitializableBlock::clientInit);
				items.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof InitializableItem)
						.map(item -> (InitializableItem) item).forEach(InitializableItem::clientInit);

				NeoForge.EVENT_BUS.register(new InteractiveBlockOverlay());
//                MinecraftForge.EVENT_BUS.register(MultiblockSchematicScrollHandler.class);

				HoloStatsGui.initializeStaticBars();
				WorkbenchStatsGui.initializeStaticBars();
				StatSorters.initializeStaticSorters();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
