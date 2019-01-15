package com.blackwing.easyExploration.proxy;

import com.blackwing.easyExploration.saveInventory.SaveInventoryHandlerClient;
import com.blackwing.easyExploration.saveInventory.SaveInventoryHandlerCommon;
import com.blackwing.easyExploration.showDamage.ShowDamageHandlerClient;
import com.blackwing.easyExploration.showDamage.ShowDamageHandlerCommon;
import com.blackwing.easyExploration.showDeathLocation.ShowDeathLocationHandlerClient;
import com.blackwing.easyExploration.showDeathLocation.ShowDeathLocationHandlerCommon;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Client extends Common {

    public static final SaveInventoryHandlerCommon saveInventoryHandler = new SaveInventoryHandlerClient();
    public static final ShowDeathLocationHandlerCommon showDeathLocationHandler = new ShowDeathLocationHandlerClient();
    public static final ShowDamageHandlerCommon showDamageHandler = new ShowDamageHandlerClient();

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
