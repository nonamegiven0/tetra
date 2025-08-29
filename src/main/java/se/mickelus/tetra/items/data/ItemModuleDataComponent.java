package se.mickelus.tetra.items.data;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import se.mickelus.tetra.util.MapCodecHelper;

//TODO: verify functionality
public class ItemModuleDataComponent {
	private Map<String, String> moduleKeys;
	private Map<String, String> variantKeys;
	private Map<String, Integer> moduleSteps;
	private Map<String, Integer> settleProgress;
	private Map<String, Integer> improvementLevels;
	
	public static final Codec<ItemModuleDataComponent> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				MapCodecHelper.STRING_MAP_CODEC.fieldOf("module_keys").forGetter(ItemModuleDataComponent::getModuleKeys),
				MapCodecHelper.STRING_MAP_CODEC.fieldOf("variant_keys").forGetter(ItemModuleDataComponent::variantKeys),
				MapCodecHelper.INT_MAP_CODEC.fieldOf("module_steps").forGetter(ItemModuleDataComponent::getModuleSteps),
				MapCodecHelper.INT_MAP_CODEC.fieldOf("settle_progress").forGetter(ItemModuleDataComponent::settleProgress),
				MapCodecHelper.INT_MAP_CODEC.fieldOf("improvement_levels").forGetter(ItemModuleDataComponent::improvementLevels)
				).apply(instance, ItemModuleDataComponent::new);
		});

	public ItemModuleDataComponent(Map<String, String> moduleKeys, Map<String, String> variantKeys,
			Map<String, Integer> moduleSteps, Map<String, Integer> settleProgress, Map<String, Integer> improvementLevels) {
		this.moduleKeys = moduleKeys;
		this.variantKeys = variantKeys;
		this.moduleSteps = moduleSteps;
		this.settleProgress = settleProgress;
		this.improvementLevels = improvementLevels;
	}

	public Map<String, String> getModuleKeys() {
		return moduleKeys;
	}

	public void setModuleKeys(Map<String, String> moduleKeys) {
		this.moduleKeys = moduleKeys;
	}

	public String getVariantKeys(String key) {
		return variantKeys.get(key);
	}
	public Map<String, String> variantKeys() {
		return variantKeys;
	}

	public void setVariantKey(String variantKey, String variant) {
		this.variantKeys.put(variantKey, variant);
	}

	public Map<String, Integer> getModuleSteps() {
		return moduleSteps;
	}

	public void setModuleSteps(Map<String, Integer> moduleSteps) {
		this.moduleSteps = moduleSteps;
	}
	
	@Nullable
	public Integer getSettleProgress(String key) {
		return settleProgress.get(key);
	}
	public Map<String, Integer> settleProgress() {
		return settleProgress;
	}

	public void setSettleProgress(String key, @Nullable Integer settleProgress) {
		this.settleProgress.put(key, settleProgress);
	}

	public Map<String, Integer> improvementLevels() {
		return improvementLevels;
	}
	public @Nullable Integer getImprovementLevels(String key) {
		return improvementLevels.get(key);
	}

	public void setImprovementLevels(String key, @Nullable Integer value) {
		this.improvementLevels.put(key, value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(moduleKeys, moduleSteps, settleProgress, variantKeys);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ItemModuleDataComponent)) {
			return false;
		}
		ItemModuleDataComponent other = (ItemModuleDataComponent) obj;
		return Objects.equals(moduleKeys, other.moduleKeys) && Objects.equals(moduleSteps, other.moduleSteps)
				&& settleProgress == other.settleProgress && Objects.equals(variantKeys, other.variantKeys);
	}
}
