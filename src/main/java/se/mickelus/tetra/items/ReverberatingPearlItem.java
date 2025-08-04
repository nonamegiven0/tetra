package se.mickelus.tetra.items;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import se.mickelus.tetra.Tooltips;

@ParametersAreNonnullByDefault
public class ReverberatingPearlItem extends TetraItem {
    private static final String unlocalizedName = "reverberating_pearl";
//    @ObjectHolder(TetraMod.MOD_ID + ":" + unlocalizedName)
//    public static ReverberatingPearlItem instance;

    public ReverberatingPearlItem() {
        super(new Properties());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item." + unlocalizedName + ".tooltip"));
        tooltip.add(Component.literal(" "));

        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            tooltip.add(Tooltips.reveal);
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("item." + unlocalizedName + ".tooltip_extended"));
        } else {
            tooltip.add(Tooltips.expand);
        }
    }
}
