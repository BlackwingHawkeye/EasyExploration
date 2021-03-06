package com.blackwing.easyExploration.util;

import net.minecraftforge.event.entity.player.PlayerEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.blackwing.easyExploration.EasyExploration.MODID;

public class FileStorage {

    public static final String FEATURE_KEY_SAVEINVENTORY = "saveinventory";

    /**
     * Singleton
     */
    private static final Map<String, FileStorage> instances = new HashMap<>();

    /**
     * @return the instance
     */
    public static FileStorage instance(String featureKey) {
        if (!instances.containsKey(featureKey)) instances.put(featureKey, new FileStorage(featureKey));
        return instances.get(featureKey);
    }

    private String modChildFolder;
    private String featureChildFolder;

    /**
     * Constructor
     *
     * @param featureChildFolder
     */
    private FileStorage(String featureChildFolder) {
        this.modChildFolder = MODID;
        this.featureChildFolder = featureChildFolder;
    }

    /**
     * Gets a folder reference.
     * If the folder doesn't exist on the machine, then it will be created.
     *
     * @param parentDir The parent abstract pathname
     * @param childDir  The child pathname string
     * @return The save folder.
     */
    private static File getDirectory(File parentDir, String childDir) {

        final File saveDir = new File(parentDir, childDir);

        if (!saveDir.exists()) saveDir.mkdirs();

        return saveDir;
    }

    /**
     * Gets a save folder for the mod.
     *
     * @param playerDir The instance specific save folder for player data.
     * @return The save folder to use for the mod.
     */
    private File getModSaveDir(File playerDir) {
        return getDirectory(playerDir, modChildFolder);
    }

    /**
     * Gets a save folder for the feature.
     *
     * @param playerDir The instance specific save folder for player data.
     * @return The save folder to use for the feature.
     */
    private File getFeatureSaveDir(File playerDir) {
        return getDirectory(getModSaveDir(playerDir), featureChildFolder);
    }

    /**
     * Gets a save file for a player.
     *
     * @param playerDir The instance specific save folder for player data.
     * @param uuid      The uuid of the player to get a file for.
     * @return The save file to use for the player.
     */
    private File getPlayerSaveFile(File playerDir, String uuid) {
        return new File(getFeatureSaveDir(playerDir), uuid + ".dat");
    }

    /**
     * Gets a save file for a player.
     *
     * @param event The event that the file is needed for.
     * @return The save file to use for the player.
     */
    public File getPlayerSaveFile(PlayerEvent.SaveToFile event) {
        return getPlayerSaveFile(event.getPlayerDirectory(), event.getPlayerUUID());
    }

    /**
     * Gets a save file for a player.
     *
     * @param event The event that the file is needed for.
     * @return The save file to use for the player.
     */
    public File getPlayerSaveFile(PlayerEvent.LoadFromFile event) {
        return getPlayerSaveFile(event.getPlayerDirectory(), event.getPlayerUUID());
    }
}
