package com.bwhe.easyExploration.saveInventory;

import com.bwhe.easyExploration.config.EasyExplorationConfig;
import com.bwhe.easyExploration.config.EasyExplorationConfig.InventoryOption;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SaveInventory {

    /**
     * The singleton
     */
    private static final SaveInventory INSTANCE = new SaveInventory();

    /**
     * @return the instance
     */
    public static SaveInventory instance() {
        return INSTANCE;
    }

    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private EasyExplorationConfig.SubCategorySaveInventory config = EasyExplorationConfig.saveInventory;
    private static Map<UUID, InventoryPlayer> inventories = new HashMap<UUID, InventoryPlayer>();

    void put(EntityPlayer player, InventoryPlayer inventory) {
        inventories.put(player.getPersistentID(), inventory);
    }

    InventoryPlayer get(EntityPlayer player) {
        return inventories.getOrDefault(player.getPersistentID(), new InventoryPlayer(player));
    }

    void remove(EntityPlayer player) {
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

    static int count(InventoryBasic inv) {
        if (inv == null) return 0;
        if (inv.getSizeInventory() <= 0) return 0;
        AtomicInteger itemStackCount = new AtomicInteger(0);
        for (int i = 0; i < inv.getSizeInventory(); ++i)
            if (!inv.getStackInSlot(i).isEmpty()) itemStackCount.incrementAndGet();
        return itemStackCount.get();
    }

    private void moveStacks(NonNullList<ItemStack> target, NonNullList<ItemStack> source) {
        if (target == null) {
            logger.error("Target inventory missing.");
            return;
        }
        if (source == null) {
            logger.error("Source inventory missing.");
            return;
        }
        if (target.size() != source.size()) {
            logger.error("Target inventory has wrong size.");
            return;
        }
        for (int i = 0; i < target.size(); ++i) target.set(i, source.get(i));
        source.clear();
    }

    private void moveStacks(NonNullList<ItemStack> source, InventoryBasic target) {
        if (source == null) {
            logger.warn("source inventory missing.");
            return;
        }
        for (ItemStack stack : source) target.addItem(stack);
        source.clear();
    }

    InventoryBasic moveInventory(EntityPlayer player) {
        InventoryPlayer inventory = new InventoryPlayer(player);
        InventoryBasic deathChest = new InventoryBasic(player.getName() + "'s loot crate", true, inventory.armorInventory.size() + inventory.offHandInventory.size() + inventory.mainInventory.size());
        if (config.equipment == InventoryOption.KEEP) {
            moveStacks(inventory.armorInventory, player.inventory.armorInventory);
            moveStacks(inventory.offHandInventory, player.inventory.offHandInventory);
        }
        if (config.loot == InventoryOption.KEEP) {
            moveStacks(inventory.mainInventory, player.inventory.mainInventory);
        }
        if (config.equipment == InventoryOption.SAVE) {
            moveStacks(player.inventory.armorInventory, deathChest);
            moveStacks(player.inventory.offHandInventory, deathChest);
        }
        if (config.loot == InventoryOption.SAVE) {
            moveStacks(player.inventory.mainInventory, deathChest);
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
        return deathChest;
    }

    void destroyVanishingCursedItems(InventoryPlayer inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
                inventory.removeStackFromSlot(i);
            }
        }
    }

}
