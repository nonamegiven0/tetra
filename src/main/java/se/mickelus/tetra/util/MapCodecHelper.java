package se.mickelus.tetra.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.UnboundedMapCodec;

public abstract class MapCodecHelper {
	public static final UnboundedMapCodec<String, String> STRING_MAP_CODEC = Codec.unboundedMap(Codec.STRING,
			Codec.STRING);
	public static final UnboundedMapCodec<String, Integer> INT_MAP_CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT);
	public static final UnboundedMapCodec<String, Boolean> BOOL_MAP_CODEC = Codec.unboundedMap(Codec.STRING, Codec.BOOL);
}
