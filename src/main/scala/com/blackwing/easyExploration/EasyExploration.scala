package com.blackwing.easyExploration

import com.blackwing.easyExploration.proxy.CommonProxy
import com.blackwing.easyExploration.utilities.{EventHandlerBase, IHasModel}
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.event.{FMLFingerprintViolationEvent, FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.{Mod, SidedProxy}

@Mod(
	useMetadata = true,
	modid = EasyExploration.modId,
	name = EasyExploration.name,
	version = EasyExploration.version,
	acceptedMinecraftVersions = "1.12",
	canBeDeactivated = true,
	certificateFingerprint = EasyExploration.fingerprint,
	modLanguage = "scala"
)
object EasyExploration extends EventHandlerBase {

	val modId: String = Reference.modId
	val name: String = Reference.name
	val version: String = Reference.version
	val fingerprint: String = Reference.fingerprint

	@SidedProxy(clientSide = "com.blackwing.easyExploration.proxy.ClientProxy", serverSide = "com.blackwing.easyExploration.proxy.CommonProxy")
	val proxy: CommonProxy.type = _

	/*
	 * Here we check if our mod has been tampered with
	 */
	@Mod.EventHandler
	def invalidFingerprint(event: FMLFingerprintViolationEvent): Unit =
		if (fingerprint != "@FINGERPRINT@") logger.warn("THE EASYEXPLORATION MOD THAT YOU ARE USING HAS BEEN CHANGED/TAMPERED WITH!")

	// public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.instance.newSimpleChannel(MODID)

	@Mod.EventHandler
	override def onPreInit(event: FMLPreInitializationEvent): Unit = {
		super.onPreInit(event)
		proxy.onPreInit(event)
	}

	@Mod.EventHandler
	override def onInit(event: FMLInitializationEvent): Unit = {
		super.onInit(event)
		proxy.onInit(event)
	}

	@Mod.EventHandler
	override def onPostInit(event: FMLPostInitializationEvent): Unit = {
		super.onPostInit(event)
		proxy.onPostInit(event)
	}

	var items: List[Item] = List.empty
	var blocks: List[Block] = List.empty

	@SubscribeEvent
	def onItemRegister(event: RegistryEvent.Register[Item]): Unit =
		event.getRegistry.registerAll(items: _*)

	@SubscribeEvent
	def onBlockRegister(event: RegistryEvent.Register[Block]): Unit =
		event.getRegistry.registerAll(blocks: _*)

	@SubscribeEvent
	def onModelRegister(event: ModelRegistryEvent): Unit = {
		items.filter(_.isInstanceOf[IHasModel]).foreach(_.asInstanceOf[IHasModel].registerModels())

		blocks.filter(_.isInstanceOf[IHasModel]).foreach(_.asInstanceOf[IHasModel].registerModels())
	}
}
