package com.blackwing.easyExploration.init;

import com.blackwing.easyExploration.block.base.BlockBase;
import com.blackwing.easyExploration.items.base.ItemBase;
import com.blackwing.easyExploration.tileEntity.base.ITileEntityRenderer;
import com.blackwing.easyExploration.tileEntity.base.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class RegisterHandler {

    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final List<Block> BLOCKS = new ArrayList<Block>();
    public static final List<TileEntity> TILE_ENTITYS = new ArrayList<TileEntity>();
    public static final List<ITileEntityRenderer> TILE_ENTITY_RENDERERS = new ArrayList<ITileEntityRenderer>();

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
        for (TileEntity tileEntity : TILE_ENTITYS)
            if (tileEntity instanceof TileEntityBase)
                GameRegistry.registerTileEntity(tileEntity.getClass(), new ResourceLocation(((TileEntityBase) tileEntity).getId()));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Item item : ITEMS)
            if (item instanceof ItemBase)
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), ((ItemBase) item).getId()));
        for (Block block : BLOCKS)
            if (block instanceof BlockBase) {
                Item item = Item.getItemFromBlock(block);
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), ((BlockBase) block).getId()));
            }
        for (ITileEntityRenderer renderer : TILE_ENTITY_RENDERERS) renderer.register();
    }
}
