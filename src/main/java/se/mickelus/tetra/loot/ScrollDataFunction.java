package se.mickelus.tetra.loot;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import se.mickelus.tetra.blocks.scroll.ScrollData;

@ParametersAreNonnullByDefault
public class ScrollDataFunction extends LootItemConditionalFunction {
    public static final String identifier = "scroll";

    public static Supplier<LootItemFunctionType<ScrollDataFunction>> type;

    private final ScrollData data;

    protected ScrollDataFunction(List<LootItemCondition> conditions, ScrollData data) {
        super(conditions);

        this.data = data;
    }

    @Override
    protected ItemStack run(ItemStack itemStack, LootContext context) {
        data.write(itemStack);
        return itemStack;
    }

    @Override
    public LootItemFunctionType<ScrollDataFunction> getType() {
        return type.get();
    }

//    public static class Serializer extends LootItemConditionalFunction.Serializer<ScrollDataFunction> {
//        public void serialize(JsonObject json, ScrollDataFunction dataFunction, JsonSerializationContext context) {
//            super.serialize(json, dataFunction, context);
//
//            dataFunction.data.write(json);
//        }
//
//        @Override
//        public ScrollDataFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
//            return new ScrollDataFunction(conditions, ScrollData.read(json));
//        }
//    }
}
