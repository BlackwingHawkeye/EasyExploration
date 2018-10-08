package com.tp.easyExploration.keepInventory;

import com.tp.easyExploration.config.Config;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class EventHandler {

    private Logger logger = null;

    public EventHandler(Logger logger) {
        this.logger = logger;
    }

    private InventoryPlayer inventory = null;
    private InventoryBasic lootCrate = null;

    private class ItemStackCounter {
        private int i;

        ItemStackCounter(int init) {
            this.i = init;
        }

        void inc() {
            this.i++;
        }

        void countItemStack(ItemStack itemStack) {
            if (!itemStack.isEmpty()) this.inc();
        }

        @Override
        public String toString() {
            return String.valueOf(this.i);
        }
    }

    private int countInventory(InventoryPlayer inv) {
        ItemStackCounter itemStackCount = new ItemStackCounter(0);
        inv.mainInventory.forEach(itemStackCount::countItemStack);
        inv.armorInventory.forEach(itemStackCount::countItemStack);
        inv.offHandInventory.forEach(itemStackCount::countItemStack);
        return itemStackCount.i;
    }

    private void keepStacks(NonNullList<ItemStack> target, NonNullList<ItemStack> source) {
        target.addAll(source);
        source.clear();
    }

    private void safeStacks(NonNullList<ItemStack> source) {
        source.forEach(stack -> lootCrate.addItem(stack));
        source.clear();
    }

    private void moveInventory(EntityPlayerMP player) {
        inventory = new InventoryPlayer(player);
        lootCrate = new InventoryBasic(player.getName() + "'s loot crate", true, inventory.armorInventory.size() + inventory.offHandInventory.size() + inventory.mainInventory.size());
        if (Config.keepInventoryEquipment.equals(Config.PROPERTY_VALUE_KEEP)) {
            keepStacks(inventory.armorInventory, player.inventory.armorInventory);
            keepStacks(inventory.offHandInventory, player.inventory.offHandInventory);
        }
        if (Config.keepInventoryLoot.equals(Config.PROPERTY_VALUE_KEEP)) {
            keepStacks(inventory.mainInventory, player.inventory.mainInventory);
        }
        if (Config.keepInventoryEquipment.equals(Config.PROPERTY_VALUE_SAVE)) {
            safeStacks(player.inventory.armorInventory);
            safeStacks(player.inventory.offHandInventory);
        }
        if (Config.keepInventoryLoot.equals(Config.PROPERTY_VALUE_SAVE)) {
            safeStacks(player.inventory.mainInventory);
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (Config.keepInventoryEnabled) {
            if (event.getEntity() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
                logger.info(player.getName() + " died. Attempting to save inventory.", player);
                inventory = new InventoryPlayer(player);
                inventory.copyInventory(player.inventory);
                logger.info("Saved " + countInventory(inventory) + "/" + countInventory(player.inventory) + " item stacks.");
                player.inventory.clear();
            }
        }
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        if (Config.keepInventoryEnabled) {
            if (event.isWasDeath() && event.getEntity() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
                logger.info(player.getName() + " respawned. Attempting to restore inventory.", event.getOriginal());
                if (inventory == null) {
                    logger.info("Failed: no saved inventory found.");
                } else {
                    player.inventory.copyInventory(inventory);
                    logger.info("Restored " + countInventory(player.inventory) + "/" + countInventory(inventory) + " item stacks.");
                }
            }
        }
    }
}

