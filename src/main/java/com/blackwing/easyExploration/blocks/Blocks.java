package com.blackwing.easyExploration.blocks;

import net.minecraft.block.Block;

public class Blocks {
    private static Blocks ourInstance = new Blocks();

    public static Blocks getInstance() {
        return ourInstance;
    }

    private Blocks() {
    }

    public static final Block DeathChest = new DeathChest("death_chest");
}
