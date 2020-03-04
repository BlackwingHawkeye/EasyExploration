package com.blackwing.easy_exploration.config;

import com.blackwing.easy_exploration.EasyExploration;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * For configuration settings that change the behaviour of code on the LOGICAL SERVER.
 */
final class ServerConfig {

    final ForgeConfigSpec.BooleanValue serverSaveInventoryEnabled;
    final ForgeConfigSpec.EnumValue<EasyExplorationConfig.InventoryOption> serverSaveInventoryEquipment;
    final ForgeConfigSpec.EnumValue<EasyExplorationConfig.InventoryOption> serverSaveInventoryLoot;
    final ForgeConfigSpec.EnumValue<EasyExplorationConfig.InventoryOption> serverSaveInventoryXP;

    final ForgeConfigSpec.EnumValue<EasyExplorationConfig.ShowDeathLocationOption> serverShowDeathLocation;

    final ForgeConfigSpec.BooleanValue serverSleepingBagsEnabled;

    final ForgeConfigSpec.BooleanValue serverShowDamage;

    ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        serverSaveInventoryEnabled = builder
                .comment("Configure what happens with your items when you die.", "toggle feature on/off")
                .translation(EasyExploration.MODID + ".config.saveInventory")
                .define("saveInventoryEnabled", true);
        serverSaveInventoryEquipment = builder
                .comment("What to do with equipped items (item in armor and hotbar slots)?", "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location")
                .translation(EasyExploration.MODID + ".config.saveInventory.equipment")
                .defineEnum("saveInventoryEquipment", EasyExplorationConfig.InventoryOption.KEEP);
        serverSaveInventoryLoot = builder
                .comment("What to do with the loot in the inventory?", "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location")
                .translation(EasyExploration.MODID + ".config.saveInventory.loot")
                .defineEnum("saveInventoryLoot", EasyExplorationConfig.InventoryOption.SAVE);
        serverSaveInventoryXP = builder
                .comment("What to do with your XP?", "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location")
                .translation(EasyExploration.MODID + ".config.saveInventory.xp")
                .defineEnum("saveInventoryXP", EasyExplorationConfig.InventoryOption.DROP);

        serverShowDeathLocation = builder
                .comment("Configure who should receive your location of death.")
                .translation(EasyExploration.MODID + ".config.showDeathLocation")
                .defineEnum("showDeathLocation", EasyExplorationConfig.ShowDeathLocationOption.TEAM);

        serverSleepingBagsEnabled = builder
                .comment("Configure your travel beds.", "toggle feature on/off")
                .translation(EasyExploration.MODID + ".config.sleepingBags")
                .define("sleepingBags", true);

        serverShowDamage = builder
                .comment("Configure damage information.", "toggle feature on/off")
                .translation(EasyExploration.MODID + ".config.showDamage")
                .define("showDamage", true);
        builder.pop();
    }
}
