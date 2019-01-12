package com.blackwing.easyExploration.items.Bases

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class FoodEffectBase(name: String, amount: Int, saturation: Float, isAnimalFood: Boolean, effect: PotionEffect)
	extends FoodBase(name, amount, saturation, isAnimalFood) {

	setAlwaysEdible()

	override protected def onFoodEaten(stack: ItemStack, worldIn: World, player: EntityPlayer): Unit =
		if (!worldIn.isRemote) player.addPotionEffect(new PotionEffect(effect.getPotion, effect.getDuration, effect.getAmplifier, effect.getIsAmbient, effect.doesShowParticles))

	@SideOnly(Side.CLIENT)
	override def hasEffect(stack: ItemStack) = true
}
