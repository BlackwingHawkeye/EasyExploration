package com.blackwing.easyExploration.showDeathLocation;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShowDeathLocationHandlerClient extends ShowDeathLocationHandlerCommon {

    @Override
    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (isClientPlayer(event.getEntity())) super.onDeath(event);    // only deal with your own death
    }
}
