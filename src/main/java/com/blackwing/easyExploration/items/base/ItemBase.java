package com.blackwing.easyExploration.items.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IItem {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ItemBase(String id, CreativeTabs tab) {
        super();
        init(id, tab);
    }

}
