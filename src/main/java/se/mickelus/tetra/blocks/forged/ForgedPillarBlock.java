package se.mickelus.tetra.blocks.forged;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.RotatedPillarBlock;
import se.mickelus.tetra.blocks.InitializableBlock;

@ParametersAreNonnullByDefault
public class ForgedPillarBlock extends RotatedPillarBlock implements InitializableBlock {
    public static final String identifier = "forged_pillar";

    public ForgedPillarBlock() {
        super(ForgedBlockCommon.propertiesSolid);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext ctx, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }
}
