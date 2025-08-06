package se.mickelus.tetra.data.deserializer;

import java.lang.reflect.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundEventDeserializer implements JsonDeserializer<SoundEvent> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public SoundEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(json.getAsString()));
        } catch (JsonParseException e) {
            logger.debug("Failed to parse sound event: {}", json, e);
            return null;
        }
    }
}
