package com.blackwing.easyExploration.handlers;

import com.blackwing.easyExploration.Configuration;
import com.blackwing.easyExploration.util.EventHandlerBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShowDamageHandler extends EventHandlerBase {

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        // there is nothing to do on server side
        if (!event.getEntity().world.isRemote) return;
        // feature is disabled
        if (!Configuration.showDamage.enabled) return;
        // only deal with your own stuff
        if (isNotClientPlayer(event.getEntity())) return;
        //render event.getAmount() above event.getEntity().getPosition()
    }
}
