package com.bwhe.easyExploration.showDeathLocation;

import com.bwhe.easyExploration.config.Config;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class EventHandler {

    private Logger logger;

    public EventHandler(Logger logger) {
        this.logger = logger;
    }

    private String getMessage(EntityPlayerMP player) {
        return player.getName() + " died in dimension " + player.dimension
                + " at X/Y/Z " + player.posX + "/" + player.posY + "/" + player.posZ;
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if(Config.showDeathLocationEnabled) {
            if (event.getEntity() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
                player.mcServer.getPlayerList().sendMessage(new TextComponentString(getMessage(player)));
                logger.info(getMessage(player));
            }
        }
    }
}

