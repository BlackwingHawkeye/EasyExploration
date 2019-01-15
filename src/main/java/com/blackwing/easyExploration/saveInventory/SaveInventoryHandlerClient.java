package com.blackwing.easyExploration.saveInventory;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SaveInventoryHandlerClient extends SaveInventoryHandlerCommon {

    @Override
    @SubscribeEvent
    public void onLoad(PlayerEvent.LoadFromFile event) {
        if (isClientPlayer(event.getEntity())) super.onLoad(event);    // only deal with your own load
    }

    @Override
    @SubscribeEvent
    public void onLoggedIn(PlayerLoggedInEvent event) {
    }       // do nothing since we can't login on a client

    @Override
    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (isClientPlayer(event.getEntity())) super.onDeath(event);    // only deal with your own death
    }

    @Override
    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        if (isClientPlayer(event.getEntity())) super.onClone(event);    // only deal with your own spawn
    }

    @Override
    @SubscribeEvent
    public void onSave(PlayerEvent.SaveToFile event) {
        if (isClientPlayer(event.getEntity())) super.onSave(event);    // only deal with your own save
    }
}
