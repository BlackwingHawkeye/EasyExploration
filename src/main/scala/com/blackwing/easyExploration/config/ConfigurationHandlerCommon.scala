package com.blackwing.easyExploration.config

import com.blackwing.easyExploration.EasyExploration.modId
import com.blackwing.easyExploration.utilities.EventHandlerBase
import net.minecraftforge.common.config.{Config, ConfigManager}
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ConfigurationHandlerCommon extends EventHandlerBase {

	@Mod.EventHandler
	override def onInit(event: FMLInitializationEvent): Unit =
		ConfigManager.sync(modId, Config.Type.INSTANCE)

	@SubscribeEvent
	def onConfigChangedEvent(event: ConfigChangedEvent.OnConfigChangedEvent): Unit =
		if (event.getModID == modId) ConfigManager.sync(modId, Config.Type.INSTANCE)
}