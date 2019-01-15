package com.blackwing.easyExploration.proxy;

import com.blackwing.easyExploration.config.ConfigurationHandlerCommon;
import com.blackwing.easyExploration.saveInventory.SaveInventory;
import com.blackwing.easyExploration.saveInventory.SaveInventoryHandlerCommon;
import com.blackwing.easyExploration.showDamage.ShowDamageHandlerCommon;
import com.blackwing.easyExploration.showDeathLocation.ShowDeathLocationHandlerCommon;
import com.blackwing.easyExploration.utilities.EventHandlerBase;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Common extends EventHandlerBase {

    public static final ConfigurationHandlerCommon configHandler = new ConfigurationHandlerCommon();
    public static final SaveInventoryHandlerCommon saveInventoryHandler = new SaveInventoryHandlerCommon();
    public static final ShowDeathLocationHandlerCommon showDeathLocationHandler = new ShowDeathLocationHandlerCommon();
    public static final ShowDamageHandlerCommon showDamageHandler = new ShowDamageHandlerCommon();

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
        SaveInventory.instance().setLogger(logger);
    }

    /*
     * This is where you can run things after all other mods have e.g. registered their blocks and items
     */
    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        super.onPostInit(event);
        for (EventHandlerBase handler : handlers) handler.onPostInit(event);
    }

    public void registerItemRenderer(Item item, int meta, String id) {}
}
