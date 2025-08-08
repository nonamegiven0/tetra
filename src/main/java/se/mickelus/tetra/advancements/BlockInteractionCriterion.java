package se.mickelus.tetra.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import se.mickelus.mutil.util.JsonOptional;
import se.mickelus.tetra.blocks.PropertyMatcher;


public class BlockInteractionCriterion extends AbstractCriterionTriggerInstance {
    public static final GenericTrigger<BlockInteractionCriterion> trigger = new GenericTrigger<>("tetra:block_interaction", BlockInteractionCriterion::deserialize);
    private final PropertyMatcher before;
    private final PropertyMatcher after;
    private final ItemAbility itemAbility;
    private final int toolLevel;

    public BlockInteractionCriterion(ContextAwarePredicate playerCondition, PropertyMatcher before, PropertyMatcher after, ItemAbility ItemAbility, int toolLevel) {
        super(trigger.getId(), playerCondition);
        this.before = before;
        this.after = after;
        this.itemAbility = ItemAbility;
        this.toolLevel = toolLevel;
    }

    public static void trigger(ServerPlayer player, BlockState beforeState, BlockState afterState, ItemAbility usedItemAbility, int usedToolLevel) {
        trigger.fulfillCriterion(player, criterion -> criterion.test(beforeState, afterState, usedItemAbility, usedToolLevel));


    }

    private static BlockInteractionCriterion deserialize(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return new BlockInteractionCriterion(entityPredicate,
                JsonOptional.field(json, "before")
                        .map(PropertyMatcher::deserialize)
                        .orElse(null),
                JsonOptional.field(json, "after")
                        .map(PropertyMatcher::deserialize)
                        .orElse(null),
                JsonOptional.field(json, "tool")
                        .map(JsonElement::getAsString)
                        .map(ItemAbility::get)
                        .orElse(null),
                JsonOptional.field(json, "toolLevel")
                        .map(JsonElement::getAsInt)
                        .orElse(-1));
    }

    public boolean test(BlockState beforeState, BlockState afterState, ItemAbility usedItemAbility, int usedToolLevel) {
        if (before != null && !before.test(beforeState)) {
            return false;
        }

        if (after != null && !after.test(afterState)) {
            return false;
        }

        if (this.itemAbility != null && !this.itemAbility.equals(usedItemAbility)) {
            return false;
        }

        return this.toolLevel == -1 || this.toolLevel == usedToolLevel;
    }
}
