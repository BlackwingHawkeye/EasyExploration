package com.blackwing.easy_exploration.config;

import com.blackwing.easy_exploration.EasyExploration;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * For configuration settings that change the behaviour of code on the LOGICAL CLIENT.
 */
final class ClientConfig {

    final ForgeConfigSpec.BooleanValue clientSaveInventoryEnabled;
    final ForgeConfigSpec.EnumValue<EasyExplorationConfig.InventoryOption> clientSaveInventoryEquipment;
    final ForgeConfigSpec.EnumValue<EasyExplorationConfig.InventoryOption> clientSaveInventoryLoot;
    final ForgeConfigSpec.EnumValue<EasyExplorationConfig.InventoryOption> clientSaveInventoryXP;

    final ForgeConfigSpec.EnumValue<EasyExplorationConfig.ShowDeathLocationOption> clientShowDeathLocation;

    final ForgeConfigSpec.BooleanValue clientSleepingBagsEnabled;

    final ForgeConfigSpec.BooleanValue clientShowDamage;

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        clientSaveInventoryEnabled = builder
                .comment("Configure what happens with your items when you die.", "toggle feature on/off")
                .translation(EasyExploration.MODID + ".config.saveInventory")
                .define("saveInventoryEnabled", true);
        clientSaveInventoryEquipment = builder
                .comment("What to do with equipped items (item in armor and hotbar slots)?", "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location")
                .translation(EasyExploration.MODID + ".config.saveInventory.equipment")
                .defineEnum("saveInventoryEquipment", EasyExplorationConfig.InventoryOption.KEEP);
        clientSaveInventoryLoot = builder
                .comment("What to do with the loot in the inventory?", "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location")
                .translation(EasyExploration.MODID + ".config.saveInventory.loot")
                .defineEnum("saveInventoryLoot", EasyExplorationConfig.InventoryOption.SAVE);
        clientSaveInventoryXP = builder
                .comment("What to do with your XP?", "\"drop\" => item get dropped at death location; \"keep\" => items stay on your avatar; \"save\" => items are stored in a loot crate at death location")
                .translation(EasyExploration.MODID + ".config.saveInventory.xp")
                .defineEnum("saveInventoryXP", EasyExplorationConfig.InventoryOption.DROP);

        clientShowDeathLocation = builder
                .comment("Configure who should receive your location of death.")
                .translation(EasyExploration.MODID + ".config.showDeathLocation")
                .defineEnum("showDeathLocation", EasyExplorationConfig.ShowDeathLocationOption.TEAM);

        clientSleepingBagsEnabled = builder
                .comment("Configure your travel beds.", "toggle feature on/off")
                .translation(EasyExploration.MODID + ".config.sleepingBags")
                .define("sleepingBags", true);

        clientShowDamage = builder
                .comment("Configure damage information.", "toggle feature on/off")
                .translation(EasyExploration.MODID + ".config.showDamage")
                .define("showDamage", true);
        builder.pop();
    }
}