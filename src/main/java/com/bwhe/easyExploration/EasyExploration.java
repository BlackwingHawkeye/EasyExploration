package com.bwhe.easyExploration;

import com.bwhe.easyExploration.showDeathLocation.EventHandler;
import com.bwhe.easyExploration.config.Config;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

/*

useMetadata	boolean	false	If set to true, properties in @Mod will be overridden by mcmod.info.
clientSideOnly
serverSideOnly	boolean
boolean	false
false	If either is set to true, the jar will be skipped on the other side, and the mod will not load. If both are true, the game crashes.
acceptedMinecraftVersions	String	”“	The version range of Minecraft the mod will run on.* An empty string will match all versions.
acceptableRemoteVersions	String	”“	Specifies a remote version range that this mod will accept as valid.* "" Matches the current version, and "*" matches all versions.
acceptableSaveVersions	String	”“	A version range specifying compatible save version information.* If you follow an unusual version convention, use SaveInspectionHandler instead.
certificateFingerprint	String	”“	See the tutorial on jar signing.
modLanguage	String	“java”	The programming language the mod is written in. Can be either "java" or "scala".
modLanguageAdapter	String	”“	Path to a language adapter for the mod. The class must have a default constructor and must implement ILanguageAdapter. If it doesn’t, Forge will crash. If set, overrides modLanguage.
canBeDeactivated	boolean	false	This is not implemented, but if the mod could be deactivated (e.g. a minimap mod), this would be set to true and the mod would receive FMLDeactivationEvent to perform cleanup tasks.
guiFactory	String	”“	Path to the mod’s GUI factory, if one exists. GUI factories are used to make custom config screens, and must implement IModGuiFactory. For an example, look at  FMLConfigGuiFactory.
updateJSON	String	”“	URL to an update JSON file. See Forge Update Checker
 */
@Mod(
        modid = EasyExploration.MODID,
        name = EasyExploration.NAME,
        version = EasyExploration.VERSION,
        guiFactory = EasyExploration.GUI_FACTORY
)
public class EasyExploration {
    public static final String MODID = "easyexploration";
    public static final String NAME = "Easy Exploration";
    public static final String VERSION = "1.0.5";
    public static final String CONFIG_FILE = "easyexploration.cfg";
    public static final String GUI_FACTORY = "com.tp.easyExploration.GUIFactory";

    public static EasyExploration instance;

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        Config.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        MinecraftForge.EVENT_BUS.register(new com.bwhe.easyExploration.keepInventory.EventHandler(logger));
        MinecraftForge.EVENT_BUS.register(new EventHandler(logger));
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
