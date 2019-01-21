package com.blackwing.easyExploration.tileEntity.base

import com.blackwing.easyExploration.EasyExploration
import com.blackwing.easyExploration.util.IHasRenderer
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity

trait ITileEntityRender[T >: TileEntity] extends TileEntitySpecialRenderer[T] with IHasRenderer {

	def init(): Unit = {
		EasyExploration.TILE_ENTITY_RENDERER.add(this[T])
	}

	def registerRenderer(): Unit = {
		EasyExploration.proxy.registerTileEntityAndRenderer(classOf[T], getId, this)
	}
}
