package com.blackwing.easyExploration.showDeathLocation

import com.blackwing.easyExploration.config.Configuration
import com.blackwing.easyExploration.config.Configuration.ShowDeathOption._
import com.blackwing.easyExploration.utilities.EventHandlerBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.DimensionType
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ShowDeathLocationHandlerCommon extends EventHandlerBase {

	private def getMessage(player: EntityPlayerMP) = new TextComponentString(
		s"""${player.getName} died
		   | in ${DimensionType.getById(player.dimension).getName} (id:${player.dimension})
		   | at X=${Math.ceil(player.posX).toInt}/Y=${Math.ceil(player.posY).toInt}/Z=${Math.ceil(player.posZ).toInt}"
		   | """.stripMargin)

	@SubscribeEvent
	def onDeath(event: LivingDeathEvent): Unit = {
		// someone/something else died
		if (!event.getEntity.isInstanceOf[EntityPlayerMP]) return
		val player = event.getEntity.asInstanceOf[EntityPlayerMP]
		Configuration.showDeathLocation.sendTo match {
			case NOONE =>
			case PLAYER =>
				player.sendMessage(getMessage(player))
			case TEAM =>
				player.sendMessage(getMessage(player))
				player.mcServer.getPlayerList.sendMessageToAllTeamMembers(player, getMessage(player))
			case EVERYONE =>
				player.mcServer.getPlayerList.sendMessage(getMessage(player))
		}
	}
}