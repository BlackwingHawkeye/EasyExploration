package com.blackwing.easyExploration.proxy;

import com.blackwing.easyExploration.saveInventory.SaveInventoryHandlerClient;
import com.blackwing.easyExploration.saveInventory.SaveInventoryHandlerCommon;
import com.blackwing.easyExploration.showDamage.ShowDamageHandlerClient;
import com.blackwing.easyExploration.showDamage.ShowDamageHandlerCommon;
import com.blackwing.easyExploration.showDeathLocation.ShowDeathLocationHandlerClient;
import com.blackwing.easyExploration.showDeathLocation.ShowDeathLocationHandlerCommon;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Client extends Common {

    public static final SaveInventoryHandlerCommon saveInventoryHandler = new SaveInventoryHandlerClient();
    public static final ShowDeathLocationHandlerCommon showDeathLocationHandler = new ShowDeathLocationHandlerClient();
    public static final ShowDamageHandlerCommon showDamageHandler = new ShowDamageHandlerClient();

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ResourceLocation resourceLocation = item.getRegistryName();
        if (resourceLocation == null) return;
        log.info("registerItemRenderer: location" + resourceLocation.toString() + "+" + id);
        ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), id);
        log.info("registerItemRenderer: model=" + model.toString());
        ModelLoader.setCustomModelResourceLocation(item, meta, model);
    }

    @Override
    public <T extends TileEntity> void registerTileEntityAndRenderer(Class<T> tileEntityClass, String id, TileEntitySpecialRenderer<? super T> specialRenderer) {
        ClientRegistry.registerTileEntity(tileEntityClass, id, specialRenderer);
    }
}
