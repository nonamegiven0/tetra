package se.mickelus.tetra.blocks.rack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import se.mickelus.tetra.TetraRegistries;

@ParametersAreNonnullByDefault
public class RackTile extends BlockEntity {
	public static final String unlocalizedName = "rack";
	public static final int inventorySize = 2;
	private static final String inventoryKey = "inv";
	public static DeferredHolder<BlockEntityType<?>, BlockEntityType<RackTile>> type;
//    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> new ItemStackHandler(inventorySize) {
//        protected void onContentsChanged(int slot) {
//            setChanged();
//            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
//        }
//    });

	public RackTile(BlockPos p_155268_, BlockState p_155269_) {
		super(type.get(), p_155268_, p_155269_);
		setData(TetraRegistries.stackHandlerAttachment, new ItemStackHandler(inventorySize) {
			protected void onContentsChanged(int slot) {
				setChanged();
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			}
		});
	}

//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull net.neoforged.neoforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
//        if (cap == Capabilities.ITEM_HANDLER) {
//            return handler.cast();
//        }
//        return super.getCapability(cap, side);
//    }

	public void slotInteract(int slot, Player playerEntity, InteractionHand hand) {
		IItemHandler cap = this.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, worldPosition, null);
		if (cap != null && cap instanceof ItemStackHandler) {
			ItemStackHandler handler = (ItemStackHandler) cap;
			ItemStack slotStack = handler.getStackInSlot(slot);
			ItemStack heldStack = playerEntity.getItemInHand(hand);
			if (slotStack.isEmpty()) {
				ItemStack remainder = handler.insertItem(slot, heldStack.copy(), false);
				playerEntity.setItemInHand(hand, remainder);
				playerEntity.playSound(SoundEvents.WOOD_PLACE, 0.5f, 0.7f);
			} else {
				ItemStack extractedStack = handler.extractItem(slot, handler.getSlotLimit(slot), false);
				if (playerEntity.getInventory().add(extractedStack)) {
					playerEntity.playSound(SoundEvents.ITEM_PICKUP, 0.5f, 1);
				} else {
					playerEntity.drop(extractedStack, false);
				}
			}
		}
	}

//	@Override
//	public AABB getRenderBoundingBox() {
//		return Shapes.block().bounds().move(worldPosition);
//	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveWithoutMetadata(registries);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
		loadAdditional(pkt.getTag(), registries);
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);

//		handler.ifPresent(handler -> handler.deserializeNBT(compound.getCompound(inventoryKey)));
		ItemStackHandler handler = getData(TetraRegistries.stackHandlerAttachment);
		if (handler != null) {
			handler.deserializeNBT(registries, compound.getCompound(inventoryKey));
		}
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);

//		handler.ifPresent(handler -> compound.put(inventoryKey, handler.serializeNBT()));
		ItemStackHandler handler = getData(TetraRegistries.stackHandlerAttachment);
		if (handler != null) {
			compound.put(inventoryKey, handler.serializeNBT(registries));
		}
	}
}
