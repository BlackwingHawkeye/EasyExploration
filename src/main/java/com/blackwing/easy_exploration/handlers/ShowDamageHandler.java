package com.blackwing.easy_exploration.handlers;

import com.blackwing.easy_exploration.EasyExploration;
import com.blackwing.easy_exploration.config.Configuration;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Logger;

//@Only(Side.CLIENT)
public class ShowDamageHandler extends BaseForgeEventHandler {

    private static final Logger LOGGER = EasyExploration.getLogger(ShowDamageHandler.class);

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        // there is nothing to do on server side
        if (!event.getEntity().world.isRemote) return;
        // feature is disabled
        //if (!Configuration.showDamage.enabled) return;
        // only deal with your own stuff
        if (isNotClientPlayer(event.getEntity())) return;
        //render event.getAmount() above event.getEntity().getPosition()
    }
}