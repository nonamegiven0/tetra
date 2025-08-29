package se.mickelus.tetra.items.data;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ModularItemDataComponent {
	private UUID id;
	private @Nullable Integer honingProgress;
	private @Nullable Boolean honingAvailable;
	private @Nullable Integer honingCount;
	private @Nullable Integer repairCount;
//	private int cooledStrength;

	public static final Codec<ModularItemDataComponent> CODEC = RecordCodecBuilder.create(instance -> {
		return instance
				.group(Codec.STRING.fieldOf("id")
						.forGetter(cmp -> cmp.getId().toString()),
						Codec.INT.fieldOf("honing_progress")
						.forGetter(ModularItemDataComponent::getHoningProgress),
						Codec.BOOL.fieldOf("honing_available")
								.forGetter(ModularItemDataComponent::isHoningAvailable),
								Codec.INT.fieldOf("honing_count")
								.forGetter(ModularItemDataComponent::getHoningCount),
								Codec.INT.fieldOf("repair_count")
								.forGetter(ModularItemDataComponent::getRepairCount))
				.apply(instance, ModularItemDataComponent::new);
	});

	public ModularItemDataComponent(String id, @Nullable Integer honingProgress, @Nullable Boolean honingAvailable, @Nullable Integer honingCount, @Nullable Integer repairCount) {
		super();
		this.id = UUID.fromString(id);
		this.honingProgress = honingProgress;
		this.honingAvailable = honingAvailable;
		this.honingCount = honingCount;
		this.repairCount = repairCount;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public @Nullable Integer getHoningProgress() {
		return honingProgress;
	}

	public void setHoningProgress(@Nullable Integer honingProgress) {
		this.honingProgress = honingProgress;
	}

	public @Nullable Boolean isHoningAvailable() {
		return honingAvailable;
	}

	public void setHoningAvailable(@Nullable Boolean honingAvailable) {
		this.honingAvailable = honingAvailable;
	}

	public @Nullable Integer getHoningCount() {
		return honingCount;
	}

	public void setHoningCount(@Nullable Integer honingCount) {
		this.honingCount = honingCount;
	}

	public @Nullable Integer getRepairCount() {
		return repairCount;
	}

	public void setRepairCount(@Nullable Integer repairCount) {
		this.repairCount = repairCount;
	}
}
