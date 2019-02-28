package com.blackwing.easyExploration.items.base

import com.blackwing.easyExploration.init.RegisterHandler
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

trait IItem extends Item {

	def setId(id: String): Unit

	def getId: String

	def init(id: String, tab: CreativeTabs): Unit = {
		setId(id)
		setUnlocalizedName(id)
		setRegistryName(id)
		setCreativeTab(tab)

		RegisterHandler.ITEMS.add(this)
	}
}