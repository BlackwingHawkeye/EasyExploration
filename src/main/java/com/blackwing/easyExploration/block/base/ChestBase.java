package com.blackwing.easyExploration.block.base;

import net.minecraft.block.BlockChest;

public class ChestBase extends BlockChest implements IBlock {

    public ChestBase(String name, BlockChest.Type type) {
        super(type);
        initBlock(name);
    }
}
