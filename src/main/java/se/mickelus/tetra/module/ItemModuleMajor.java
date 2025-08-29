package se.mickelus.tetra.module;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Multimap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.items.data.ItemModuleDataComponent;
import se.mickelus.tetra.items.data.TetraDataComponents;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemColors;
import se.mickelus.tetra.module.data.AspectData;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ImprovementData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.module.data.TweakData;
import se.mickelus.tetra.module.improvement.SettlePacket;
import se.mickelus.tetra.properties.AttributeHelper;

//TODO: EnchantMentMapping code migration to DataComponent (blocked by EnchantmentHelper)
public abstract class ItemModuleMajor extends ItemModule {

	public static final String settleImprovement = "settled";
	public static final String arrestedImprovement = "arrested";
	protected ImprovementData[] improvements = new ImprovementData[0];
	protected int settleMax = 0;
	private String settleProgressKey = "/settle_progress";

	public ItemModuleMajor(String slotKey, String moduleKey) {
		super(slotKey, moduleKey);

		settleProgressKey = getSlot() + settleProgressKey;
	}

	public static void addImprovement(ItemStack itemStack, String slot, String improvement, int level) {
		IModularItem item = (IModularItem) itemStack.getItem();
		CastOptional.cast(item.getModuleFromSlot(itemStack, slot), ItemModuleMajor.class)
				.filter(module -> module.acceptsImprovementLevel(improvement, level))
				.ifPresent(module -> module.addImprovement(itemStack, improvement, level));
	}

	public static void removeImprovement(ItemStack itemStack, String slot, String improvement) {
//        if (itemStack.hasTag()) {
//            itemStack.getTag().remove(slot + ":" + improvement);
//        }
		// TODO: verify functionality
		if (itemStack.has(TetraDataComponents.ITEM_MODULE)) {
			ItemModuleDataComponent cmp = itemStack.get(TetraDataComponents.ITEM_MODULE);
			cmp.getModuleKeys().remove(slot + ":" + improvement);
			itemStack.set(TetraDataComponents.ITEM_MODULE, cmp);
		}
	}

	public void tickProgression(LivingEntity entity, ItemStack itemStack, int multiplier) {
		int settleMaxCount = getSettleMaxCount(itemStack);
		if (settleMaxCount == 0) {
			return;
		}

//        CompoundTag tag = itemStack.getOrCreateTag();
		ItemModuleDataComponent module = itemStack.get(TetraDataComponents.ITEM_MODULE);
		if (module != null) {
			int settleLevel = getImprovementLevel(itemStack, settleImprovement);

			if (settleLevel < settleMaxCount && (getImprovementLevel(itemStack, arrestedImprovement) == -1)) {
				int settleProgress = getSettleProgress(itemStack);

				settleProgress -= multiplier;
//				tag.putInt(settleProgressKey, settleProgress);
				module.setSettleProgress(settleProgressKey, settleProgress);
				if (settleProgress <= 0) {
					addImprovement(itemStack, settleImprovement, settleLevel == -1 ? 1 : settleLevel + 1);
//					tag.remove(settleProgressKey);
					module.setSettleProgress(settleProgressKey, null);

					if (entity instanceof ServerPlayer) {
						TetraMod.packetHandler.sendTo(new SettlePacket(itemStack, getSlot()), (ServerPlayer) entity);
//						IModularItem.updateIdentifier(tag);
						IModularItem.updateIdentifier(itemStack);
					}
				}
			}
		}
		itemStack.set(TetraDataComponents.ITEM_MODULE, module);
	}

	/**
	 * Returns the remaining number of times the item has to be used before this
	 * module will settle.
	 *
	 * @param itemStack The itemstack which the module is present on
	 * @return
	 */
	public int getSettleProgress(ItemStack itemStack) {
		return Optional.ofNullable(itemStack.get(TetraDataComponents.ITEM_MODULE))
				.filter(mod -> mod.getSettleProgress(settleProgressKey) != null)
				.map(tag -> tag.getSettleProgress(settleProgressKey)).orElseGet(() -> getSettleLimit(itemStack));
	}

	/**
	 * Returns the total number of times the item has to be used before this module
	 * will settle.
	 *
	 * @param itemStack The itemstack which the module is present on
	 * @return
	 */
	public int getSettleLimit(ItemStack itemStack) {
		return (int) ((ConfigHandler.settleLimitBase.get()
				+ getDurability(itemStack) * ConfigHandler.settleLimitDurabilityMultiplier.get())
				* Math.max(getImprovementLevel(itemStack, settleImprovement)
						* ConfigHandler.settleLimitLevelMultiplier.get(), 1f));
	}

