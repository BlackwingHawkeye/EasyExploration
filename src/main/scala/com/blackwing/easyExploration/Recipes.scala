package com.blackwing.easyExploration

import com.blackwing.easyExploration.Blocks._
import com.blackwing.easyExploration.Items._
import net.minecraft.init.Blocks._
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

class Recipes {

	def init(): Unit = {
		GameRegistry.addSmelting(RUBY, new ItemStack(RUBY_BLOCK, 1), 1.5f)
		GameRegistry.addSmelting(RUBY_BLOCK, new ItemStack(DIAMOND_BLOCK, 2), 3.0f)
	}
}
