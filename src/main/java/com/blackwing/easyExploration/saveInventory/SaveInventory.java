package com.blackwing.easyExploration.saveInventory;

import com.blackwing.easyExploration.config.Configuration;
import com.blackwing.easyExploration.config.Configuration.InventoryOption;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.Logger;

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

    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private Configuration.SubCategorySaveInventory config = Configuration.saveInventory;
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

    static int count(IInventory inv) {
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

    InventoryBasic keepInventory(EntityPlayer player) {
        InventoryPlayer keepInventory = new InventoryPlayer(player);
        InventoryBasic storeInventory = new InventoryBasic(player.getName(), true, keepInventory.armorInventory.size() + keepInventory.offHandInventory.size() + keepInventory.mainInventory.size());
        if (config.equipment == InventoryOption.KEEP) {
            moveStacks(keepInventory.armorInventory, player.inventory.armorInventory);
            moveStacks(keepInventory.offHandInventory, player.inventory.offHandInventory);
        }
        if (config.loot == InventoryOption.KEEP) {
            moveStacks(keepInventory.mainInventory, player.inventory.mainInventory);
        }
        if (config.equipment == InventoryOption.SAVE) {
            moveStacks(player.inventory.armorInventory, storeInventory);
            moveStacks(player.inventory.offHandInventory, storeInventory);
        }
        if (config.loot == InventoryOption.SAVE) {
            moveStacks(player.inventory.mainInventory, storeInventory);
        }
        /*
        if (config.equipment == InventoryOption.DROP) {
            // happens automatically since we don't cancel the event and don't remove the items from the inventory
        }
        if (config.loot == InventoryOption.DROP) {
            // happens automatically since we don't cancel the event and don't remove the items from the inventory
        }
        */
        inventories.put(player.getPersistentID(), keepInventory);
        return storeInventory;
    }

    void storeInventory(InventoryBasic storeInventory, TileEntityChest storeContainer) {
        if (storeContainer.getSizeInventory() < storeInventory.getSizeInventory()) {
            logger.error("death chest can hold only {}/{} item stacks {}",
                    storeContainer.getSizeInventory(), storeInventory.getSizeInventory(), storeContainer.getClass());
            return;
        }
        for (int i = 0; i < storeInventory.getSizeInventory(); ++i)
            storeContainer.setInventorySlotContents(i, storeInventory.getStackInSlot(i));
        storeInventory.clear();
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
