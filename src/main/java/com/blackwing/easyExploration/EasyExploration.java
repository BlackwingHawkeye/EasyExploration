package com.blackwing.easyExploration;

import com.blackwing.easyExploration.config.EasyExplorationConfigEventHandlerCommon;
import com.blackwing.easyExploration.saveInventory.SaveInventory;
import com.blackwing.easyExploration.saveInventory.SaveInventoryEventHandlerCommon;
import com.blackwing.easyExploration.showDamage.ShowDamageEventHandlerClient;
import com.blackwing.easyExploration.showDeathLocation.ShowDeathLocationEventHandlerCommon;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        useMetadata = true,
        modid = EasyExploration.MODID,
        name = EasyExploration.NAME,
        version = EasyExploration.VERSION,
        acceptedMinecraftVersions = "1.12",
        canBeDeactivated = true,
        certificateFingerprint = EasyExploration.FINGERPRINT
)
public class EasyExploration extends EasyExplorationEventHandlerBasic {
    public static final String MODID = "EasyExploration";
    public static final String NAME = "Easy Exploration";
    public static final String VERSION = "@VERSION@";
    public static final String FINGERPRINT = "@FINGERPRINT@";

    @Mod.Instance(MODID)
    public static EasyExploration instance;

    /*
     * Here we check if our mod has been tampered with
     */
    @Mod.EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent event) {
        if (!FINGERPRINT.equals("@FINGERPRINT@")) {
            logger.warn("THE EASYEXPLORATION MOD THAT YOU ARE USING HAS BEEN CHANGED/TAMPERED WITH!");
        }
    }

    // public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.instance.newSimpleChannel(MODID);

    @SidedProxy(clientSide = "com.blackwing.easyExploration.saveInventory.SaveInventoryEventHandlerClient", serverSide = "com.blackwing.easyExploration.saveInventory.SaveInventoryEventHandlerCommon")
    public static SaveInventoryEventHandlerCommon saveInventoryEventHandler;

    /*
     * This is where we load our network configuration, mod configuration, .. and where we initialize our items and blocks
     */
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

    /*
     * This is where we register our GUIs, tile entities, crafting recipes, ..
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SaveInventory.instance().setLogger(logger);
    }

    /*
     * This is where you can run things after all other mods have e.g. registered their blocks and items
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
