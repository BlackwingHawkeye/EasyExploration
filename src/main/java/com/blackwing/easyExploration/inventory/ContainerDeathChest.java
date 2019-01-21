package com.blackwing.easyExploration.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerDeathChest extends Container {

    private final InventoryDeathChest chestInventory;
    private final int numRows;

    public ContainerDeathChest(InventoryPlayer playerInventory, InventoryDeathChest inventoryDeathChest, EntityPlayer player) {
        chestInventory = inventoryDeathChest;

        int rows = inventoryDeathChest.getSizeInventory() / 9;
        if (rows < inventoryDeathChest.getSizeInventory() / 9.0) rows++;
        numRows = rows;

        inventoryDeathChest.openInventory(player);
        int i = (numRows - 4) * 18;

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlotToContainer(new Slot(inventoryDeathChest, col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return chestInventory.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @NotNull
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < numRows * 9) {
                if (!mergeItemStack(itemstack1, numRows * 9, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 0, numRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        chestInventory.closeInventory(playerIn);
    }

    /**
     * Return this chest container's chest inventory.
     */
    public IInventory getChestInventory() {
        return chestInventory;
    }
}
