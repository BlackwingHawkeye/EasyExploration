package com.blackwing.easyExploration.blocks

import com.blackwing.easyExploration.Items
import com.blackwing.easyExploration.blocks.Bases.BlockBase
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item

import scala.util.Random

class RubyOre(name: String, material: Material) extends BlockBase(name, material) {

	setSoundType(SoundType.METAL)
	setHardness(5.0F)
	setResistance(15.0F)
	setHarvestLevel("pickaxe", 2)

	def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = Items.RUBY

	def quantityDropped(rand: Random): Int = {
		val max = 4
		val min = 1
		rand.nextInt(max) + min
	}
}
