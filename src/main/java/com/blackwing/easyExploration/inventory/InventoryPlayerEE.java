package com.blackwing.easyExploration.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class InventoryPlayerEE extends InventoryPlayer {
    public InventoryDeathChest inventoryDeathChest;

    public InventoryPlayerEE(EntityPlayer playerIn) {
        super(playerIn);
        inventoryDeathChest = new InventoryDeathChest(mainInventory.size() + armorInventory.size() + offHandInventory.size());
    }
}
