package se.mickelus.tetra;

import java.util.Optional;
import java.util.stream.StreamSupport;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class LooseItemPredicate extends ItemPredicate {

    private final String[] keys;

    public LooseItemPredicate(JsonObject jsonObject) {
        keys = StreamSupport.stream(jsonObject.get("keys").getAsJsonArray().spliterator(), false)
                .map(JsonElement::getAsString)
                .toArray(String[]::new);

    }

    @Override
    public boolean test(ItemStack itemStack) {
        String target = Optional.of(itemStack.getItem())
                .map(BuiltInRegistries.ITEM::getKey)
                .map(ResourceLocation::getPath)
                .orElse(null);
        for (String key : keys) {
            if (key.equals(target)) {
                return true;
            }
        }

        return false;
    }
}
