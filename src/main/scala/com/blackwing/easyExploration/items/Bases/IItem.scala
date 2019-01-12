package com.blackwing.easyExploration.items.Bases

import com.blackwing.easyExploration.EasyExploration
import com.blackwing.easyExploration.utilities.IHasModel
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

/**
  * Using this trait, we can easily determine by isInstanceOf(IHasModel) if an item is one ours.
  */
trait IItem extends Item with IHasModel {

	val name: String
	val tab: CreativeTabs

	setUnlocalizedName(name)
	setRegistryName(name)
	setCreativeTab(tab)
	EasyExploration.items :+= this

	def registerModels(): Unit = EasyExploration.proxy.registerItemRenderer(this, 0, "inventory")
}
