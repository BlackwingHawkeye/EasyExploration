package com.blackwing.easyExploration.tileEntity.base;

import com.blackwing.easyExploration.init.RegisterHandler;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public TileEntityBase() {
        super();
        RegisterHandler.TILE_ENTITYS.add(this);
    }
}
