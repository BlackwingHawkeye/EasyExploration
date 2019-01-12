package com.blackwing.easyExploration.utilities

import java.io.File

import com.blackwing.easyExploration.EasyExploration.modId
import net.minecraftforge.event.entity.player.PlayerEvent

import scala.collection.mutable

object FileStorage {
	/**
	  * Singleton
	  */
	private var instances = mutable.Map[String, FileStorage]()

	/**
	  * @return the instance
	  */
	def instance(featureKey: String): FileStorage =
		instances.getOrElse(featureKey, {
			val store = new FileStorage(featureKey)
			instances.put(featureKey, store)
			store
		})
}

class FileStorage private(val featureChildFolder: String) {

	private val modChildFolder = modId

	/**
	  * Gets a folder reference.
	  * If the folder doesn't exist on the machine, then it will be created.
	  *
	  * @param parentDir The parent abstract pathname
	  * @param childDir  The child pathname string
	  * @return The save folder.
	  */
	private def getDirectory(parentDir: File, childDir: String) = {
		val saveDir = new File(parentDir, childDir)
		if (!saveDir.exists) saveDir.mkdirs
		saveDir
	}

	/**
	  * Gets a save folder for the mod.
	  *
	  * @param playerDir The instance specific save folder for player data.
	  * @return The save folder to use for the mod.
	  */
	private def getModSaveDir(playerDir: File) = getDirectory(playerDir, modChildFolder)

	/**
	  * Gets a save folder for the feature.
	  *
	  * @param playerDir The instance specific save folder for player data.
	  * @return The save folder to use for the feature.
	  */
	private def getFeatureSaveDir(playerDir: File) = getDirectory(getModSaveDir(playerDir), featureChildFolder)

	/**
	  * Gets a save file for a player.
	  *
	  * @param playerDir The instance specific save folder for player data.
	  * @param uuid      The uuid of the player to get a file for.
	  * @return The save file to use for the player.
	  */
	private def getPlayerSaveFile(playerDir: File, uuid: String) = new File(getFeatureSaveDir(playerDir), uuid + ".dat")

	/**
	  * Gets a save file for a player.
	  *
	  * @param event The event that the file is needed for.
	  * @return The save file to use for the player.
	  */
	def getPlayerSaveFile(event: PlayerEvent.SaveToFile): File = getPlayerSaveFile(event.getPlayerDirectory, event.getPlayerUUID)

	def getPlayerSaveFile(event: PlayerEvent.LoadFromFile): File = getPlayerSaveFile(event.getPlayerDirectory, event.getPlayerUUID)
}