package com.bwhe.easyExploration;

import com.bwhe.easyExploration.config.EasyExplorationConfigEventHandlerCommon;
import com.bwhe.easyExploration.saveInventory.SaveInventory;
import com.bwhe.easyExploration.saveInventory.SaveInventoryEventHandlerCommon;
import com.bwhe.easyExploration.showDamage.ShowDamageEventHandlerClient;
import com.bwhe.easyExploration.showDeathLocation.ShowDeathLocationEventHandlerCommon;
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

    // public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @SidedProxy(clientSide = "com.bwhe.easyExploration.saveInventory.SaveInventoryEventHandlerClient", serverSide = "com.bwhe.easyExploration.saveInventory.SaveInventoryEventHandlerCommon")
    private static SaveInventoryEventHandlerCommon saveInventoryEventHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // register event handlers
        super.onPreInit(event);
        (new EasyExplorationConfigEventHandlerCommon()).onPreInit(event);
        saveInventoryEventHandler.onPreInit(event);
        (new ShowDeathLocationEventHandlerCommon()).onPreInit(event);
        (new ShowDamageEventHandlerClient()).onPreInit(event);
        // register Messages
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SaveInventory.instance().setLogger(logger);
    }
}
