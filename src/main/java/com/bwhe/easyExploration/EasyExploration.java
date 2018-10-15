package com.bwhe.easyExploration;

import com.bwhe.easyExploration.config.EasyExplorationConfigEventHandler;
import com.bwhe.easyExploration.saveInventory.SaveInventoryEventHandlerCommon;
import com.bwhe.easyExploration.showDeathLocation.ShowDeathLocationEventHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        useMetadata = true,
        modid = EasyExploration.MODID,
        name = EasyExploration.NAME,
        version = EasyExploration.VERSION,
        acceptedMinecraftVersions = "1.12",
        canBeDeactivated = true
)
public class EasyExploration extends EasyExplorationEventHandlerBasic {
    public static final String MODID = "easyexploration";
    public static final String NAME = "Easy Exploration";
    public static final String VERSION = "1.0.6";

    @SidedProxy(clientSide = "com.bwhe.easyExploration.saveInventory.SaveInventoryEventHandlerClient", serverSide = "com.bwhe.easyExploration.saveInventory.SaveInventoryEventHandlerCommon")
    public static SaveInventoryEventHandlerCommon saveInventoryEventHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        (new EasyExplorationConfigEventHandler()).onPreInit(event);
        saveInventoryEventHandler.onPreInit(event);
        (new ShowDeathLocationEventHandler()).onPreInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // register event handlers
    }
}
