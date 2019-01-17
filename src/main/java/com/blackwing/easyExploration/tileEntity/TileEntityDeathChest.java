package com.blackwing.easyExploration.tileEntity;

import com.blackwing.easyExploration.init.Blocks;
import com.blackwing.easyExploration.init.SoundEvents;
import com.blackwing.easyExploration.inventory.InventoryDeathChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityDeathChest extends TileEntityChest implements ITickable {

    public EntityPlayer chestOwner;
    public InventoryDeathChest chestInventory;
    public float lidAngle;
    /**
     * The angle of the death chest lid last tick
     */
    public float prevLidAngle;
    public boolean isInUse;
    private int ticksSinceSync;

    public void setChestOwner(EntityPlayer player) {
        chestOwner = player;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update() {
        if (++ticksSinceSync % 20 * 4 == 0) world.addBlockEvent(pos, Blocks.DEATH_CHEST, 1, isInUse ? 1 : 0);

        prevLidAngle = lidAngle;
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        float f = 0.1F;

        if (isInUse && lidAngle == 0.0F) {
            double d0 = (double) i + 0.5D;
            double d1 = (double) k + 0.5D;
            world.playSound((EntityPlayer) null, d0, (double) j + 0.5D, d1, SoundEvents.BLOCK_DEATHCHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (!isInUse && lidAngle > 0.0F || isInUse && lidAngle < 1.0F) {
            float f2 = lidAngle;

            if (isInUse) lidAngle += 0.1F;
            else lidAngle -= 0.1F;

            if (lidAngle > 1.0F) lidAngle = 1.0F;

            float f1 = 0.5F;

            if (lidAngle < 0.5F && f2 >= 0.5F) {
                double d3 = (double) i + 0.5D;
                double d2 = (double) k + 0.5D;
                world.playSound((EntityPlayer) null, d3, (double) j + 0.5D, d2, SoundEvents.BLOCK_DEATHCHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F) lidAngle = 0.0F;
        }
    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            isInUse = type > 0;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate() {
        updateContainingBlockInfo();
        super.invalidate();
    }

    public void openChest() {
        isInUse = true;
        world.addBlockEvent(pos, Blocks.DEATH_CHEST, 1, 1);
    }

    public void closeChest() {
        isInUse = false;
        world.addBlockEvent(pos, Blocks.DEATH_CHEST, 1, 0);
    }

    public boolean canBeUsed(EntityPlayer player) {
        if (world.getTileEntity(pos) != this) return false;
        if (chestOwner != player) return false;
        return player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
    }
}
