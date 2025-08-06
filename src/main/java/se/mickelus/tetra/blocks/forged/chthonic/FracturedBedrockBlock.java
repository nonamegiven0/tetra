package se.mickelus.tetra.blocks.forged.chthonic;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.forged.extractor.SeepingBedrockBlock;

@ParametersAreNonnullByDefault
public class FracturedBedrockBlock extends TetraBlock implements EntityBlock {
    public static final String identifier = "fractured_bedrock";

    public static DeferredHolder<Block, FracturedBedrockBlock> instance = TetraRegistries.fracturedBedrock;

    public FracturedBedrockBlock() {
        super(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).noLootTable());
    }

    public static boolean canPierce(Level world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return Blocks.BEDROCK.equals(blockState.getBlock())
                || (blockState.is(SeepingBedrockBlock.instance) && !SeepingBedrockBlock.isActive(blockState));
    }

    public static void pierce(Level world, BlockPos pos, int amount) {
        FracturedBedrockTile tile = TileEntityOptional.from(world, pos, FracturedBedrockTile.class).orElse(null);

        if (tile == null && canPierce(world, pos)) {
//            BlockState blockState = world.getBlockState(pos);
            world.setBlock(pos, instance.get().defaultBlockState(), 2);
            tile = TileEntityOptional.from(world, pos, FracturedBedrockTile.class).orElse(null);

            if (!world.isClientSide) {
                tile.updateLuck();
            }
        }

        if (tile != null) {
            tile.activate(amount);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FracturedBedrockTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
//        return getTicker(entityType, FracturedBedrockTile.type, (lvl, pos, blockState, tile) -> tile.tick(lvl, pos, blockState));
    	return getTicker(entityType, FracturedBedrockTile.type.get(), (lvl, pos, blockState, blockEntity) -> blockEntity.tick(level, pos, state));
    }
}
