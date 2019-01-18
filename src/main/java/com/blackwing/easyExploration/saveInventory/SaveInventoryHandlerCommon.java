package com.blackwing.easyExploration.saveInventory;

import com.blackwing.easyExploration.config.Configuration;
import com.blackwing.easyExploration.init.Blocks;
import com.blackwing.easyExploration.inventory.InventoryPlayerEE;
import com.blackwing.easyExploration.tileEntity.TileEntityDeathChest;
import com.blackwing.easyExploration.util.EventHandlerBase;
import com.blackwing.easyExploration.util.FileStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile;
import net.minecraftforge.event.entity.player.PlayerEvent.SaveToFile;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.io.File;
import java.io.IOException;

public class SaveInventoryHandlerCommon extends EventHandlerBase {

    private static final Configuration.SubCategorySaveInventory config = Configuration.saveInventory;
    private static final FileStorage fileStorage = FileStorage.instance(FileStorage.FEATURE_KEY_SAVEINVENTORY);

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
        final InventoryPlayerEE playerInventory = new InventoryPlayerEE(player);

        try {
            final File playerFile = fileStorage.getPlayerSaveFile(event);
            // this is legit if the player is new in this game world
            if (!playerFile.exists()) logger.warn("Player file not found. " + playerFile.getPath());
            else {
                final NBTTagCompound compound = CompressedStreamTools.read(playerFile);
                if (compound == null) throw new IOException("Can't read from file " + playerFile.getPath());

                playerInventory.readFromNBT(compound.getTagList("Inventory", 10));
                playerInventory.currentItem = compound.getInteger("SelectedItemSlot");

                logger.info("Loaded {} item stacks for {}.", SaveInventory.count(playerInventory), player.getName());
            }
        } catch (final Exception e) {
            logger.error("Could not load inventory for {}. {}", player.getName(), e.getMessage());
            logger.catching(e);
        } finally {
            SaveInventory.put(player, playerInventory);
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
        EntityPlayerMP player = (EntityPlayerMP) event.player;

        logger.info("Player {} logged in. Sending sync package to player client.", player.getName());
        // Send Client a package containing the inventory to be synced
        player.sendContainerToPlayer(player.inventoryContainer);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = (EntityPlayer) event.getEntity();
        final int totalInventoryCount = SaveInventory.count(player.inventory);

        logger.info(player.getName() + " died. Attempting to save inventory.");
        SaveInventory.destroyVanishingCursedItems(player.inventory);
        InventoryPlayerEE inventory = SaveInventory.keepInventory(player);

        if (inventory.inventoryDeathChest.itemCount > 0) {
            BlockPos pos = Blocks.DEATH_CHEST.placeDeathChest(player);

            TileEntity tileEntity = player.world.getTileEntity(pos);
            if (!(tileEntity instanceof TileEntityDeathChest)) return;
            TileEntityDeathChest tileEntityDeathChest = (TileEntityDeathChest) tileEntity;
            tileEntityDeathChest.setOwner(player);
            tileEntityDeathChest.setInventory(inventory.inventoryDeathChest);
        }

        logger.info("Kept {} and stored {} of {} item stacks.", SaveInventory.count(inventory), inventory.inventoryDeathChest.stackCount, totalInventoryCount);
    }

    @SubscribeEvent
    public void onClone(Clone event) {
        // noone died
        if (!event.isWasDeath()) return;
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = event.getEntityPlayer();
        final InventoryPlayer playerInventory = SaveInventory.get(player);

        logger.info(player.getName() + " respawned. Attempting to restore inventory.");
        player.inventory.copyInventory(playerInventory);
        logger.info("Restored {} of {} item stacks.", SaveInventory.count(player.inventory), SaveInventory.count(playerInventory));

        SaveInventory.remove(player);
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
        final InventoryPlayer playerInventory = SaveInventory.get(player);

        try {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("Inventory", playerInventory.writeToNBT(new NBTTagList()));
            compound.setInteger("SelectedItemSlot", playerInventory.currentItem);

            CompressedStreamTools.safeWrite(compound, fileStorage.getPlayerSaveFile(event));

            logger.info("Saved {} item stacks for {}.", SaveInventory.count(playerInventory), player.getName());
        } catch (Exception e) {
            logger.error("Could not save inventory for {}. {}", player.getName(), e.getMessage());
            logger.catching(e);
        }
    }

    //TODO: test if temp inventory is restored for other players (p1 dies, p2 dies, p1 respawns, p2 respawns)
    //TODO: place deathchest
    //TODO: lock deathchest
    // TODO: store xp
    // TODO: try to find a better spot to place death chest (re: void, lava)
    // TODO: add config and checks for death reasons MOB, LAVA, VOID
    //TODO: save/load deathchest from/into filestorage
}
