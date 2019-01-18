package com.blackwing.easyExploration;

import com.blackwing.easyExploration.proxy.Common;
import com.blackwing.easyExploration.util.EventHandlerBase;
import com.blackwing.easyExploration.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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

    public static final Logger logger = LogManager.getLogger(EasyExploration.MODID);

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

    @SidedProxy(clientSide = "com.blackwing.easyExploration.proxy.Client", serverSide = "com.blackwing.easyExploration.proxy.Common")
    public static Common proxy;

    /*
     * This is where we load our network configuration, mod configuration, .. and where we initialize our items and blocks
     */
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        proxy.onPreInit(event);
    }

    /*
     * This is where we register our GUIs, tile entities, crafting recipes, ..
     */
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
        proxy.onInit(event);
    }

    /*
     * This is where you can run things after all other mods have e.g. registered their blocks and items
     */
    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        super.onPostInit(event);
        proxy.onPostInit(event);
    }

    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Item item : ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }

        for (Block block : BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }
    }
}
//TODO: FIX [21:22:54] [main/ERROR] [FML]: FML appears to be missing any signature data. This is not a good thing
// TODO: create an actual fingerprint
