package com.bwhe.easyExploration.config;

import com.bwhe.easyExploration.EasyExploration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(EasyExploration.MODID))
            Config.syncFromGUI();
    }
}
