package se.mickelus.tetra.items.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class BoosterItemDataComponent {
    public static final Codec<BoosterItemDataComponent> CODEC = RecordCodecBuilder.create(instance -> {
	return instance
		.group(
			Codec.BOOL.fieldOf("active").forGetter(BoosterItemDataComponent::isActive),
			Codec.BOOL.fieldOf("charged").forGetter(BoosterItemDataComponent::isCharged),
			Codec.INT.fieldOf("fuel").forGetter(BoosterItemDataComponent::getFuel),
			Codec.INT.fieldOf("buffer").forGetter(BoosterItemDataComponent::getBuffer),
			Codec.INT.fieldOf("cooldown").forGetter(BoosterItemDataComponent::getCooldown)
			)
		.apply(instance, BoosterItemDataComponent::new);
    });
    
    private boolean active;
    private boolean charged;
    private int fuel;
    private int buffer;
    private int cooldown;
    
    public BoosterItemDataComponent(boolean active, boolean charged, int fuel, int buffer, int cooldown) {
	super();
	this.active = active;
	this.charged = charged;
	this.fuel = fuel;
	this.buffer = buffer;
	this.cooldown = cooldown;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCharged() {
        return charged;
    }

    public void setCharged(boolean charged) {
        this.charged = charged;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getBuffer() {
        return buffer;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
