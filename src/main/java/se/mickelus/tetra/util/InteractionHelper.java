package se.mickelus.tetra.util;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;

public class InteractionHelper {
	public static ItemInteractionResult from(InteractionResult result) {
		switch (result) {
		case CONSUME:
			return ItemInteractionResult.CONSUME;
		case CONSUME_PARTIAL:
			return ItemInteractionResult.CONSUME_PARTIAL;
		case FAIL:
			return ItemInteractionResult.FAIL;
		case PASS:
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		case SUCCESS:
			return ItemInteractionResult.SUCCESS;
		case SUCCESS_NO_ITEM_USED:
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		default:
			return ItemInteractionResult.FAIL; // should not be reached
		}
	}
	public static InteractionResult from(ItemInteractionResult result) {
		switch (result) {
		case CONSUME:
			return InteractionResult.CONSUME;
		case CONSUME_PARTIAL:
			return InteractionResult.CONSUME_PARTIAL;
		case FAIL:
			return InteractionResult.FAIL;
		case PASS_TO_DEFAULT_BLOCK_INTERACTION:
			return InteractionResult.PASS;
		case SUCCESS:
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		case SKIP_DEFAULT_BLOCK_INTERACTION:
			return InteractionResult.SUCCESS;
		default:
			return InteractionResult.FAIL; // should not be reached
		}
	}
}
