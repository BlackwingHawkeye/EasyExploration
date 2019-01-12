package com.blackwing.easyExploration.blocks.Bases

import com.blackwing.easyExploration.EasyExploration
import com.blackwing.easyExploration.utilities.IHasModel
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemBlock}

class BlockBase(name: String, material: Material) extends Block(material) with IHasModel {

	val tab: CreativeTabs = CreativeTabs.BUILDING_BLOCKS

	setUnlocalizedName(name)
	setRegistryName(name)
	setCreativeTab(tab)
	EasyExploration.blocks :+= this
	EasyExploration.items :+= new ItemBlock(this).setRegistryName(this.getRegistryName)

	def registerModels(): Unit = EasyExploration.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory")
}
