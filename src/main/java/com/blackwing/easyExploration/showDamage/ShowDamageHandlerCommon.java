package com.blackwing.easyExploration.showDamage;

import com.blackwing.easyExploration.util.EventHandlerBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShowDamageHandlerCommon extends EventHandlerBase {

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
    }
}
