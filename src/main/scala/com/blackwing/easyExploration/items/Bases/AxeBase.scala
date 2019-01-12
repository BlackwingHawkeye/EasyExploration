package com.blackwing.easyExploration.items.Bases

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.ItemAxe

class AxeBase(val name: String, material: ToolMaterial) extends ItemAxe(material, 6.0F, -3.2F) with IItem {
	val tab: CreativeTabs = CreativeTabs.TOOLS
}
