package com.bwhe.easyExploration.config;

import com.bwhe.easyExploration.EasyExploration;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public class Config {
    private static Configuration config = null;

    private static final String CATEGORY_NAME_KEEP_INVENTORY = "keepInventory";
    private static final String CATEGORY_NAME_SHOW_DEATH_LOCATION = "showDeathLocation";
    private static final String CATEGORY_NAME_SLEEPING_BAGS = "sleepingBags";

    private static final String PROPERTY_KEY_ENABLED = "enabled";
    private static final String PROPERTY_KEY_EQUIPMENT = "equipment";
    private static final String PROPERTY_KEY_LOOT = "loot";

    public static final String PROPERTY_VALUE_DROP = "drop";
    public static final String PROPERTY_VALUE_KEEP = "keep";
    public static final String PROPERTY_VALUE_SAVE = "save";

    private static final String PROPERTY_DEFAULT_VALUE_KEEP_INVENTORY_ENABLED = Boolean.toString(true);
    private static final String PROPERTY_DEFAULT_VALUE_KEEP_INVENTORY_EQUIPMENT = PROPERTY_VALUE_KEEP;
    private static final String PROPERTY_DEFAULT_VALUE_KEEP_INVENTORY_LOOT = PROPERTY_VALUE_SAVE;
    private static final String PROPERTY_DEFAULT_VALUE_SHOW_DEATH_LOCATION_ENABLED = Boolean.toString(true);
    private static final String PROPERTY_DEFAULT_VALUE_SLEEPING_BAGS_ENABLED = Boolean.toString(true);

    public static Boolean keepInventoryEnabled;
    public static String keepInventoryEquipment;
    public static String keepInventoryLoot;

    public static Boolean showDeathLocationEnabled;
    public static Boolean sleepingBagsEnabled;

    private static Property keepInventoryEnabledProperty;
    private static Property keepInventoryEquipmentProperty;
    private static Property keepInventoryLootProperty;

    private static Property showDeathLocationEnabledProperty;
    private static Property sleepingBagsEnabledProperty;

    public static void preInit() {
        File file = new File(Loader.instance().getConfigDir(), EasyExploration.CONFIG_FILE);
        config = new Configuration(file);
        syncFromFile();
    }

    public Configuration getConfig() {
        return config;
    }

    public static void clientPreInit() {
        MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
    }

    public static void syncFromFile() {
        config.load();
        getProperties();
        readFromProperties();
        saveConfig();
    }

    public static void syncFromGUI() {
        getProperties();
        readFromProperties();
        saveConfig();
    }

    public static void syncFromFields() {
        getProperties();
        setProperties();
        saveConfig();
    }

    private static void readFromProperties() {
        keepInventoryEnabled = keepInventoryEnabledProperty.getBoolean();
        keepInventoryEquipment = keepInventoryEquipmentProperty.getString();
        keepInventoryLoot = keepInventoryLootProperty.getString();

        showDeathLocationEnabled = showDeathLocationEnabledProperty.getBoolean();
        sleepingBagsEnabled = sleepingBagsEnabledProperty.getBoolean();
    }

    private static void getProperties() {
        keepInventoryEnabledProperty = getProperty(CATEGORY_NAME_KEEP_INVENTORY, PROPERTY_KEY_ENABLED, PROPERTY_DEFAULT_VALUE_KEEP_INVENTORY_ENABLED, Property.Type.BOOLEAN);
        keepInventoryEquipmentProperty = getProperty(CATEGORY_NAME_KEEP_INVENTORY, PROPERTY_KEY_EQUIPMENT, PROPERTY_DEFAULT_VALUE_KEEP_INVENTORY_EQUIPMENT, Property.Type.STRING);
        keepInventoryLootProperty = getProperty(CATEGORY_NAME_KEEP_INVENTORY, PROPERTY_KEY_LOOT, PROPERTY_DEFAULT_VALUE_KEEP_INVENTORY_LOOT, Property.Type.STRING);

        showDeathLocationEnabledProperty = getProperty(CATEGORY_NAME_SHOW_DEATH_LOCATION, PROPERTY_KEY_ENABLED, PROPERTY_DEFAULT_VALUE_SHOW_DEATH_LOCATION_ENABLED, Property.Type.BOOLEAN);
        sleepingBagsEnabledProperty = getProperty(CATEGORY_NAME_SLEEPING_BAGS, PROPERTY_KEY_ENABLED, PROPERTY_DEFAULT_VALUE_SLEEPING_BAGS_ENABLED, Property.Type.BOOLEAN);
    }

    private static void setProperties() {
        keepInventoryEnabledProperty.set(keepInventoryEnabled);
        keepInventoryEquipmentProperty.set(keepInventoryEquipment);
        keepInventoryLootProperty.set(keepInventoryLoot);

        showDeathLocationEnabledProperty.set(showDeathLocationEnabled);
        sleepingBagsEnabledProperty.set(sleepingBagsEnabled);
    }

    private static void saveConfig() {
        if(config.hasChanged()) config.save();
    }

    private static Property getProperty(String category, String key, String defaultValue, Property.Type type) {
        String fullKey = "gui.config." + category + "." + key;
        Property prop = config.get(category, key, defaultValue, I18n.format(fullKey + ".comment"), type);
        prop.setLanguageKey(fullKey + ".name");
        return prop;
    }
}
