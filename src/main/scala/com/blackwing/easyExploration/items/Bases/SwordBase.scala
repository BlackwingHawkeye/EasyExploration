package com.blackwing.easyExploration.items.Bases

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemSword}

class SwordBase(val name: String, material: Item.ToolMaterial) extends ItemSword(material) with IItem {
	val tab: CreativeTabs = CreativeTabs.COMBAT
}