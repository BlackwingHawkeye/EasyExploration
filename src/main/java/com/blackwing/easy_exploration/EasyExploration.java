package com.blackwing.easy_exploration;

import com.blackwing.easy_exploration.handlers.ConfigurationHandler;
import com.blackwing.easy_exploration.handlers.ShowDamageHandler;
import com.blackwing.easy_exploration.handlers.ShowDeathLocationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EasyExploration.MODID)
public class EasyExploration {
    public static final String MODID = "easy_exploration";
    public static final String NAME = "Easy Exploration";
    public static final String VERSION = "@VERSION@";
    public static final String FINGERPRINT = "@FINGERPRINT@";

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(EasyExploration.MODID + "." + clazz.getSimpleName());
    }

    private static final Logger LOGGER = getLogger(EasyExploration.class);

    public EasyExploration() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigurationHandler.config);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register the doServerStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doServerStuff);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
        MinecraftForge.EVENT_BUS.register(new ShowDeathLocationHandler());
        MinecraftForge.EVENT_BUS.register(new ShowDamageHandler());
    }

    /* first Forge mod loading main phase: PreInit
     * some preinit code, such as
     *      Creating and reading the config files
     *      Registering Capabilities
     */
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        ConfigurationHandler.loadConfig(ConfigurationHandler.config, FMLPaths.CONFIGDIR.get().resolve("easy_exploration.toml").toString());
    }

    /* second Forge mod loading main phase: physical side-specific initialization
     * do something that can only be done on the client, such as
     *      key bindings
     */
    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    /* second Forge mod loading main phase: physical side-specific initialization
     * do something that can only be done on the server, such as
     *
     */
    private void doServerStuff(final FMLDedicatedServerSetupEvent event) {
        LOGGER.info("Server name {}", event.getServerSupplier().get().getName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("easy_exploration", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("Registering no blocks ... yet");
            //event.getRegistry().registerAll(block1, block2, ...);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            // register a new item here
            LOGGER.info("Registering no items neither");
            //event.getRegistry().registerAll(block1, block2, ...);
        }
    }
}
