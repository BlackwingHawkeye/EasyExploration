package com.blackwing.easyExploration.saveInventory

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

import com.blackwing.easyExploration.config.Configuration
import com.blackwing.easyExploration.config.Configuration.InventoryOption
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.inventory.InventoryBasic
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import org.apache.logging.log4j.Logger

import scala.collection.JavaConversions._
import scala.collection.mutable

object SaveInventory {

	private var logger: Logger = _

	def setLogger(logger: Logger): Unit = this.logger = logger

	private val config = Configuration.saveInventory
	private var inventories = mutable.Map[UUID, InventoryPlayer]()

	private[saveInventory] def put(player: EntityPlayer, inventory: InventoryPlayer) =
		inventories += player.getPersistentID -> inventory

	private[saveInventory] def get(player: EntityPlayer): InventoryPlayer =
		inventories.getOrElse(player.getPersistentID, new InventoryPlayer(player))

	private[saveInventory] def remove(player: EntityPlayer) =
		inventories -= player.getPersistentID

	private[saveInventory] def count(inv: InventoryPlayer): Int = {
		case null => 0
		case _ if inv.getSizeInventory <= 0 => 0
		case _ =>
			val itemStackCount = new AtomicInteger(0)
			for (stack <- inv.mainInventory) if (!stack.isEmpty) itemStackCount.incrementAndGet
			for (stack <- inv.armorInventory) if (!stack.isEmpty) itemStackCount.incrementAndGet
			for (stack <- inv.offHandInventory) if (!stack.isEmpty) itemStackCount.incrementAndGet
			itemStackCount.get
	}

	private[saveInventory] def count(inv: InventoryBasic): Int = {
		case null => 0
		case _ if inv.getSizeInventory <= 0 => 0
		case _ =>
			val itemStackCount = new AtomicInteger(0)
			for (slot <- Range(0, inv.getSizeInventory)) if (!inv.getStackInSlot(slot).isEmpty) itemStackCount.incrementAndGet
			itemStackCount.get
	}

	private def moveStacks(target: NonNullList[ItemStack], source: NonNullList[ItemStack]): Unit = {
		case (null, _) => logger.error("Target inventory missing.")
		case (_, null) => logger.error("Source inventory missing.")
		case _ if target.size != source.size => logger.error("Target inventory has wrong size.")
		case _ =>
			for (slot <- Range(0, target.size)) target.set(slot, source.get(slot))
			source.clear()
	}

	private def moveStacks(source: NonNullList[ItemStack], target: InventoryBasic): Unit = {
		case (null, _) => logger.warn("Source inventory missing.")
		case _ =>
			for (stack <- source) target.addItem(stack)
			source.clear()
	}

	private[saveInventory] def moveInventory(player: EntityPlayer) = {
		val inventory = new InventoryPlayer(player)
		val deathChest = new InventoryBasic(player.getName + "'s loot crate", true, inventory.armorInventory.size + inventory.offHandInventory.size + inventory.mainInventory.size)
		if (config.equipment eq InventoryOption.KEEP) {
			moveStacks(inventory.armorInventory, player.inventory.armorInventory)
			moveStacks(inventory.offHandInventory, player.inventory.offHandInventory)
		}
		if (config.loot eq InventoryOption.KEEP) moveStacks(inventory.mainInventory, player.inventory.mainInventory)
		if (config.equipment eq InventoryOption.SAVE) {
			moveStacks(player.inventory.armorInventory, deathChest)
			moveStacks(player.inventory.offHandInventory, deathChest)
		}
		if (config.loot eq InventoryOption.SAVE) moveStacks(player.inventory.mainInventory, deathChest)
		/*
		if (config.equipment == InventoryOption.DROP) {
			// happens automatically since we don't cancel the event and don't remove the items from the inventory
		}
		if (config.loot == InventoryOption.DROP) {
			// happens automatically since we don't cancel the event and don't remove the items from the inventory
		}
		*/
		inventories.put(player.getPersistentID, inventory)
		deathChest
	}

	private[saveInventory] def destroyVanishingCursedItems(inventory: InventoryPlayer): Unit = {
		for (slot <- Range(0, inventory.getSizeInventory)) {
			val itemstack = inventory.getStackInSlot(slot)
			if (!itemstack.isEmpty && EnchantmentHelper.hasVanishingCurse(itemstack)) inventory.removeStackFromSlot(slot)
		}
	}
}