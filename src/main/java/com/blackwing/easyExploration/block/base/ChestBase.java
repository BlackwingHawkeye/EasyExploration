package com.blackwing.easyExploration.block.base;

import net.minecraft.block.BlockChest;

public class ChestBase extends BlockChest implements IBlock {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ChestBase(String id, Type type) {
        super(type);
        init(id);
    }
}
