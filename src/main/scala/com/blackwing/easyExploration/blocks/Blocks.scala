package com.blackwing.easyExploration
import com.blackwing.easyExploration.blocks.RubyBlock
import com.blackwing.easyExploration.blocks.RubyOre
import net.minecraft.block.Block
import net.minecraft.block.material.Material


package object Blocks {

	//Blocks
	val RUBY_BLOCK = new RubyBlock("ruby_block", Material.IRON)
	val RUBY_ORE = new RubyOre("ruby_ore", Material.ROCK)
}
