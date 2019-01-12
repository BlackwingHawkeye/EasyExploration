package com.blackwing.easyExploration.items.Bases

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.ItemPickaxe

class PickaxeBase(val name: String, material: ToolMaterial) extends ItemPickaxe(material) with IItem {
	val tab: CreativeTabs = CreativeTabs.TOOLS
}
