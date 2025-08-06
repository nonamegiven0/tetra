package se.mickelus.tetra.blocks.workbench;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.advancements.BlockUseCriterion;

@ParametersAreNonnullByDefault
public class BasicWorkbenchBlock extends AbstractWorkbenchBlock {
    public static final String identifier = "basic_workbench";
    public static DeferredHolder<Block, BasicWorkbenchBlock> instance = TetraRegistries.basicWorkbench;

    public BasicWorkbenchBlock() {
        super(Properties.of()
                .strength(2.5f)
                .sound(SoundType.WOOD));
    }

    public static InteractionResult upgradeWorkbench(Player player, Level world, BlockPos pos, InteractionHand hand, Direction facing) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!player.mayUseItemAt(pos.relative(facing), facing, itemStack)) {
            return InteractionResult.FAIL;
        }

        if (world.getBlockState(pos).getBlock().equals(Blocks.CRAFTING_TABLE)) {

            world.playSound(player, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);

            if (!world.isClientSide) {
                world.setBlockAndUpdate(pos, instance.get().defaultBlockState());

                BlockUseCriterion.trigger((ServerPlayer) player, instance.get().defaultBlockState(), ItemStack.EMPTY);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext ctx, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(Component.translatable("block.tetra.basic_workbench.description").withStyle(ChatFormatting.GRAY));
    }
}
