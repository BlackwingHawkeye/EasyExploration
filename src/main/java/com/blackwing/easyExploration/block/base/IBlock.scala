package com.blackwing.easyExploration.block.base

import com.blackwing.easyExploration.EasyExploration
import com.blackwing.easyExploration.util.IHasRenderer
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemBlock}

trait IBlock extends Block with IHasRenderer {

	def init(): Unit = {
		setUnlocalizedName(getName)
		setRegistryName(getName)
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS)

		EasyExploration.BLOCKS.add(this)
		EasyExploration.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName))
	}

	def registerRenderer(): Unit = {
		EasyExploration.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, getId)
	}
}
