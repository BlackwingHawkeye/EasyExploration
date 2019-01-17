package com.blackwing.easyExploration.block.base

import com.blackwing.easyExploration.EasyExploration
import com.blackwing.easyExploration.util.IHasModel
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemBlock}

trait IBlock extends Block with IHasModel {
	def initBlock(name: String) {
		setUnlocalizedName(name)
		setRegistryName(name)
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
		EasyExploration.BLOCKS.add(this)
		EasyExploration.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName))
	}

	@Override
	override def registerModels(): Unit = {
		EasyExploration.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory")
	}
}
