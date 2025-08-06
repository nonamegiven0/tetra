package se.mickelus.tetra.items.forged;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class EarthpiercerItem extends TetraItem {
    public static final String identifier = "earthpiercer";
    public static DeferredHolder<Item, EarthpiercerItem> instance = TetraRegistries.earthpiercer;

    public EarthpiercerItem() {
        super(new Properties());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(ForgedBlockCommon.unsettlingTooltip);
        tooltip.add(Component.literal(" "));

        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            tooltip.add(Component.translatable("item.tetra.earthpiercer.description").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal(" "));
            tooltip.add(ForgedBlockCommon.locationTooltip);
        } else {
            tooltip.add(Tooltips.expand);
        }
    }
}
