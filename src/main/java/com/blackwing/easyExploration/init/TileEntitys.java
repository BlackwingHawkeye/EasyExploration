package com.blackwing.easyExploration.init;

import com.blackwing.easyExploration.EasyExploration;
import com.blackwing.easyExploration.tileEntity.TileEntityDeathChest;
import com.blackwing.easyExploration.tileEntity.TileEntityRendererDeathChest;

public class TileEntitys {
    public static TileEntityDeathChest tileEntityDeathChest = new TileEntityDeathChest();
    public static TileEntityRendererDeathChest tileEntityRendererDeathChest = new TileEntityRendererDeathChest();

    static {
        EasyExploration.proxy.registerTileEntityAndRenderer(TileEntityDeathChest.class, "deathchest", tileEntityRendererDeathChest);
    }
}
