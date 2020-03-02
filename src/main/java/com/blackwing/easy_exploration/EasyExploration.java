package com.blackwing.easy_exploration;

import com.blackwing.easy_exploration.config.Configuration;
import com.blackwing.easy_exploration.init.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
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
        LOGGER.debug("Friendly greetings from Easy Exploration mod. Because manners matter!");

        // Register Configs (Does not need to be before Deferred Registers)
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, Configuration.SERVER_SPEC);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register Deferred Registers (Does not need to be after Configs)
        ModBlocks.BLOCKS.register(modEventBus);
        LOGGER.debug("Registered Blocks");
        ModItems.ITEMS.register(modEventBus);
        LOGGER.debug("Registered Items");
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
        LOGGER.debug("Registered Containers");
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        LOGGER.debug("Registered Tiles");

        // Register the setup method for modloading
        modEventBus.addListener(this::setup);
        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::doClientStuff);
        // Register the doServerStuff method for modloading
        modEventBus.addListener(this::doServerStuff);
        // Register the enqueueIMC method for modloading
        modEventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modEventBus.addListener(this::processIMC);

    }

    /* first Forge mod loading main phase: PreInit
     * some preinit code, such as
     *      Creating and reading the config files
     *      Registering Capabilities
     */
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        //Configuration.loadConfig(Configuration.config, FMLPaths.CONFIGDIR.get().resolve("easy_exploration.toml").toString());
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

    /**
     * Subscribe to events from the MOD EventBus that should be handled on both PHYSICAL sides in this class
     */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public final static class ModEventSubscriber {

        /**
         * This method will be called by Forge when it is time for the mod to register its Items.
         * This method will always be called after the Block registry method.
         */
        @SubscribeEvent
        public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();
            // Automatically register BlockItems for all our Blocks
            ModBlocks.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get)
                    // You can do extra filtering here if you don't want some blocks to have an BlockItem automatically registered for them
                    // .filter(block -> needsItemBlock(block))
                    // Register the BlockItem for the block
                    .forEach(block -> {
                        // Make the properties, and make it so that the item will be on our ItemGroup (CreativeTab)
                        final Item.Properties properties = new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP);
                        // Create the new BlockItem with the block and it's properties
                        final BlockItem blockItem = new BlockItem(block, properties);
                        // Set the new BlockItem's registry name to the block's registry name
                        blockItem.setRegistryName(block.getRegistryName());
                        // Register the BlockItem
                        registry.register(blockItem);
                    });
            LOGGER.debug("Registered BlockItems");
        }

        /**
         * This method will be called by Forge when a config changes.
         */
        @SubscribeEvent
        public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
            final ModConfig config = event.getConfig();
            // Re-make the configs when they change
            if (config.getSpec() == Configuration.CLIENT_SPEC) {
                Configuration.makeClient(config);
                LOGGER.debug("Made the client config");
            } else if (config.getSpec() == Configuration.SERVER_SPEC) {
                Configuration.makeServer(config);
                LOGGER.debug("Made the server config");
            }
        }
    }

    /**
     * Subscribe to events from the FORGE EventBus that should be handled on both PHYSICAL sides in this class
     */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public final static class ForgeEventSubscriber {
    }
}
