package se.mickelus.tetra.items.modular.impl.toolbelt.booster;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import se.mickelus.tetra.items.data.BoosterItemDataComponent;
import se.mickelus.tetra.items.data.TetraDataComponents;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;

@ParametersAreNonnullByDefault
public class TickHandlerBooster {

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPlayerTick(PlayerTickEvent.Pre event) {
		ItemStack itemStack = ToolbeltHelper.findToolbelt(event.getEntity());
		int level = UtilBooster.getBoosterLevel(itemStack);
		if (level > 0) {
			tickItem(event.getEntity(), itemStack, level);
		}
	}

	public void tickItem(Player player, ItemStack stack, int level) {
//		CompoundTag tag = stack.getOrCreateTag();
	    	BoosterItemDataComponent data = stack.get(TetraDataComponents.BOOSTER_ITEM);
//		boolean charged = tag.getBoolean(UtilBooster.chargedKey);
	    	boolean charged = data.isCharged();
		if (!player.isInWater() && player.getVehicle() == null && /*UtilBooster.isActive(tag)*/data.isActive()
//				&& UtilBooster.hasFuel(tag, charged)) {
				&& UtilBooster.hasFuel(data, charged)) {
			if (charged) {
				UtilBooster.boostPlayerCharged(player, data, level);
			} else {
				UtilBooster.boostPlayer(player, data, level);
			}

			UtilBooster.consumeFuel(data, charged);
		} else {
			UtilBooster.rechargeFuel(data, stack);
		}

		if (charged) {
//			tag.putBoolean(UtilBooster.chargedKey, false);
		    data.setCharged(false);
		}
		stack.set(TetraDataComponents.BOOSTER_ITEM, data);
	}
}
