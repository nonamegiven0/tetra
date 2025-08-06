package se.mickelus.tetra.blocks.geode;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class GeodeItem extends TetraItem {
    public static final String identifier = "geode";

    public static DeferredHolder<Item, GeodeItem> instance = TetraRegistries.geode;

    public GeodeItem() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.tetra.geode.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
