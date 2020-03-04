package com.blackwing.easy_exploration.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

/**
 * This holds the Client & Server Configs and the Client & Server ConfigSpecs.
 * This bakes the config values to normal fields
 * It can be merged into the main ExampleModConfig class, but is separate because of personal preference and to keep the code organised
 *
 * @author Cadiboo
 */
public class Configuration {

    public static final ClientConfig CLIENT;
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
    }

    public static void makeClient(final ModConfig config) {
        EasyExplorationConfig.clientSaveInventoryEnabled = Configuration.CLIENT.clientSaveInventoryEnabled.get();
        EasyExplorationConfig.clientSaveInventoryEquipment = Configuration.CLIENT.clientSaveInventoryEquipment.get();
        EasyExplorationConfig.clientSaveInventoryLoot = Configuration.CLIENT.clientSaveInventoryLoot.get();
        EasyExplorationConfig.clientSaveInventoryXP = Configuration.CLIENT.clientSaveInventoryXP.get();

        EasyExplorationConfig.clientShowDeathLocation = Configuration.CLIENT.clientShowDeathLocation.get();

        EasyExplorationConfig.clientSleepingBagsEnabled = Configuration.CLIENT.clientSleepingBagsEnabled.get();

        EasyExplorationConfig.clientShowDamage = Configuration.CLIENT.clientShowDamage.get();
    }

    public static void makeServer(final ModConfig config) {
        EasyExplorationConfig.serverSaveInventoryEnabled = Configuration.SERVER.serverSaveInventoryEnabled.get();
        EasyExplorationConfig.serverSaveInventoryEquipment = Configuration.SERVER.serverSaveInventoryEquipment.get();
        EasyExplorationConfig.serverSaveInventoryLoot = Configuration.SERVER.serverSaveInventoryLoot.get();
        EasyExplorationConfig.serverSaveInventoryXP = Configuration.SERVER.serverSaveInventoryXP.get();

        EasyExplorationConfig.serverShowDeathLocation = Configuration.SERVER.serverShowDeathLocation.get();

        EasyExplorationConfig.serverSleepingBagsEnabled = Configuration.SERVER.serverSleepingBagsEnabled.get();

        EasyExplorationConfig.serverShowDamage = Configuration.SERVER.serverShowDamage.get();
    }
}
