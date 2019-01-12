package com.blackwing.easyExploration.proxy

import com.blackwing.easyExploration.config.ConfigurationHandlerCommon
import com.blackwing.easyExploration.saveInventory.{SaveInventory, SaveInventoryHandlerCommon}
import com.blackwing.easyExploration.showDamage.ShowDamageHandlerCommon
import com.blackwing.easyExploration.showDeathLocation.{ShowDeathLocationHandlerCommon, ShowDeathLocationHandlerCommon}
import com.blackwing.easyExploration.utilities.EventHandlerBase
import net.minecraft.item.Item
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}

object CommonProxy extends EventHandlerBase {

	val configurationHandler: ConfigurationHandlerCommon.type = ConfigurationHandlerCommon
	val saveInventoryHandler: SaveInventoryHandlerCommon.type = SaveInventoryHandlerCommon
	val showDeathLocationHandler: ShowDeathLocationHandlerCommon.type = ShowDeathLocationHandlerCommon
	val showDamageHandler: ShowDamageHandlerCommon.type = ShowDamageHandlerCommon

	/*
	 * This is where we load our network configuration, mod configuration, .. and where we initialize our items and blocks
	 */
	@Mod.EventHandler
	override def onPreInit(event: FMLPreInitializationEvent): Unit = {
		// register event handlers
		super.onPreInit(event)
		configurationHandler.onPreInit(event)
		saveInventoryHandler.onPreInit(event)
		showDeathLocationHandler.onPreInit(event)
		showDamageHandler.onPreInit(event)
		// register Messages
		//GameRegistry.registerWorldGenerator(new ModWorldGen(), 3)
	}

	/*
	 * This is where we register our GUIs, tile entities, crafting recipes, ..
	 */
	@Mod.EventHandler
	override def onInit(event: FMLInitializationEvent): Unit = {
		super.onInit(event)
		SaveInventory.setLogger(logger)
		//ModRecipes.init()
	}

	def registerItemRenderer(item: Item, meta: Int, id: String): Unit = {}
}
