package se.mickelus.tetra.blocks.forged;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.blocks.TetraBlock;

@ParametersAreNonnullByDefault
public class ForgedPlatformBlock extends TetraBlock {
    public static final String identifier = "forged_platform";

    public static DeferredHolder<Block, ForgedPlatformBlock> instance = TetraRegistries.forgedPlatform;

    public ForgedPlatformBlock() {
        super(ForgedBlockCommon.propertiesSolid);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext ctx, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }
}
