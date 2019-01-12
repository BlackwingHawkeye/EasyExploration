package com.blackwing.easyExploration.items.Bases

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.ItemHoe

class HoeBase(val name: String, material: ToolMaterial) extends ItemHoe(material) with IItem {
	val tab: CreativeTabs = CreativeTabs.TOOLS
}