	/**
	 * Returns the total number of times the item has to be used before this module
	 * will settle.
	 *
	 * @param itemStack The itemstack which the module is present on
	 * @return
	 */
	public int getSettleMaxCount(ItemStack itemStack) {
		if (settleMax == 0) {
			return 0;
		}

		int integrity = getVariantData(itemStack).integrity;
		if (integrity <= -4 || integrity >= 6) {
			return settleMax;
		} else if (integrity != 0) {
			return 1;
		}

		return 0;
	}

	protected void clearProgression(ItemStack itemStack) {
//		if (itemStack.hasTag()) {
//			itemStack.getTag().remove(String.format(settleProgressKey, getSlot()));
//		}
		ItemModuleDataComponent module = itemStack.get(TetraDataComponents.ITEM_MODULE);
		if (module != null) {
			module.setSettleProgress(settleProgressKey, null);
			itemStack.set(TetraDataComponents.ITEM_MODULE, module);
		}
	}

	public int getImprovementLevel(ItemStack itemStack, String improvementKey) {
//		return Optional.ofNullable(itemStack.getTag()).filter(tag -> tag.contains(slotTagKey + ":" + improvementKey))
//				.map(tag -> tag.getInt(slotTagKey + ":" + improvementKey)).orElse(-1);
		return Optional.ofNullable(itemStack.get(TetraDataComponents.ITEM_MODULE).improvementLevels())
				.filter(map -> map.containsKey(slotTagKey + ":" + improvementKey))
				.map(map -> map.get(slotTagKey + ":" + improvementKey)).orElse(-1);
	}

	public ImprovementData getImprovement(ItemStack itemStack, String improvementKey) {
//		if (itemStack.hasTag()) {
//			CompoundTag tag = itemStack.getTag();
//			return Arrays.stream(improvements).filter(improvement -> improvementKey.equals(improvement.key))
//					.filter(improvement -> tag.contains(slotTagKey + ":" + improvement.key))
//					.filter(improvement -> improvement.level == tag.getInt(slotTagKey + ":" + improvement.key))
//					.findAny().orElse(null);
//		}
		ItemModuleDataComponent module = itemStack.get(TetraDataComponents.ITEM_MODULE);
		if (module != null) {
			return Arrays.stream(improvements).filter(improvement -> improvementKey.equals(improvement.key))
					.filter(improvement -> module.improvementLevels().containsKey(slotTagKey + ":" + improvement.key))
					.filter(improvement -> improvement.level == module.improvementLevels()
							.get(slotTagKey + ":" + improvement.key))
					.findAny().orElse(null);
		}
		return null;
	}

	public ImprovementData[] getImprovements(ItemStack itemStack) {
//		if (itemStack.hasTag()) {
//			CompoundTag tag = itemStack.getTag();
//			return Arrays.stream(improvements).filter(improvement -> tag.contains(slotTagKey + ":" + improvement.key))
//					.filter(improvement -> improvement.level == tag.getInt(slotTagKey + ":" + improvement.key))
//					.toArray(ImprovementData[]::new);
//		}
		ItemModuleDataComponent module = itemStack.get(TetraDataComponents.ITEM_MODULE);
		if (module != null) {
			return Arrays.stream(improvements)
					.filter(improvement -> module.improvementLevels().containsKey(slotTagKey + ":" + improvement.key))
					.filter(improvement -> improvement.level == module.improvementLevels()
							.get(slotTagKey + ":" + improvement.key))
					.toArray(ImprovementData[]::new);
		}
		return new ImprovementData[0];
	}

	public boolean acceptsImprovement(String improvementKey) {
		return Arrays.stream(improvements).map(improvement -> improvement.key).anyMatch(improvementKey::equals);
	}

	public boolean acceptsImprovementLevel(String improvementKey, int level) {
		return Arrays.stream(improvements).filter(improvement -> improvementKey.equals(improvement.key))
				.anyMatch(improvement -> level == improvement.level);
	}

	public void addImprovement(ItemStack itemStack, String improvementKey, int level) {
		removeCollidingImprovements(itemStack, improvementKey, level);
//		itemStack.getOrCreateTag().putInt(slotTagKey + ":" + improvementKey, level);
		ItemModuleDataComponent mod = itemStack.get(TetraDataComponents.ITEM_MODULE);
		if (mod != null) {
			mod.improvementLevels().put(slotTagKey + ":" + improvementKey, level);
			itemStack.set(TetraDataComponents.ITEM_MODULE, mod);
		}
	}

