package com.blackwing.easyExploration.tileEntity;

import com.blackwing.easyExploration.init.SoundEvents;
import com.blackwing.easyExploration.inventory.InventoryDeathChest;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class TileEntityDeathChest extends TileEntity implements ITickable, IInteractionObject {

    private InventoryDeathChest inventory;
    private UUID ownerUUID;
    private String ownerName;
    private IItemHandler itemHandler;
    /**
     * The current angle of the lid (between 0 and 1)
     */
    private float lidAngle = 0.0F;
    /**
     * The number of players currently using this chest
     */
    private boolean isInUse = false;
    /**
     * Server sync counter (once per 20 ticks)
     */
    private int ticksSinceSync;

    public void setInventory(InventoryDeathChest inventory) {
        this.inventory.moveStacks(inventory);
        this.inventory.setTileEntityDeathChest(this);
    }

    public InventoryDeathChest getInventory() {
        return inventory;
    }

    public void setOwner(EntityPlayer player) {
        ownerUUID = player.getUniqueID();
        ownerName = player.getName();
    }

    /**
     * Shows the name of the player who died as name of the chest.
     */
    @Override
    @NotNull
    public String getName() {
        return ownerName;
    }

    /**
     * Cannot be renamed by name tags etc.
     */
    @Override
    public boolean hasCustomName() {
        return false;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @NotNull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(inventory);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) (itemHandler == null ? (itemHandler = createUnSidedHandler()) : itemHandler);
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    /**
     * The tile entity is being loaded from the world store.
     */
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory.loadInventoryFromNBT(compound.getTagList("Inventory", 10));
        if (compound.hasKey("OwnerUUID", 8)) ownerUUID = compound.getUniqueId("OwnerUUID");
        if (compound.hasKey("OwnerName", 8)) ownerName = compound.getString("OwnerName");
    }

    /**
     * The tile entity is being saved to the world store.
     */
    @NotNull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("Inventory", inventory.saveInventoryToNBT());
        compound.setUniqueId("OwnerUUID", ownerUUID);
        compound.setString("OwnerName", ownerName);

        return compound;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update() {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        ++ticksSinceSync;

        if (!world.isRemote && isInUse && (ticksSinceSync + i + j + k) % 200 == 0) {
            isInUse = false;

            // for every player in reach of the chest, check if he/she has this container open
            for (EntityPlayer entityplayer : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F), (double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F), (double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {
                if (entityplayer.openContainer instanceof ContainerChest) {
                    IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();

                    isInUse = (iinventory == inventory);
                    break;
                }
            }
        }

        if (isInUse && lidAngle == 0.0F) {
            double d1 = (double) i + 0.5D;
            double d2 = (double) k + 0.5D;
            world.playSound(null, d1, (double) j + 0.5D, d2, SoundEvents.BLOCK_DEATHCHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (!isInUse && lidAngle > 0.0F || isInUse && lidAngle < 1.0F) {
            float f2 = lidAngle;

            if (isInUse) lidAngle += 0.1F;
            else lidAngle -= 0.1F;

            if (lidAngle > 1.0F) lidAngle = 1.0F;

            if (lidAngle < 0.5F && f2 >= 0.5F) {
                double d3 = (double) i + 0.5D;
                double d0 = (double) k + 0.5D;
                world.playSound(null, d3, (double) j + 0.5D, d0, SoundEvents.BLOCK_DEATHCHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F) lidAngle = 0.0F;
        }
    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            isInUse = type > 0;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate() {
        updateContainingBlockInfo();
        super.invalidate();
    }

    /**
     * do stuff when opening the inventory
     */
    public void openInventory(@NotNull EntityPlayer player) {
        if (player.isSpectator()) return;

        isInUse = true;
        world.addBlockEvent(pos, getBlockType(), 1, 1);
        world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
    }

    /**
     * do stuff when closing the inventory
     */
    public void closeInventory(@NotNull EntityPlayer player) {
        if (player.isSpectator() || !(getBlockType() instanceof BlockChest)) return;

        isInUse = false;
        world.addBlockEvent(pos, getBlockType(), 1, 0);
        world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
    }

    @NotNull
    public String getGuiID() {
        return "minecraft:chest";
    }

    @NotNull
    public Container createContainer(@NotNull InventoryPlayer playerInventory, @NotNull EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, inventory, playerIn);
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player) {
        // double check if the block at this position is block
        if (world.getTileEntity(pos) != this) return false;
        // check if the player is too far away
        if (player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) > 64.0D)
            return false;
        // check if the player is the owner of this chest
        if (player.getUniqueID() != ownerUUID) return false;

        return inventory.isUsableByPlayer(player);
    }
}
