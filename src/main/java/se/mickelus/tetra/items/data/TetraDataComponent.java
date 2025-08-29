package se.mickelus.tetra.items.data;

import com.mojang.serialization.Codec;

public interface TetraDataComponent {
	public <T extends Codec<?>> T codec(boolean forNetworking);
}
