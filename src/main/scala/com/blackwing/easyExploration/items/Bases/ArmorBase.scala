package com.blackwing.easyExploration.items.Bases

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemArmor

class ArmorBase(val name: String, materialIn: ItemArmor.ArmorMaterial, renderIndexIn: Int, equipmentSlotIn: EntityEquipmentSlot)
	extends ItemArmor(materialIn, renderIndexIn, equipmentSlotIn) with IItem {

	val tab: CreativeTabs = CreativeTabs.COMBAT
}