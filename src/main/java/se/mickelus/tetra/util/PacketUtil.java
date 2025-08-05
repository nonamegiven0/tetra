package se.mickelus.tetra.util;

import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class PacketUtil {
	public static void writeItem(FriendlyByteBuf buf, ItemStack stack) {
    	buf.writeNbt(ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).getOrThrow());
	}
	public static ItemStack readItem(FriendlyByteBuf buf) {
    	return ItemStack.CODEC.parse(NbtOps.INSTANCE, buf.readNbt()).getOrThrow();
	}
}
