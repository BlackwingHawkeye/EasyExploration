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

    private Logger logger;
    private EasyExplorationConfig.SubCategorySaveInventory config;
    private Map<UUID, InventoryPlayer> inventories;

    public SaveInventory(Logger logger) {
        this.logger = logger;
        this.config = EasyExplorationConfig.saveInventory;
        this.inventories = new HashMap<UUID, InventoryPlayer>();
    }

    public InventoryPlayer put(EntityPlayer player, InventoryPlayer inventory) {
        return inventories.put(player.getPersistentID(), inventory);
    }

    public InventoryPlayer get(EntityPlayer player) {
        return inventories.getOrDefault(player.getPersistentID(), new InventoryPlayer(player));
    }

    public InventoryPlayer remove(EntityPlayer player) {
        return inventories.remove(player.getPersistentID());
    }

    public static int count(InventoryPlayer inv) {
        if (inv == null) return 0;
        if (inv.getSizeInventory() <= 0) return 0;
        AtomicInteger itemStackCount = new AtomicInteger(0);
        for (ItemStack stack : inv.mainInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        for (ItemStack stack : inv.armorInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        for (ItemStack stack : inv.offHandInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        return itemStackCount.get();
    }

    public static int count(InventoryBasic inv) {
        if (inv == null) return 0;
        if (inv.getSizeInventory() <= 0) return 0;
        AtomicInteger itemStackCount = new AtomicInteger(0);
        for (int i = 0; i < inv.getSizeInventory(); ++i)
            if (!inv.getStackInSlot(i).isEmpty()) itemStackCount.incrementAndGet();
        return itemStackCount.get();
    }

    public void moveStacks(NonNullList<ItemStack> target, NonNullList<ItemStack> source) {
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

    public void moveStacks(NonNullList<ItemStack> source, InventoryBasic target) {
        if (source == null) {
            logger.warn("source inventory missing.");
            return;
        }
        for (ItemStack stack : source) target.addItem(stack);
        source.clear();
    }

    public InventoryBasic moveInventory(EntityPlayer player) {
        InventoryPlayer inventory = new InventoryPlayer(player);
        InventoryBasic deathChest = new InventoryBasic(player.getName() + "'s loot crate", true, inventory.armorInventory.size() + inventory.offHandInventory.size() + inventory.mainInventory.size());
        if (config.equipment == InventoryOption.KEEP) {
            logger.info("Keeping equipment.");
            moveStacks(inventory.armorInventory, player.inventory.armorInventory);
            moveStacks(inventory.offHandInventory, player.inventory.offHandInventory);
        }
        if (config.loot == InventoryOption.KEEP) {
            logger.info("Keeping loot.");
            moveStacks(inventory.mainInventory, player.inventory.mainInventory);
        }
        if (config.equipment == InventoryOption.SAVE) {
            logger.info("Saving equipment.");
            moveStacks(player.inventory.armorInventory, deathChest);
            moveStacks(player.inventory.offHandInventory, deathChest);
        }
        if (config.loot == InventoryOption.SAVE) {
            logger.info("Saving loot.");
            moveStacks(player.inventory.mainInventory, deathChest);
        }
        if (config.equipment == InventoryOption.DROP) {
            logger.info("Dropping equipment.");
            // happens automatically since we don't cancel the event and don't remove the items from the inventory
        }
        if (config.loot == InventoryOption.DROP) {
            logger.info("Dropping loot.");
            // happens automatically since we don't cancel the event and don't remove the items from the inventory
        }
        inventories.put(player.getPersistentID(), inventory);
        return deathChest;
    }

    public void destroyVanishingCursedItems(InventoryPlayer inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
                inventory.removeStackFromSlot(i);
            }
        }
    }

}
