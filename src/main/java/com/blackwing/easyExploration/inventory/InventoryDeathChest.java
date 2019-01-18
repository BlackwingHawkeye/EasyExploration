package com.blackwing.easyExploration.inventory;

import com.blackwing.easyExploration.tileEntity.TileEntityDeathChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryDeathChest extends InventoryBasic {

    public TileEntityDeathChest tileEntityDeathChest;
    public int stackCount = 0;
    public int itemCount = 0;

    public InventoryDeathChest(int slotCount) {
        super("container.deathchest", false, slotCount);
    }

    public void setTileEntityDeathChest(TileEntityDeathChest tileEntityDeathChest) {
        this.tileEntityDeathChest = tileEntityDeathChest;
    }

    public void moveStacks(NonNullList<ItemStack> source) {
        AtomicInteger ai_stackCount = new AtomicInteger(stackCount);
        AtomicInteger ai_itemCount = new AtomicInteger(itemCount);
        for (ItemStack stack : source)
            if (!stack.isEmpty()) {
                addItem(stack);
                ai_stackCount.incrementAndGet();
                ai_itemCount.addAndGet(stack.getCount());
            }
        stackCount = ai_stackCount.get();
        itemCount = ai_itemCount.get();
        source.clear();
    }

    public void moveStacks(InventoryDeathChest source) {
        AtomicInteger ai_stackCount = new AtomicInteger(stackCount);
        AtomicInteger ai_itemCount = new AtomicInteger(itemCount);
        for (int i = 0; i < source.getSizeInventory(); ++i) {
            ItemStack stack = source.getStackInSlot(i);
            if (!stack.isEmpty()) {
                addItem(stack);
                ai_stackCount.incrementAndGet();
                ai_itemCount.addAndGet(stack.getCount());
            }
        }
        stackCount = ai_stackCount.get();
        itemCount = ai_itemCount.get();
        source.clear();
    }

    public void loadInventoryFromNBT(NBTTagList tagList) {
        clear();
        //for (int i = 0; i < getSizeInventory(); ++i)             setInventorySlotContents(i, ItemStack.EMPTY);

        for (int k = 0; k < tagList.tagCount(); ++k) {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(k);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < getSizeInventory()) {
                setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        }
    }

    public NBTTagList saveInventoryToNBT() {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i) {
            ItemStack itemstack = getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void openInventory(EntityPlayer player) {
        tileEntityDeathChest.openInventory(player);
        super.openInventory(player);
    }

    public void closeInventory(EntityPlayer player) {
        tileEntityDeathChest.closeInventory(player);
        super.closeInventory(player);
    }
}
