package com.bwhe.easyExploration.saveInventory;

import com.bwhe.easyExploration.EasyExplorationConfig;
import com.bwhe.easyExploration.EasyExplorationConfig.InventoryOption;
import com.bwhe.easyExploration.EasyExplorationConfig.SubCategorySaveInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class EventHandler {

    private Logger logger;

    public EventHandler(Logger logger) {
        this.logger = logger;
    }

    private InventoryPlayer inventory = null;
    private InventoryBasic deathChest = null;

    private SubCategorySaveInventory config = EasyExplorationConfig.saveInventory;

    private int countInventoryPlayer(InventoryPlayer inv) {
        if (inv == null) return 0;
        if (inv.getSizeInventory() <= 0) return 0;
        AtomicInteger itemStackCount = new AtomicInteger(0);
        for (ItemStack stack : inv.mainInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        for (ItemStack stack : inv.armorInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        for (ItemStack stack : inv.offHandInventory) if (!stack.isEmpty()) itemStackCount.incrementAndGet();
        return itemStackCount.get();
    }

    private int countInventoryBasic(InventoryBasic inv) {
        if (inv == null) return 0;
        if (inv.getSizeInventory() <= 0) return 0;
        AtomicInteger itemStackCount = new AtomicInteger(0);
        for (int i = 0; i < inv.getSizeInventory(); ++i)
            if (!inv.getStackInSlot(i).isEmpty()) itemStackCount.incrementAndGet();
        return itemStackCount.get();
    }

    private void keepStacks(NonNullList<ItemStack> target, NonNullList<ItemStack> source) {
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

    private void safeStacks(NonNullList<ItemStack> source) {
        if (source == null) {
            logger.warn("source inventory missing.");
            return;
        }
        for (ItemStack stack : source) deathChest.addItem(stack);
        source.clear();
    }

    private void moveInventory(EntityPlayerMP player) {
        inventory = new InventoryPlayer(player);
        deathChest = new InventoryBasic(player.getName() + "'s loot crate", true, inventory.armorInventory.size() + inventory.offHandInventory.size() + inventory.mainInventory.size());
        if (config.equipment == InventoryOption.KEEP) {
            logger.info("Keeping equipment.");
            keepStacks(inventory.armorInventory, player.inventory.armorInventory);
            keepStacks(inventory.offHandInventory, player.inventory.offHandInventory);
        }
        if (config.loot == InventoryOption.KEEP) {
            logger.info("Keeping loot.");
            keepStacks(inventory.mainInventory, player.inventory.mainInventory);
        }
        if (config.equipment == InventoryOption.SAVE) {
            logger.info("Saving equipment.");
            safeStacks(player.inventory.armorInventory);
            safeStacks(player.inventory.offHandInventory);
        }
        if (config.loot == InventoryOption.SAVE) {
            logger.info("Saving loot.");
            safeStacks(player.inventory.mainInventory);
        }
        if (config.equipment == InventoryOption.DROP) {
            logger.info("Dropping equipment.");
            // happens automatically since we don't cancel the event and don't remove the items from the inventory
        }
        if (config.loot == InventoryOption.DROP) {
            logger.info("Dropping loot.");
            // happens automatically since we don't cancel the event and don't remove the items from the inventory
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        // feature is disabled
        if (!config.enabled) return;
        // someone else / something else died
        if (!(event.getEntity() instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        logger.info(player.getName() + " died. Attempting to save inventory.", player);
        int playerInventoryCount = countInventoryPlayer(player.inventory);
        moveInventory(player);
        logger.info("Kept " + countInventoryPlayer(inventory) + " and saved " + countInventoryBasic(deathChest) + " of " + playerInventoryCount + " item stacks.");
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        // feature is disabled
        if (!config.enabled) return;
        // someone else / something else was cloned
        if (!(event.isWasDeath() && event.getEntity() instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        logger.info(player.getName() + " respawned. Attempting to restore inventory.", event.getOriginal());
        if (inventory == null) {
            logger.warn("No saved inventory found.");
            return;
        }
        if (inventory.player.getGameProfile().getId() != player.getGameProfile().getId()) {
            logger.error("No saved inventory found for player" + player.getName() + ".");
            return;
        }
        player.inventory.copyInventory(inventory);
        logger.info("Restored " + countInventoryPlayer(player.inventory) + "/" + countInventoryPlayer(inventory) + " item stacks.");
    }

    //TODO: on login/logout make stuff not available for same player on other gameworlds (p1 dies, logout, change world, login, respawn)
    //TODO: on login/logout write to file to make stuff available aften game restart (p1 dies, logout, quit game, start game, login, respawn)
    //TODO: test if temp inventory is restored for other players (p1 dies, p2 dies, p1 respawns, p2 respawns)
    //TODO: place deathchest
    //TODO: lock deathchest
    //TODO: make deathchest indestructable
}
