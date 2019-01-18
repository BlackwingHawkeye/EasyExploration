package com.blackwing.easyExploration.saveInventory;

import com.blackwing.easyExploration.EasyExploration;
import com.blackwing.easyExploration.config.Configuration;
import com.blackwing.easyExploration.config.Configuration.InventoryOption;
import com.blackwing.easyExploration.inventory.InventoryPlayerEE;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SaveInventory {

    /**
     * Singleton
     */
    private static final SaveInventory instance = new SaveInventory();

    /**
     * @return instance
     */
    public static SaveInventory instance() {
        return instance;
    }

    /**
     * Constructor
     */
    private SaveInventory() {
    }

    private static Configuration.SubCategorySaveInventory config = Configuration.saveInventory;
    private static Map<UUID, InventoryPlayerEE> inventories = new HashMap<UUID, InventoryPlayerEE>();

    public static void put(EntityPlayer player, InventoryPlayerEE inventory) {
        inventories.put(player.getPersistentID(), inventory);
    }

    public static InventoryPlayerEE get(EntityPlayer player) {
        return inventories.getOrDefault(player.getPersistentID(), new InventoryPlayerEE(player));
    }

    public static void remove(EntityPlayer player) {
        inventories.remove(player.getPersistentID());
    }

    static int count(InventoryPlayer inv) {
        if (inv == null) return 0;
        if (inv.getSizeInventory() <= 0) return 0;
        AtomicInteger itemStackCount = new AtomicInteger(0);
        for (ItemStack stack : inv.mainInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        for (ItemStack stack : inv.armorInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        for (ItemStack stack : inv.offHandInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        return itemStackCount.get();
    }

    static int count(IInventory inv) {
        if (inv == null) return 0;
        if (inv.getSizeInventory() <= 0) return 0;
        AtomicInteger itemStackCount = new AtomicInteger(0);
        for (int i = 0; i < inv.getSizeInventory(); ++i)
            if (!inv.getStackInSlot(i).isEmpty()) itemStackCount.incrementAndGet();
        return itemStackCount.get();
    }

    private static void moveStacks(NonNullList<ItemStack> target, NonNullList<ItemStack> source) {
        if (target == null) {
            EasyExploration.logger.error("Target inventory missing.");
            return;
        }
        if (source == null) {
            EasyExploration.logger.error("Source inventory missing.");
            return;
        }
        if (target.size() != source.size()) {
            EasyExploration.logger.error("Target inventory has wrong size.");
            return;
        }
        for (int i = 0; i < target.size(); ++i) target.set(i, source.get(i));
        source.clear();
    }

    static InventoryPlayerEE keepInventory(EntityPlayer player) {
        InventoryPlayerEE inventory = new InventoryPlayerEE(player);

        if (config.equipment == InventoryOption.KEEP) {
            moveStacks(inventory.armorInventory, player.inventory.armorInventory);
            moveStacks(inventory.offHandInventory, player.inventory.offHandInventory);
        }
        if (config.loot == InventoryOption.KEEP) {
            moveStacks(inventory.mainInventory, player.inventory.mainInventory);
        }
        if (config.equipment == InventoryOption.SAVE) {
            inventory.inventoryDeathChest.moveStacks(player.inventory.armorInventory);
            inventory.inventoryDeathChest.moveStacks(player.inventory.offHandInventory);
        }
        if (config.loot == InventoryOption.SAVE) {
            inventory.inventoryDeathChest.moveStacks(player.inventory.mainInventory);
        }

        /*
        if (config.equipment == InventoryOption.DROP) {
            // happens automatically since we don't cancel the event and don't remove the items from the inventory
        }
        if (config.loot == InventoryOption.DROP) {
            // happens automatically since we don't cancel the event and don't remove the items from the inventory
        }
        */
        inventories.put(player.getPersistentID(), inventory);
        return inventory;
    }

    static void destroyVanishingCursedItems(InventoryPlayer inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
                inventory.removeStackFromSlot(i);
            }
        }
    }


}
