package com.blackwing.easyExploration.saveInventory

import java.io.IOException

import com.blackwing.easyExploration.config.Configuration
import com.blackwing.easyExploration.utilities.{EventHandlerBase, FileStorage}
import net.minecraft.entity.Entity
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP, InventoryPlayer}
import net.minecraft.nbt.{CompressedStreamTools, NBTTagCompound, NBTTagList}
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.PlayerEvent.{Clone, LoadFromFile, SaveToFile}
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.{PlayerLoggedInEvent, PlayerLoggedOutEvent}

object SaveInventoryHandlerCommon extends EventHandlerBase {

	private val config = Configuration.saveInventory
	private val fileStorage = FileStorage.instance("saveinventory")

	private def canNotDo(entity: Entity): Boolean = {
		// feature is disabled
		if (!config.enabled) return true
		// not a player
		if (!entity.isInstanceOf[EntityPlayer]) return true
		// MC own features trump mine
		if (entity.world.getGameRules.getBoolean("keepInventory")) return true
		// player is not really here
		if (entity.asInstanceOf[EntityPlayer].isSpectator) return true
		// all right, let's do that
		false
	}

	/**
	  * The player is being loaded from the world save.
	  * Load an additional file from the players directory containing additional mod related player data.
	  */
	@SubscribeEvent def onLoad(event: LoadFromFile): Unit = {
		if (canNotDo(event.getEntity)) return
		val player = event.getEntityPlayer
		val playerInventory = new InventoryPlayer(player)

		try {
			val playerFile = fileStorage.getPlayerSaveFile(event)
			// this is legit if the player is new in this game world
			if (!playerFile.exists) logger.warn("Player file not found. " + playerFile.getPath)
			else {
				val compound = CompressedStreamTools.read(playerFile)
				if (compound == null) throw new IOException("Can't read from file " + playerFile.getPath)
				playerInventory.readFromNBT(compound.getTagList("Inventory", 10))
				playerInventory.currentItem = compound.getInteger("SelectedItemSlot")
				logger.info("Loaded {} item stacks for {}.", SaveInventory.count(playerInventory), player.getName)
			}
		} catch {
			case e: Exception =>
				logger.error("Could not load inventory for {}. {}", player.getName, e.getMessage)
				logger.catching(e)
		} finally SaveInventory.put(player, playerInventory)
	}

	/**
	  * If the player is a valid server side player, their data will be synced to the client.
	  * Syncs a client's data with the data that is on the server. This can only be called server side.
	  */
	@SubscribeEvent def onLoggedIn(event: PlayerLoggedInEvent): Unit = {
		if (canNotDo(event.player)) return
		val player = event.player.asInstanceOf[EntityPlayerMP]
		logger.info("Player {} logged in. Sending sync package to player client.", player.getName)
		// Send ClientProxy a package containing the inventory to be synced
		player.sendContainerToPlayer(player.inventoryContainer)
	}

	@SubscribeEvent def onDeath(event: LivingDeathEvent): Unit = {
		if (canNotDo(event.getEntity)) return
		val player = event.getEntity.asInstanceOf[EntityPlayer]
		val totalInventoryCount = SaveInventory.count(player.inventory)
		logger.info(player.getName + " died. Attempting to save inventory.")
		SaveInventory.destroyVanishingCursedItems(player.inventory)
		val deathChest = SaveInventory.moveInventory(player)
		logger.info("Kept {} and saved {} of {} item stacks.", SaveInventory.count(SaveInventory.get(player)), SaveInventory.count(deathChest), totalInventoryCount)
		// TODO: place deathChest
	}

	@SubscribeEvent def onClone(event: Clone): Unit = {
		// noone died
		if (!event.isWasDeath) return
		if (canNotDo(event.getEntity)) return
		val player = event.getEntityPlayer
		val playerInventory = SaveInventory.get(player)
		logger.info(player.getName + " respawned. Attempting to restore inventory.")
		player.inventory.copyInventory(playerInventory)
		logger.info("Restored {} of {} item stacks.", SaveInventory.count(player.inventory), SaveInventory.count(playerInventory))
		SaveInventory.remove(player)
	}

	@SubscribeEvent def onLoggedOut(event: PlayerLoggedOutEvent): Unit = {
		logger.info("Player {} logged out. Removing inventory store from memory.", event.player.getName)
		SaveInventory.remove(event.player) // needed, so that the inventory is not available on other (local) game worlds
	}

	/**
	  * The player is being saved to the world store.
	  * This event saves the additional mod related player data to the world store.
	  * <em>WARNING</em>: Do not overwrite the player's .dat file here. You will corrupt the world state.
	  */
	@SubscribeEvent def onSave(event: SaveToFile): Unit = {
		if (canNotDo(event.getEntity)) return
		val player = event.getEntityPlayer
		val playerInventory = SaveInventory.get(player)
		try {
			val compound = new NBTTagCompound
			compound.setTag("Inventory", playerInventory.writeToNBT(new NBTTagList))
			compound.setInteger("SelectedItemSlot", playerInventory.currentItem)
			CompressedStreamTools.safeWrite(compound, fileStorage.getPlayerSaveFile(event))
			logger.info("Saved {} item stacks for {}.", SaveInventory.count(playerInventory), player.getName)
		} catch {
			case e: Exception =>
				logger.error("Could not save inventory for {}. {}", player.getName, e.getMessage)
				logger.catching(e)
		}
	}

	//TODO: test if temp inventory is restored for other players (p1 dies, p2 dies, p1 respawns, p2 respawns)
	//TODO: place deathchest
	//TODO: lock deathchest
	//TODO: make deathchest indestructable
}