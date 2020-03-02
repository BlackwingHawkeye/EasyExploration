package com.blackwing.easy_exploration.client;

import com.blackwing.easy_exploration.EasyExploration;
import com.blackwing.easy_exploration.client.gui.DeathChestScreen;
import com.blackwing.easy_exploration.init.ModContainerTypes;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.Logger;

/**
 * Subscribe to events from the MOD EventBus that should be handled on the PHYSICAL CLIENT side in this class
 */
@EventBusSubscriber(modid = EasyExploration.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventSubscriber {

    private static final Logger LOGGER = EasyExploration.getLogger(ClientModEventSubscriber.class);

    /**
     * We need to register our renderers on the client because rendering code does not exist on the server
     * and trying to use it on a dedicated server will crash the game.
     * <p>
     * This method will be called by Forge when it is time for the mod to do its client-side setup
     * This method will always be called after the Registry events.
     * This means that all Blocks, Items, TileEntityTypes, etc. will all have been registered already
     */
    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {

        // Register TileEntity Renderers
        LOGGER.debug("Registered TileEntity Renderers");

        // Register Entity Renderers
        LOGGER.debug("Registered Entity Renderers");

        // Register ContainerType Screens
        // ScreenManager.registerFactory is not safe to call during parallel mod loading so we queue it to run later
        DeferredWorkQueue.runLater(() -> {
            ScreenManager.registerFactory(ModContainerTypes.DEATH_CHEST.get(), DeathChestScreen::new);
            LOGGER.debug("Registered ContainerType Screens");
        });
    }
}
