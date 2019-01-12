package com.blackwing.easyExploration.items.Bases

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemFood

class FoodBase(val name: String, amount: Int, saturation: Float, isAnimalFood: Boolean)
	extends ItemFood(amount, saturation, isAnimalFood) with IItem {

	val tab: CreativeTabs = CreativeTabs.FOOD
}
