package com.blackwing.easyExploration.utilities

/**
  * Using this trait, we can easily determine by isInstanceOf(IHasModel) if an item is one ours.
  */
trait IHasModel {
	def registerModels(): Unit
}
