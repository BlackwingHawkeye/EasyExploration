package com.blackwing.easyExploration.items.base;

import com.blackwing.easyExploration.EasyExploration;
import com.blackwing.easyExploration.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name)
    {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MATERIALS);

        EasyExploration.ITEMS.add(this);
    }

    @Override
    public void registerModels()
    {
        EasyExploration.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
