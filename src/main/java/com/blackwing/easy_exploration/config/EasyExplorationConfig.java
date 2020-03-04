package com.blackwing.easy_exploration.config;

import com.blackwing.easy_exploration.EasyExploration;

/**
 * This holds the runtime values for our config. These values should never be changed from outside this package.
 * This can be split into multiple classes (Server, Client, Player, Common) but has been kept in one class for simplicity.
 */
public final class EasyExplorationConfig {

    enum InventoryOption {
        KEEP("keep", "items stay on your avatar"),
        SAVE("store", "items are stored in a loot crate at death location"),
        DROP("keep", "item get dropped at death location");

        private final String translationKey;
        private final String comment;

        InventoryOption(String translationKeyIn, String commentIn) {
            this.translationKey = EasyExploration.MODID + ".inventoryOption." + translationKeyIn;
            this.comment = commentIn;
        }
    }

    enum ShowDeathLocationOption {
        NOONE("noone", "Disables the feature. Death location is not shown."),
        PLAYER("player", "Only the player who died receives the death location message."),
        TEAM("team", "The team of the player who died receives the death location message."),
        EVERYONE("everyone", "Every player receives the death location message.");

        private final String translationKey;
        private final String comment;

        ShowDeathLocationOption(String translationKeyIn, String commentIn) {
            this.translationKey = EasyExploration.MODID + ".showDeathLocationOption." + translationKeyIn;
            this.comment = commentIn;
        }
    }

    // Client
    public static boolean clientSaveInventoryEnabled;
    public static InventoryOption clientSaveInventoryEquipment;
    public static InventoryOption clientSaveInventoryLoot;
    public static InventoryOption clientSaveInventoryXP;

    public static ShowDeathLocationOption clientShowDeathLocation;

    public static boolean clientSleepingBagsEnabled;

    public static boolean clientShowDamage;

    // Server
    public static boolean serverSaveInventoryEnabled;
    public static InventoryOption serverSaveInventoryEquipment;
    public static InventoryOption serverSaveInventoryLoot;
    public static InventoryOption serverSaveInventoryXP;

    public static ShowDeathLocationOption serverShowDeathLocation;

    public static boolean serverSleepingBagsEnabled;

    public static boolean serverShowDamage;

}
