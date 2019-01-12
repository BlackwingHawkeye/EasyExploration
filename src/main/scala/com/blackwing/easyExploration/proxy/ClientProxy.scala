package com.blackwing.easyExploration.proxy

import com.blackwing.easyExploration.saveInventory.{SaveInventoryHandlerClient, SaveInventoryHandlerCommon}
import com.blackwing.easyExploration.showDamage.{ShowDamageHandlerClient, ShowDamageHandlerCommon}
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader

object ClientProxy extends CommonProxy.type {

	override val saveInventoryHandler: SaveInventoryHandlerCommon.type = SaveInventoryHandlerClient()
	override val showDamageHandler: ShowDamageHandlerCommon.type = ShowDamageHandlerClient()

	override def registerItemRenderer(item: Item, meta: Int, id: String): Unit =
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName, id))
}