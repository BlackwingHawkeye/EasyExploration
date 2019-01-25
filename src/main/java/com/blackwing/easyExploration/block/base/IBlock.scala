package com.blackwing.easyExploration.block.base

import com.blackwing.easyExploration.init.RegisterHandler
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemBlock

trait IBlock extends Block {

	def setId(id: String): Unit

	def getId: String

	def init(id: String) {
		setId(id)
		setUnlocalizedName(id)
		setRegistryName(id)
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS)

		RegisterHandler.BLOCKS.add(this)
		RegisterHandler.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName))
	}
}
