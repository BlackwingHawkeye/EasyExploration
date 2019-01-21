package com.blackwing.easyExploration.block.base;

import net.minecraft.block.BlockChest;

public abstract class ChestBase extends BlockChest implements IBlock {

    public ChestBase(BlockChest.Type type) {
        super(type);
        init();
    }
}