	public void removeCollidingImprovements(ItemStack itemStack, String improvementKey, int level) {
		// TODO: verify funtionality
		Arrays.stream(improvements).filter(improvement -> improvementKey.equals(improvement.key))
				.filter(/* slotTagKey + ":" + improvementKey, */improvement -> level == improvement.level)
				.filter(improvement -> improvement.group != null).map(improvement -> improvement.group).findFirst()
				.ifPresent(group -> Arrays.stream(getImprovements(itemStack))
						.filter(improvement -> group.equals(improvement.group))
						.forEach(improvement -> removeImprovement(itemStack, slotTagKey, improvement.key)));
	}

	public void removeImprovement(ItemStack itemStack, String improvement) {
		removeImprovement(itemStack, slotTagKey, improvement);
	}

	public void removeEnchantments(ItemStack itemStack) {
		TetraEnchantmentHelper.removeEnchantments(itemStack, getSlot());
	}

	public boolean acceptsEnchantment(ItemStack itemStack, Enchantment enchantment, boolean fromTable) {
		return Optional.ofNullable(getAspects(itemStack)).map(AspectData::getLevelMap)
				.filter(aspects -> TetraEnchantmentHelper.isApplicableForAspects(enchantment, fromTable, aspects))
				.isPresent();
	}

	public EnchantmentCategory[] getApplicableEnchantmentCategories(ItemStack itemStack, boolean fromTable) {
		int requiredLevel = fromTable ? 2 : 1;
		return Optional.ofNullable(getAspects(itemStack)).map(AspectData::getLevelMap).map(Map::entrySet)
				.map(Set::stream).orElseGet(Stream::empty).filter(entry -> entry.getValue() >= requiredLevel)
				.map(Map.Entry::getKey).map(TetraEnchantmentHelper::getEnchantmentCategories).flatMap(Arrays::stream)
				.toArray(EnchantmentCategory[]::new);
	}

