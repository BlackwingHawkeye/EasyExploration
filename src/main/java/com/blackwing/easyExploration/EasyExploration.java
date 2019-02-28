package com.blackwing.easyExploration;

import com.blackwing.easyExploration.handlers.ConfigurationHandler;
import com.blackwing.easyExploration.handlers.SaveInventoryHandler;
import com.blackwing.easyExploration.handlers.ShowDamageHandler;
import com.blackwing.easyExploration.handlers.ShowDeathLocationHandler;
import com.blackwing.easyExploration.util.EventHandlerBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        useMetadata = true,
        modid = EasyExploration.MODID,
        name = EasyExploration.NAME,
        version = EasyExploration.VERSION,
        acceptedMinecraftVersions = "1.12",
        canBeDeactivated = true,
        certificateFingerprint = EasyExploration.FINGERPRINT
)
public class EasyExploration extends EventHandlerBase {
    public static final String MODID = "easyexploration";
    public static final String NAME = "Easy Exploration";
    public static final String VERSION = "@VERSION@";
    public static final String FINGERPRINT = "@FINGERPRINT@";

    @Mod.Instance(MODID)
    public static EasyExploration instance;

    protected final Logger log = LogManager.getLogger(EasyExploration.MODID + "." + getClass().getSimpleName());

    /*
     * Here we check if our mod has been tampered with
     */
    @Mod.EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent event) {
        if (!FINGERPRINT.equals("@FINGERPRINT@")) {
            log.warn("THE EASYEXPLORATION MOD THAT YOU ARE USING HAS BEEN CHANGED/TAMPERED WITH!");
        }
    }

    // public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.instance.newSimpleChannel(MODID);

    public static final ConfigurationHandler configHandler = new ConfigurationHandler();
    public static final SaveInventoryHandler saveInventoryHandler = new SaveInventoryHandler();
    public static final ShowDeathLocationHandler showDeathLocationHandler = new ShowDeathLocationHandler();
    public static final ShowDamageHandler showDamageHandler = new ShowDamageHandler();

    protected static final EventHandlerBase[] handlers = {
            configHandler,
            saveInventoryHandler,
            showDeathLocationHandler,
            showDamageHandler
    };

    /*
     * This is where we load our network configuration, mod configuration, .. and where we initialize our items and blocks
     */
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        // register event handlers
        super.onPreInit(event);
        for (EventHandlerBase handler : handlers) handler.onPreInit(event);
        // register Messages
    }

    /*
     * This is where we register our GUIs, tile entities, crafting recipes, ..
     */
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
        for (EventHandlerBase handler : handlers) handler.onInit(event);
    }

    /*
     * This is where you can run things after all other mods have e.g. registered their blocks and items
     */
    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        super.onPostInit(event);
        for (EventHandlerBase handler : handlers) handler.onPostInit(event);
    }
}
//TODO: FIX [21:22:54] [main/ERROR] [FML]: FML appears to be missing any signature data. This is not a good thing
// TODO: create an actual fingerprint
