package com.bwhe.easyExploration.showDeathLocation;

import com.bwhe.easyExploration.EasyExplorationConfig;
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
        return player.getName() + " died in dimension " + player.dimension + " at X/Y/Z " + (int) Math.ceil(player.posX) + "/" + (int) Math.ceil(player.posY) + "/" + (int) Math.ceil(player.posZ);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (!EasyExplorationConfig.showDeathLocation.enabled) return;
        if (!(event.getEntity() instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
        String message = getMessage(player);
        player.sendMessage(new TextComponentString(message));
        logger.info("Notified Player: " + message);
    }

    //TODO: find dimension name
    //TODO: create config notify (all on server) / notify team / notify (dead) player
}