	public Set<String> getEnchantmentKeys(ItemStack itemStack) {
		CompoundTag mappings = itemStack.getTagElement("EnchantmentMapping");
		if (mappings != null) {
			return mappings.getAllKeys().stream().filter(key -> getSlot().equals(mappings.get(key).getAsString()))
					.collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

	public Map<String, Integer> getEnchantmentsPrimitive(ItemStack itemStack) {
		CompoundTag mappings = itemStack.getTagElement("EnchantmentMapping");

		if (itemStack.hasTag() && mappings != null) {
			return itemStack.getTag().getList("Enchantments", Tag.TAG_COMPOUND).stream().map(tag -> (CompoundTag) tag)
					.filter(tag -> getSlot().equals(mappings.getString(tag.getString("id"))))
					.map(TetraEnchantmentHelper::getEnchantmentPrimitive).filter(Objects::nonNull)
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}

		return Collections.emptyMap();
	}

	public Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
		CompoundTag mappings = itemStack.getTagElement("EnchantmentMapping");

		if (itemStack.hasTag() && mappings != null) {
			return itemStack.getTag().getList("Enchantments", Tag.TAG_COMPOUND).stream().map(tag -> (CompoundTag) tag)
					.filter(tag -> getSlot().equals(mappings.getString(tag.getString("id"))))
					.map(TetraEnchantmentHelper::getEnchantment).filter(Objects::nonNull)
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}

		return Collections.emptyMap();
	}

	public int getEnchantmentMagicCapacityCost(ItemStack itemStack) {
		return -getEnchantments(itemStack).entrySet().stream()
				.mapToInt(entry -> TetraEnchantmentHelper.getEnchantmentCapacityCost(entry.getKey(), entry.getValue()))
				.sum();
	}

	@Override
	public boolean isTweakable(ItemStack itemStack) {
		String[] improvementKeys = Arrays.stream(getImprovements(itemStack)).map(improvement -> improvement.key)
				.toArray(String[]::new);

		return Arrays.stream(tweaks).anyMatch(tweak -> ArrayUtils.contains(improvementKeys, tweak.improvement))
				|| super.isTweakable(itemStack);
	}

	@Override
	public TweakData[] getTweaks(ItemStack itemStack) {
//		if (itemStack.hasTag()) {
//			String variant = itemStack.getTag().getString(this.variantTagKey);
//			String[] improvementKeys = Arrays.stream(getImprovements(itemStack)).map(improvement -> improvement.key)
//					.toArray(String[]::new);
//
//			return Arrays.stream(tweaks).filter(
//					tweak -> variant.equals(tweak.variant) || ArrayUtils.contains(improvementKeys, tweak.improvement))
//					.toArray(TweakData[]::new);
//		}
		if (itemStack.has(TetraDataComponents.ITEM_MODULE)) {
			String variant = itemStack.get(TetraDataComponents.ITEM_MODULE).getVariantKeys().get(this.variantTagKey);
			String[] improvementKeys = Arrays.stream(getImprovements(itemStack)).map(improvement -> improvement.key)
					.toArray(String[]::new);

			return Arrays.stream(tweaks).filter(
					tweak -> variant.equals(tweak.variant) || ArrayUtils.contains(improvementKeys, tweak.improvement))
					.toArray(TweakData[]::new);
		}

		return new TweakData[0];
	}

	@Override
	public ItemStack[] removeModule(ItemStack targetStack, boolean upgrade) {
		ItemStack[] salvage = super.removeModule(targetStack, upgrade);

//		if (!upgrade && targetStack.hasTag()) {
//			CompoundTag tag = targetStack.getTag();
//			Arrays.stream(improvements).map(improvement -> slotTagKey + ":" + improvement.key).forEach(tag::remove);
//
//			clearProgression(targetStack);
//		}
		if (!upgrade && targetStack.has(TetraDataComponents.ITEM_MODULE)) {
			ItemModuleDataComponent mod = targetStack.get(TetraDataComponents.ITEM_MODULE);
			//TODO: verify functionality
			Arrays.stream(improvements).map(improvement -> slotTagKey + ":" + improvement.key).forEach(mod.improvementLevels()::remove);

			clearProgression(targetStack);
		}

		return salvage;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack itemStack) {
		return Arrays.stream(getImprovements(itemStack)).map(improvement -> improvement.attributes)
				.filter(Objects::nonNull).reduce(super.getAttributeModifiers(itemStack), AttributeHelper::merge);
	}

	@Override
	public ItemProperties getProperties(ItemStack itemStack) {
		return Arrays.stream(getImprovements(itemStack)).reduce(super.getProperties(itemStack), ItemProperties::merge,
				ItemProperties::merge);
	}

	@Override
	public EffectData getEffectData(ItemStack itemStack) {
		return Arrays.stream(getImprovements(itemStack)).map(improvement -> improvement.effects)
				.filter(Objects::nonNull).reduce(super.getEffectData(itemStack), EffectData::merge);
	}

	@Override
	public ToolData getToolData(ItemStack itemStack) {
		return Arrays.stream(getImprovements(itemStack)).map(improvement -> improvement.tools).filter(Objects::nonNull)
				.reduce(super.getToolData(itemStack), ToolData::merge);
	}

	public AspectData getAspects(ItemStack itemStack) {
		return Arrays.stream(getImprovements(itemStack)).map(improvement -> improvement.aspects)
				.filter(Objects::nonNull).reduce(super.getAspects(itemStack), AspectData::merge);
	}

	@Override
	public int getMagicCapacityGain(ItemStack itemStack) {
		return super.getMagicCapacityGain(itemStack) + getImprovementMagicCapacityGain(itemStack);
	}

	@Override
	public int getMagicCapacityCost(ItemStack itemStack) {
		return super.getMagicCapacityCost(itemStack) + getImprovementMagicCapacityCost(itemStack)
				+ getEnchantmentMagicCapacityCost(itemStack);
	}

	public int getImprovementMagicCapacityGain(ItemStack itemStack) {
		return Math.round(ConfigHandler.magicCapacityMultiplier.get().floatValue()
				* CastOptional.cast(itemStack.getItem(), IModularItem.class)
						.map(item -> item.getStabilityModifier(itemStack)).orElse(1f)
				* Arrays.stream(getImprovements(itemStack)).mapToInt(improvement -> improvement.magicCapacity)
						.filter(magicCapacity -> magicCapacity > 0).sum());
	}

	public int getImprovementMagicCapacityCost(ItemStack itemStack) {
		return -Arrays.stream(getImprovements(itemStack)).mapToInt(improvement -> improvement.magicCapacity)
				.filter(integrity -> integrity < 0).sum();
	}

	protected ModuleModel[] getImprovementModels(ItemStack itemStack, int tint) {
		return Arrays.stream(getImprovements(itemStack)).filter(improvement -> improvement.models.length > 0)
				.flatMap(improvement -> Arrays.stream(improvement.models)).map(model -> {
					if (ItemColors.inherit == model.tint) {
						ModuleModel copy = model.copy();
						copy.tint = tint;
						return copy;
					}
					return model;
				}).toArray(ModuleModel[]::new);
	}

	@Override
	public ModuleModel[] getModels(ItemStack itemStack) {
		ModuleModel[] models = super.getModels(itemStack);
		return ArrayUtils.addAll(models,
				getImprovementModels(itemStack, models.length > 0 ? models[0].overlayTint : 0xffffff));
	}
}
