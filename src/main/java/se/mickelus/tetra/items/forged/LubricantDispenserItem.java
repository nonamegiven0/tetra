package se.mickelus.tetra.items.forged;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class LubricantDispenserItem extends TetraItem {
    public static final String identifier = "lubricant_dispenser";
    public static DeferredHolder<Item, LubricantDispenserItem> instance;

    public LubricantDispenserItem() {
        super(new Properties());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.tetra.lubricant_dispenser.description").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" "));
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }
}
