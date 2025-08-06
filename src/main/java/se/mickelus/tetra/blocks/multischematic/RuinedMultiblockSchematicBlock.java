package se.mickelus.tetra.blocks.multischematic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import se.mickelus.tetra.TetraItemAbilities;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.util.InteractionHelper;

import java.util.Collection;

public class RuinedMultiblockSchematicBlock extends HorizontalDirectionalBlock implements IInteractiveBlock {
	//TODO: verify functionality & stability
	public static final MapCodec<RuinedMultiblockSchematicBlock> CODEC = simpleCodec(RuinedMultiblockSchematicBlock::new);
    public static final DirectionProperty facingProp = BlockStateProperties.HORIZONTAL_FACING;

    protected ResourceLocation pryTable;

    protected BlockInteraction[] pryAction = new BlockInteraction[] {
            new BlockInteraction(TetraItemAbilities.pry, 1, Direction.EAST, 6, 10, 7, 10,
                    BlockStatePredicate.ANY,
                    this::pryBlock)
    };

    public RuinedMultiblockSchematicBlock(final Properties properties, ResourceLocation pryTable) {
        super(properties);
        this.pryTable = pryTable;

        this.registerDefaultState(this.stateDefinition.any().setValue(facingProp, Direction.EAST));
    }
    public RuinedMultiblockSchematicBlock(Properties properties) {
    	this(properties, null);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(facingProp);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(facingProp, context.getHorizontalDirection().getOpposite());
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (pryTable != null) {
            return BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit);
        }
        return InteractionResult.FAIL;
    }

    @Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
			BlockHitResult hitResult) {
		return use(state, level, pos, player, player.getUsedItemHand(), hitResult);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		return InteractionHelper.from(use(state, level, pos, player, hand, hitResult));
	}

	@Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState blockState, Direction face, Collection<ItemAbility> tools) {
        if (pryTable != null && face.getOpposite().equals(blockState.getValue(facingProp))) {
            return pryAction;
        }
        return new BlockInteraction[0];
    }

    protected boolean pryBlock(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction facing) {
        boolean didBreak = EffectHelper.breakBlock(world, player, player.getItemInHand(hand), pos, blockState, false, false);
        if (didBreak && world instanceof ServerLevel) {
            BlockInteraction.getLoot(pryTable, player, hand, (ServerLevel) world, blockState)
                    .forEach(lootStack -> popResource(world, pos, lootStack));
        }

        return true;
    }
	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}
}
