package com.blackwing.easyExploration.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block implements IBlock {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BlockBase(String id, Material material) {
        super(material);
        init(id);
    }
}
