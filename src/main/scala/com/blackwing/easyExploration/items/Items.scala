package com.blackwing.easyExploration

import com.blackwing.easyExploration.EasyExploration.modId
import com.blackwing.easyExploration.items.Bases._
import com.blackwing.easyExploration.items._
import net.minecraft.init.{MobEffects, SoundEvents}
import net.minecraft.inventory.EntityEquipmentSlot._
import net.minecraft.item.{Item, ItemArmor}
import net.minecraft.potion.PotionEffect
import net.minecraftforge.common.util.EnumHelper
import com.blackwing.easyExploration.items.Bases.ItemBase
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

package object Items {

	//Materials
	val MATERIAL_RUBY: Item.ToolMaterial =
		EnumHelper.addToolMaterial("material_ruby", 3, 250, 8.0F, 3.0F, 10)
	val ARMOR_MATERIAL_RUBY: ItemArmor.ArmorMaterial =
		EnumHelper.addArmorMaterial("armor_material_ruby", modId + ":ruby", 14, Array[Int](2, 5, 7, 3), 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F)

	//Items
	val RUBY = new ItemBase("ruby", CreativeTabs.MATERIALS)
	val OBSIDIAN_INGOT = new ItemBase("obsidian_ingot", CreativeTabs.MATERIALS)
	val SLEEPING_BAG = new SleepingBagItem

	//Tools
	val RUBY_SWORD = new SwordBase("ruby_sword", MATERIAL_RUBY)
	val RUBY_SHOVEL = new SpadeBase("ruby_shovel", MATERIAL_RUBY)
	val RUBY_PICKAXE = new PickaxeBase("ruby_pickaxe", MATERIAL_RUBY)
	val RUBY_AXE = new AxeBase("ruby_axe", MATERIAL_RUBY)
	val RUBY_HOE = new HoeBase("ruby_hoe", MATERIAL_RUBY)

	//Armor
	val RUBY_HELMET = new ArmorBase("ruby_helmet", ARMOR_MATERIAL_RUBY, 1, HEAD)
	val RUBY_CHESTPLATE = new ArmorBase("ruby_chestplate", ARMOR_MATERIAL_RUBY, 1, CHEST)
	val RUBY_LEGGINGS = new ArmorBase("ruby_leggings", ARMOR_MATERIAL_RUBY, 2, LEGS)
	val RUBY_BOOTS = new ArmorBase("ruby_boots", ARMOR_MATERIAL_RUBY, 1, FEET)

	//Food
	val PEAR = new FoodBase("pear", 4, 2.4f, false)
	val EVIL_APPLE = new FoodEffectBase("evil_apple", 4, 2.4f, false, new PotionEffect(MobEffects.POISON, 60 * 20, 1, false, true))
}