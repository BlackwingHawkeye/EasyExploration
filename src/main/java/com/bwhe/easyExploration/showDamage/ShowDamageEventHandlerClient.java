package com.bwhe.easyExploration.showDamage;

import com.bwhe.easyExploration.EasyExploration;
import com.bwhe.easyExploration.EasyExplorationEventHandlerBasic;
import com.bwhe.easyExploration.config.EasyExplorationConfig;
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
