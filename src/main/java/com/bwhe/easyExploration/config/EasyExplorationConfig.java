package com.bwhe.easyExploration.config;

import net.minecraftforge.common.config.Config;

import static com.bwhe.easyExploration.EasyExploration.MODID;

@Config(modid = MODID, category = "")
public class EasyExplorationConfig {

    public static SubCategorySaveInventory saveInventory = new SubCategorySaveInventory();
    public enum InventoryOption {
        @Config.Comment("items stay on your avatar")
        KEEP,
        @Config.Comment("items are stored in a loot crate at death location")
        SAVE,
        @Config.Comment("item get dropped at death location")
        DROP
    }
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

    public static SubCategoryShowDeathLocation showDeathLocation = new SubCategoryShowDeathLocation();
    public enum ShowDeathOption {
        @Config.Comment("Disables the feature. Death location is not shown.")
        NOONE,
        @Config.Comment("Only the player who died receives the death location message.")
        PLAYER,
        @Config.Comment("The team of the player who died receives the death location message.")
        TEAM,
        @Config.Comment("Every player receives the death location message.")
        EVERYONE
    }
    public static class SubCategoryShowDeathLocation {
        @Config.Comment({"Who should receive the death location message?",
                "NOONE => Disables the feature. Death location is not shown.",
                "PLAYER => Only the player who died receives the death location message.",
                "TEAM => The team of the player who died receives the death location message.",
                "EVERYONE => Every player receives the death location message."
        })
        public ShowDeathOption sendTo = ShowDeathOption.TEAM;
    }

    public static SubCategory sleepingBags = new SubCategory();

    public static SubCategory showDamage = new SubCategory();

    public static class SubCategory {
        @Config.Comment("toggle feature on/off")
        public boolean enabled = true;
    }

}
