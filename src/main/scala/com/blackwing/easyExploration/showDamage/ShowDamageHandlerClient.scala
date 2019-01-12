package com.blackwing.easyExploration.showDamage

import com.blackwing.easyExploration.config.Configuration
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ShowDamageHandlerClient extends ShowDamageHandlerCommon.type {
	@SubscribeEvent def onDamage(event: LivingHurtEvent): Unit = { // feature is disabled
		if (!Configuration.showDamage.enabled) return
		//render event.getAmount() above event.getEntity().getPosition()
	}
}