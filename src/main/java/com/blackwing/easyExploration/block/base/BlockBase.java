package com.blackwing.easyExploration.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockBase extends Block implements IBlock {

    public BlockBase(Material material) {
        super(material);
        init();
    }
}
