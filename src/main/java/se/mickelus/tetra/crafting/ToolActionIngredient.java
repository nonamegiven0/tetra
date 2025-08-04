package se.mickelus.tetra.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.crafting.CraftingHelper;
import net.neoforged.neoforge.common.crafting.IIngredientSerializer;
import net.neoforged.neoforge.registries.ForgeRegistries;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.schematic.requirement.IntegerPredicate;

import javax.annotation.Nullable;

public class ItemAbilityIngredient extends Ingredient {
    private final ItemAbility ItemAbility;
    private final IntegerPredicate tier;

    protected ItemAbilityIngredient(ItemAbility ItemAbility, IntegerPredicate tier) {
        super(ForgeRegistries.ITEMS.getValues().stream()
                .map(Item::getDefaultInstance)
                .filter(stack -> stack.canPerformAction(ItemAbility))
                .map(ItemValue::new));

        this.ItemAbility = ItemAbility;
        this.tier = tier;
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null) {
            return false;
        }

        return input.canPerformAction(ItemAbility)
                && (tier == null || input.getItem() instanceof ItemModularHandheld item && tier.test(item.getHarvestTier(input, ItemAbility)));
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return ItemAbilityIngredient.Serializer.instance;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(getSerializer()).toString());
        json.addProperty("tool", ItemAbility.name());
        if (tier != null) {
            json.add("tier", tier.serialize());
        }

        return json;
    }

    /**
     * Read/write directly to json to make it easier to write in recipe jsons
     */
    public static class Serializer implements IIngredientSerializer<ItemAbilityIngredient> {
        public static final ItemAbilityIngredient.Serializer instance = new ItemAbilityIngredient.Serializer();

        @Override
        public ItemAbilityIngredient parse(JsonObject json) {
            ItemAbility ItemAbility = ItemAbility.get(json.get("tool").getAsString());
            IntegerPredicate tier = json.has("tier")
                    ? DataManager.gson.fromJson(json.getAsJsonObject("tier"), IntegerPredicate.class)
                    : null;
            return new ItemAbilityIngredient(ItemAbility, tier);
        }

        @Override
        public ItemAbilityIngredient parse(FriendlyByteBuf buffer) {
            ItemAbility ItemAbility = ItemAbility.get(buffer.readUtf());
            IntegerPredicate tier = IntegerPredicate.fromBuffer(buffer);
            return new ItemAbilityIngredient(ItemAbility, tier);
        }

        @Override
        public void write(FriendlyByteBuf buffer, ItemAbilityIngredient ingredient) {
            buffer.writeUtf(ingredient.ItemAbility.name());
            if (ingredient.tier != null) {
                ingredient.tier.toBuffer(buffer);
            } else {
                buffer.writeVarInt(Integer.MIN_VALUE);
                buffer.writeVarInt(Integer.MIN_VALUE);
            }
        }
    }
}
