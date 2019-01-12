package com.blackwing.easyExploration.blocks

import com.blackwing.easyExploration.blocks.Bases.BlockBase
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

class RubyBlock(name: String, material: Material) extends BlockBase(name, material) {

	setSoundType(SoundType.METAL)
	setHardness(5.0F)
	setResistance(15.0F)
	setHarvestLevel("pickaxe", 2)
	setLightLevel(1.0F)
	//setLightOpacity(1)
	//setBlockUnbreakable()
}
