package com.blackwing.easyExploration.showDamage;

import com.blackwing.easyExploration.config.Configuration;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShowDamageHandlerClient extends ShowDamageHandlerCommon {

    @Override
    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        // feature is disabled
        if (!Configuration.showDamage.enabled) return;
        if (!isClientPlayer(event.getEntity())) return;
        //render event.getAmount() above event.getEntity().getPosition()
    }
}
