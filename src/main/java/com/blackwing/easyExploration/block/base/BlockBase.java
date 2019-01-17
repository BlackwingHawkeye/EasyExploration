package com.blackwing.easyExploration.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block implements IBlock {

    public BlockBase(String name, Material material) {
        super(material);
        initBlock(name);
    }
}
