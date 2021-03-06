package com.blackwing.easyExploration.util;

import com.blackwing.easyExploration.EasyExploration;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EventHandlerBase {

    protected final Logger log = LogManager.getLogger(EasyExploration.MODID + "." + getClass().getSimpleName());

    /**
     * Returns true if the given entity is the player using this client
     *
     * @param entity Entity of the event
     * @return true if the given entity is the player using this client
     */
    protected boolean isNotClientPlayer(Entity entity) {
        try {
            if (FMLClientHandler.instance().getClientPlayerEntity() == null) return false;
            return entity.getUniqueID() != FMLClientHandler.instance().getClientPlayerEntity().getUniqueID();
        } catch (Exception e) {
            log.catching(e);
        }
        return true;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
    }
}
