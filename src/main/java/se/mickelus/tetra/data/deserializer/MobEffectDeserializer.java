package se.mickelus.tetra.data.deserializer;

import java.lang.reflect.Type;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

@ParametersAreNonnullByDefault
public class MobEffectDeserializer implements JsonDeserializer<MobEffect> {
    @Override
    public MobEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String string = json.getAsString();
        if (string != null) {
            ResourceLocation resourceLocation = ResourceLocation.parse(string);
            if (BuiltInRegistries.MOB_EFFECT.containsKey(resourceLocation)) {
                return BuiltInRegistries.MOB_EFFECT.get(resourceLocation);
            }
        }

        return null;
    }
}
