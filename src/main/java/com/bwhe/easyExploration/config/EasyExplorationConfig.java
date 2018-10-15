package com.bwhe.easyExploration.config;

import net.minecraftforge.common.config.Config;

import static com.bwhe.easyExploration.EasyExploration.MODID;

@Config(modid = MODID, category = "")
public class EasyExplorationConfig {

    public enum InventoryOption {
        @Config.Comment("items stay on your avatar")
        KEEP,
        @Config.Comment("items are stored in a loot crate at death location")
        SAVE,
        @Config.Comment("item get dropped at death location")
        DROP
    }

    public static SubCategorySaveInventory saveInventory = new SubCategorySaveInventory();

    public static class SubCategorySaveInventory {
        @Config.Comment("toggle feature on/off")
        public boolean enabled = true;
        @Config.Comment({
                "what to do with equipped items (item in armor and hotbar slots)",
                "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location"
        })
        public InventoryOption equipment = InventoryOption.KEEP;
        @Config.Comment({
                "what to do with the loot in the inventory",
                "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location"
        })
        public InventoryOption loot = InventoryOption.SAVE;
        @Config.Comment({
                "what to do with your XP",
                "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location"
        })
        public InventoryOption xp = InventoryOption.DROP;
    }

    public static SubCategory showDeathLocation = new SubCategory();

    public static SubCategory sleepingBags = new SubCategory();

    public static SubCategory showDamage = new SubCategory();

    public static class SubCategory {
        @Config.Comment("toggle feature on/off")
        public boolean enabled = true;
    }

}
