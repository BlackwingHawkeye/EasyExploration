package com.blackwing.easyExploration.saveInventory;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SaveInventoryEventHandlerClient extends SaveInventoryEventHandlerCommon {

    /**
     * Returns true if the given entity is the player using this client
     *
     * @param entity Entity of the event
     * @return true if the given entity is the player using this client
     */
    private boolean isClientPlayer(Entity entity) {
        try {
            if (FMLClientHandler.instance().getClientPlayerEntity() == null) return true;
            return entity.getUniqueID() == FMLClientHandler.instance().getClientPlayerEntity().getUniqueID();
        } catch (Exception e) {
            logger.catching(e);
        }
        return false;
    }

    @Override
    public void onLoad(PlayerEvent.LoadFromFile event) {
        if (isClientPlayer(event.getEntity())) super.onLoad(event);    // only deal with your own load
    }

    @Override
    public void onLoggedIn(PlayerLoggedInEvent event) {
    }       // do nothing since we can't login on a client

    @Override
    public void onDeath(LivingDeathEvent event) {
        if (isClientPlayer(event.getEntity())) super.onDeath(event);    // only deal with your own death
    }

    @Override
    public void onClone(PlayerEvent.Clone event) {
        if (isClientPlayer(event.getEntity())) super.onClone(event);    // only deal with your own spawn
    }

    @Override
    public void onLoggedOut(PlayerLoggedOutEvent event) {
        if (isClientPlayer(event.player)) super.onLoggedOut(event);    // only deal with your own logout
    }

    @Override
    public void onSave(PlayerEvent.SaveToFile event) {
        if (isClientPlayer(event.getEntity())) super.onSave(event);    // only deal with your own save
    }
}
