package com.blackwing.easyExploration.inventory;

import com.blackwing.easyExploration.EasyExploration;
import com.blackwing.easyExploration.tileEntity.TileEntityDeathChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryDeathChest extends InventoryBasic {

    private final Logger log = LogManager.getLogger(EasyExploration.MODID + "." + getClass());

    public TileEntityDeathChest tileEntityDeathChest;
    public int stackCount = 0;
    public int itemCount = 0;

    public InventoryDeathChest(int slotCount) {
        super("container.deathchest", false, slotCount);
        log.info("created death chest inventory");
    }

    public void setTileEntityDeathChest(TileEntityDeathChest tileEntityDeathChest) {
        this.tileEntityDeathChest = tileEntityDeathChest;
        log.info("set tile entity in inventory");
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
        stackCount += ai_stackCount.get();
        itemCount += ai_itemCount.get();
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
        stackCount += ai_stackCount.get();
        itemCount += ai_itemCount.get();
        source.clear();
    }

    public void loadInventoryFromNBT(NBTTagList tagList) {
        log.info("loading from file");
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
        log.info("saving to file");
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
        log.info("opening");
        tileEntityDeathChest.openInventory(player);
        super.openInventory(player);
    }

    public void closeInventory(EntityPlayer player) {
        log.info("closing");
        tileEntityDeathChest.closeInventory(player);
        super.closeInventory(player);
    }
}
