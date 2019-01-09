package com.blackwing.easyExploration.showDamage;

import com.blackwing.easyExploration.EasyExplorationEventHandlerBasic;
import com.blackwing.easyExploration.config.EasyExplorationConfig;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShowDamageEventHandlerClient extends EasyExplorationEventHandlerBasic {
    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        // feature is disabled
        if (!EasyExplorationConfig.showDamage.enabled) return;
        //render event.getAmount() above event.getEntity().getPosition()
    }
}
