package com.blackwing.easyExploration;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

import static com.blackwing.easyExploration.EasyExploration.MODID;
import static com.blackwing.easyExploration.EasyExploration.NAME;

@Config(modid = MODID, type = Type.INSTANCE, category = "", name=NAME)
public class Configuration {

    @Name("Save Inventory")
    @Comment("Configure what happens with your items when you die.")
    @LangKey("easyexploration.config.saveInventory")
    public static SubCategorySaveInventory saveInventory = new SubCategorySaveInventory();

    public enum InventoryOption {
        @Name("Keep")
        @Comment("items stay on your avatar")
        @LangKey("easyexploration.config.saveInventory.keep")
        KEEP,
        @Name("Store")
        @Comment("items are stored in a loot crate at death location")
        @LangKey("easyexploration.config.saveInventory.save")
        SAVE,
        @Name("Drop")
        @Comment("item get dropped at death location")
        @LangKey("easyexploration.config.saveInventory.drop")
        DROP
    }

    public static class SubCategorySaveInventory {
        @Name("Enabled")
        @Comment("Toggle feature on/off.")
        @LangKey("easyexploration.config.enabled")
        public boolean enabled = true;
        @Name("Equipment")
        @Comment({
                "What to do with equipped items (item in armor and hotbar slots)?",
                "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location"
        })
        @LangKey("easyexploration.config.saveInventory.equipment")
        public InventoryOption equipment = InventoryOption.KEEP;
        @Name("Loot")
        @Comment({
                "What to do with the loot in the inventory?",
                "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location"
        })
        @LangKey("easyexploration.config.saveInventory.loot")
        public InventoryOption loot = InventoryOption.SAVE;
        @Name("Experience")
        @Comment({
                "What to do with your XP?",
                "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location"
        })
        @LangKey("easyexploration.config.saveInventory.xp")
        public InventoryOption xp = InventoryOption.DROP;
    }

    @Name("Show Death Location")
    @Comment("Configure who should receive your location of death.")
    @LangKey("easyexploration.config.showDeathLocation")
    public static SubCategoryShowDeathLocation showDeathLocation = new SubCategoryShowDeathLocation();

    public enum ShowDeathOption {
        @Name("Noone")
        @Comment("Disables the feature. Death location is not shown.")
        @LangKey("easyexploration.config.showDeathLocation.noone")
        NOONE,
        @Name("Player")
        @Comment("Only the player who died receives the death location message.")
        @LangKey("easyexploration.config.showDeathLocation.player")
        PLAYER,
        @Name("Team")
        @Comment("The team of the player who died receives the death location message.")
        @LangKey("easyexploration.config.showDeathLocation.team")
        TEAM,
        @Name("Everyone")
        @Comment("Every player receives the death location message.")
        @LangKey("easyexploration.config.showDeathLocation.everyone")
        EVERYONE
    }

    public static class SubCategoryShowDeathLocation {
        @Name("Send to")
        @Comment({"Who should receive the death location message?",
                "NOONE => Disables the feature. Death location is not shown.",
                "PLAYER => Only the player who died receives the death location message.",
                "TEAM => The team of the player who died receives the death location message.",
                "EVERYONE => Every player receives the death location message."
        })
        @LangKey("easyexploration.config.showDeathLocation.sendTo")
        public ShowDeathOption sendTo = ShowDeathOption.TEAM;
    }

    @Name("Sleeping Bags")
    @Comment("Configure your travel beds.")
    @LangKey("easyexploration.config.sleepingBags")
    public static SubCategory sleepingBags = new SubCategory();

    @Name("Show damage")
    @Comment("Configure damage information.")
    @LangKey("easyexploration.config.showDamage")
    public static SubCategory showDamage = new SubCategory();

    public static class SubCategory {
        @Name("Enabled")
        @Comment("toggle feature on/off")
        @LangKey("easyexploration.config.enabled")
        public boolean enabled = true;
    }
}
