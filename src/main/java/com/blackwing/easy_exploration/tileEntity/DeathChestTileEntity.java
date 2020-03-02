package com.blackwing.easy_exploration.tileEntity;

import com.blackwing.easy_exploration.container.DeathChestContainer;
import com.blackwing.easy_exploration.init.ModBlocks;
import com.blackwing.easy_exploration.init.ModTileEntityTypes;
import com.blackwing.easy_exploration.block.DeathChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DeathChestTileEntity extends TileEntity implements INamedContainerProvider {

    private static final String INVENTORY_TAG = "inventory";

    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
            return FurnaceTileEntity.isFuel(stack);
        }

        @Override
        protected void onContentsChanged(final int slot) {
            super.onContentsChanged(slot);
            // Mark the tile entity as having changed whenever its inventory changes.
            // "markDirty" tells vanilla that the chunk containing the tile entity has
            // changed and means the game will save the chunk to disk later.
            DeathChestTileEntity.this.markDirty();
        }
    };

    // Store the capability lazy optionals as fields to keep the amount of objects we use to a minimum
    private final LazyOptional<ItemStackHandler> inventoryCapabilityExternal = LazyOptional.of(() -> this.inventory);
    private int lastEnergy = -1;

    public DeathChestTileEntity() {
        super(ModTileEntityTypes.DEATH_CHEST.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventoryCapabilityExternal.cast();
        return super.getCapability(cap, side);
    }

    /**
     * Read saved data from disk into the tile.
     */
    @Override
    public void read(final CompoundNBT compound) {
        super.read(compound);
        this.inventory.deserializeNBT(compound.getCompound(INVENTORY_TAG));
    }

    /**
     * Write data from the tile into a compound tag for saving to disk.
     */
    @Nonnull
    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        super.write(compound);
        compound.put(INVENTORY_TAG, this.inventory.serializeNBT());
        return compound;
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is re-synced via World#notifyBlockUpdate.
     * This packet comes back client-side in {@link #onDataPacket}
     */
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        final CompoundNBT tag = new CompoundNBT();
        return new SUpdateTileEntityPacket(this.pos, 0, tag);
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the
     * chunk or when many blocks change at once.
     * This compound comes back to you client-side in {@link #handleUpdateTag}
     * The default implementation ({@link TileEntity#handleUpdateTag}) calls {@link #writeInternal)}
     * which doesn't save any of our extra data so we override it to call {@link #write} instead
     */
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    /**
     * Invalidates our tile entity
     */
    @Override
    public void remove() {
        super.remove();
        // We need to invalidate our capability references so that any cached references (by other mods) don't
        // continue to reference our capabilities and try to use them and/or prevent them from being garbage collected
        inventoryCapabilityExternal.invalidate();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(ModBlocks.DEATH_CHEST.get().getTranslationKey());
    }

    /**
     * Called from {@link NetworkHooks#openGui}
     * (which is called from {@link DeathChestBlock#onBlockActivated} on the logical server)
     *
     * @return The logical-server-side Container for this TileEntity
     */
    @Nonnull
    @Override
    public Container createMenu(final int windowId, final PlayerInventory inventory, final PlayerEntity player) {
        return new DeathChestContainer(windowId, inventory, this);
    }

}
