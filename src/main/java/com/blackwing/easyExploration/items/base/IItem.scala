package com.blackwing.easyExploration.items.base

import com.blackwing.easyExploration.EasyExploration
import com.blackwing.easyExploration.util.IHasRenderer
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

trait IItem extends Item with IHasRenderer {

	def init(): Unit = {
		setUnlocalizedName(getName)
		setRegistryName(getName)
		setCreativeTab(CreativeTabs.MATERIALS)

		EasyExploration.ITEMS.add(this)
	}

	def registerRenderer(): Unit = {
		EasyExploration.proxy.registerItemRenderer(this, 0, getId)
	}
}