package com.blackwing.easyExploration.items.Bases

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.ItemSpade

class SpadeBase(val name: String, material: ToolMaterial) extends ItemSpade(material) with IItem {
	val tab: CreativeTabs = CreativeTabs.TOOLS
}
