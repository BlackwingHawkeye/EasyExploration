package com.blackwing.easyExploration.blocks;

import com.blackwing.easyExploration.blocks.base.ChestBase;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DeathChest extends ChestBase {

    private EntityPlayer chestOwner;

    public DeathChest(String name) {
        super(name, BlockChest.Type.BASIC);
        setLightLevel(8);
        setBlockUnbreakable();
        setResistance(10);
        setSoundType(SoundType.WOOD);
    }

    public DeathChest setChestOwner(EntityPlayer player) {
        this.chestOwner = player;
        return this;
    }

    /**
     * Called when the block is right clicked by a player.
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (chestOwner == playerIn)
            super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);

        return true;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (placer instanceof EntityPlayer) setChestOwner((EntityPlayer) placer);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public String getLocalizedName() {
        if (chestOwner == null) return super.getLocalizedName();
        return chestOwner.getDisplayNameString();
    }
}
