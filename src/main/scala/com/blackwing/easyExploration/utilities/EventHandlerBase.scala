package com.blackwing.easyExploration.utilities

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import org.apache.logging.log4j.Logger

trait EventHandlerBase {

	val instance: EventHandlerBase = this

	protected var logger: Logger = _

	@Mod.EventHandler
	def onPreInit(event: FMLPreInitializationEvent): Unit = {
		logger = event.getModLog
		MinecraftForge.EVENT_BUS.register(this)
	}

	@Mod.EventHandler
	def onInit(event: FMLInitializationEvent): Unit = {}

	@Mod.EventHandler
	def onPostInit(event: FMLPostInitializationEvent): Unit = {}
}
