package se.mickelus.tetra.items.forged;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.ObjectHolder;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class BeamItem extends TetraItem {
    public static final String identifier = "forged_beam";
    @ObjectHolder(registryName = "item", value = TetraMod.MOD_ID + ":" + identifier)
    public static BeamItem instance;

    public BeamItem() {
        super(new Properties());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }
}
