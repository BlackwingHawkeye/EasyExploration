package com.bwhe.easyExploration;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        useMetadata = true,
        modid = EasyExploration.MODID,
        name = EasyExploration.NAME,
        version = EasyExploration.VERSION,
        acceptedMinecraftVersions = "1.12",
        canBeDeactivated = true
)
public class EasyExploration {
    public static final String MODID = "easyexploration";
    public static final String NAME = "Easy Exploration";
    public static final String VERSION = "1.0.5";

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        MinecraftForge.EVENT_BUS.register(new com.bwhe.easyExploration.saveInventory.EventHandler(logger));
        MinecraftForge.EVENT_BUS.register(new com.bwhe.easyExploration.showDeathLocation.EventHandler(logger));
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
