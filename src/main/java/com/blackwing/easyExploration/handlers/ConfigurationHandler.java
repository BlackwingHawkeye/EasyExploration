package com.blackwing.easyExploration.handlers;

import com.blackwing.easyExploration.util.EventHandlerBase;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.blackwing.easyExploration.EasyExploration.MODID;

public class ConfigurationHandler extends EventHandlerBase {

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
        }
    }
}
