package se.mickelus.tetra.blocks.geode;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class PristineLapisItem extends TetraItem {
    public static final String identifier = "pristine_lapis";

    public static DeferredHolder<Item, PristineLapisItem> instance = TetraRegistries.pristineLapis;

    public PristineLapisItem() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext ctx, List<Component> tooltip, TooltipFlag advanced) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            tooltip.add(Component.translatable("item.tetra.pristine_gem.description").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Tooltips.expand);
        }
    }
}
