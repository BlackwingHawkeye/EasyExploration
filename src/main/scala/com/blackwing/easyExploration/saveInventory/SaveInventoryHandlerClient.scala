package com.blackwing.easyExploration.saveInventory

import net.minecraft.entity.Entity
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.gameevent.PlayerEvent.{PlayerLoggedInEvent, PlayerLoggedOutEvent}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
object SaveInventoryHandlerClient extends SaveInventoryHandlerCommon.type {

	/**
	  * Returns true if the given entity is the player using this client
	  *
	  * @param entity Entity of the event
	  * @return true if the given entity is the player using this client
	  */
	private def isClientPlayer(entity: Entity): Boolean = try {
		if (FMLClientHandler.instance.getClientPlayerEntity == null) true
		else entity.getUniqueID eq FMLClientHandler.instance.getClientPlayerEntity.getUniqueID
	} catch {
		case e: Exception =>
			logger.catching(e)
			false
	}

	override def onLoad(event: PlayerEvent.LoadFromFile): Unit =
		if (isClientPlayer(event.getEntity)) super.onLoad(event) // only deal with your own load

	override def onLoggedIn(event: PlayerLoggedInEvent): Unit = {} // do nothing since we can't login on a client

	override def onDeath(event: LivingDeathEvent): Unit =
		if (isClientPlayer(event.getEntity)) super.onDeath(event) // only deal with your own death

	override def onClone(event: PlayerEvent.Clone): Unit =
		if (isClientPlayer(event.getEntity)) super.onClone(event) // only deal with your own death

	// only deal with your own spawn
	override def onLoggedOut(event: PlayerLoggedOutEvent): Unit =
		if (isClientPlayer(event.player)) super.onLoggedOut(event) // only deal with your own death

	// only deal with your own logout
	override def onSave(event: PlayerEvent.SaveToFile): Unit =
		if (isClientPlayer(event.getEntity)) super.onSave(event) // only deal with your own save
}