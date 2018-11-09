package com.bwhe.easyExploration.saveInventory;

import com.bwhe.easyExploration.EasyExplorationEventHandlerBasic;
import com.bwhe.easyExploration.EasyExplorationFileStorage;
import com.bwhe.easyExploration.config.EasyExplorationConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile;
import net.minecraftforge.event.entity.player.PlayerEvent.SaveToFile;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import java.io.File;
import java.io.IOException;

public class SaveInventoryEventHandlerCommon extends EasyExplorationEventHandlerBasic {

    private EasyExplorationConfig.SubCategorySaveInventory config;
    private EasyExplorationFileStorage fileStorage;
    private SaveInventory inventories;

    public SaveInventoryEventHandlerCommon() {
        this.config = EasyExplorationConfig.saveInventory;
        this.fileStorage = new EasyExplorationFileStorage("saveinventory");
        this.inventories = new SaveInventory(logger);
    }

    private boolean canNotDo(Entity entity) {
        // feature is disabled
        if (!config.enabled) return true;
        // not a player
        if (!(entity instanceof EntityPlayer)) return true;
        // MC own features trump mine
        if (entity.world.getGameRules().getBoolean("keepInventory")) return true;
        // player is not really here
        if (((EntityPlayer) entity).isSpectator()) return true;
        // all right, let's do that
        return false;
    }

    /**
     * The player is being loaded from the world save.
     * Load an additional file from the players directory containing additional mod related player data.
     */
    @SubscribeEvent
    public void onLoad(LoadFromFile event) {
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = event.getEntityPlayer();
        final InventoryPlayer playerInventory = new InventoryPlayer(player);

        try {
            final File playerFile = fileStorage.getPlayerSaveFile(event);
            final NBTTagCompound compound = CompressedStreamTools.read(playerFile);
            if (compound == null) throw new IOException("Can't read from file " + playerFile.getName());

            playerInventory.readFromNBT(compound.getTagList("Inventory", 10));
            playerInventory.currentItem = compound.getInteger("SelectedItemSlot");

            logger.info("Loaded {} item stacks for {}.", SaveInventory.count(playerInventory), player.getName());
        } catch (final Exception e) {
            logger.error("Could not load inventory for {}. {}", player.getName(), e.getMessage());
            logger.catching(e);
        } finally {
            inventories.put(player, playerInventory);
        }
    }

    /**
     * If the player is a valid server side player, their data will be synced to the client.
     * Syncs a client's data with the data that is on the server. This can only be called server side.
     */
    @SubscribeEvent
    public void onLoggedIn(PlayerLoggedInEvent event) {
        // should we do something?
        if (canNotDo(event.player)) return;
        final EntityPlayer player = event.player;
        final InventoryPlayer playerInventory = inventories.get(player);

        // TODO: Send Client a package containing the inventory to be synced
        // EasyExploration.NETWORK.sendTo(new PacketSyncClient(playerInventory), player);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = (EntityPlayer) event.getEntity();
        final int totalInventoryCount = SaveInventory.count(player.inventory);

        if (totalInventoryCount == 0) logger.info(player.getName() + " died.");
        else {
            logger.info(player.getName() + " died. Attempting to save inventory.");
            inventories.destroyVanishingCursedItems(player.inventory);
            InventoryBasic deathChest = inventories.moveInventory(player);
            logger.info("Kept {} and saved {} of {} item stacks.", SaveInventory.count(inventories.get(player)), SaveInventory.count(deathChest), totalInventoryCount);
            // TODO: place deathChest
        }
    }

    @SubscribeEvent
    public void onClone(Clone event) {
        // noone died
        if (!event.isWasDeath()) return;
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = event.getEntityPlayer();
        final InventoryPlayer playerInventory = inventories.get(player);

        if (playerInventory.isEmpty()) logger.info(player.getName() + " respawned.");
        else {
            logger.info(player.getName() + " respawned. Attempting to restore inventory.");
            player.inventory.copyInventory(playerInventory);
            logger.info("Restored {} of {} item stacks.", SaveInventory.count(player.inventory), SaveInventory.count(playerInventory));
        }
        inventories.remove(player);
    }

    @SubscribeEvent
    public void onLoggedOut(PlayerLoggedOutEvent event) {
        inventories.remove(event.player); // needed, so that the inventory is not available on other (local) game worlds
    }

    /**
     * The player is being saved to the world store.
     * This event saves the additional mod related player data to the world store.
     * <em>WARNING</em>: Do not overwrite the player's .dat file here. You will corrupt the world state.
     */
    @SubscribeEvent
    public void onSave(SaveToFile event) {
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = event.getEntityPlayer();
        final InventoryPlayer playerInventory = inventories.get(player);

        try {
            if (!playerInventory.isEmpty()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setTag("Inventory", playerInventory.writeToNBT(new NBTTagList()));
                compound.setInteger("SelectedItemSlot", playerInventory.currentItem);

                CompressedStreamTools.safeWrite(compound, fileStorage.getPlayerSaveFile(event));

                logger.info("Saved {} item stacks for {}.", SaveInventory.count(playerInventory), player.getName());
            }
        } catch (Exception e) {
            logger.error("Could not save inventory for {}. {}", player.getName(), e.getMessage());
            logger.catching(e);
        } finally {
            inventories.remove(player);
        }
    }

//TODO: on login/logout make stuff not available for same player on other gameworlds (p1 dies, logout, change world, login, respawn)
//TODO: on login/logout write to file to make stuff available aften game restart (p1 dies, logout, quit game, start game, login, respawn)
//TODO: test if temp inventory is restored for other players (p1 dies, p2 dies, p1 respawns, p2 respawns)
//TODO: place deathchest
//TODO: lock deathchest
//TODO: make deathchest indestructable
}
