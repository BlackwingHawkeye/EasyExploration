package com.blackwing.easyExploration.block;

import com.blackwing.easyExploration.block.base.ChestBase;
import com.blackwing.easyExploration.tileEntity.TileEntityDeathChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDeathChest extends ChestBase {

    private Logger logger;

    public BlockDeathChest() {
        super("death_chest", BlockChest.Type.BASIC);
        setLightLevel(8);
        setBlockUnbreakable();
        setResistance(10);
        setSoundType(SoundType.WOOD);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    @NotNull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    /**
     * Called when the block is right clicked by a player.
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // get the tile entity
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityDeathChest)) return true;
        TileEntityDeathChest tileEntityDeathChest = (TileEntityDeathChest) tileEntity;

        if (!tileEntityDeathChest.canBeUsed(playerIn)) return true;
        if (tileEntityDeathChest.chestInventory == null) return true;
        if (worldIn.getBlockState(pos.up()).doesSideBlockChestOpening(worldIn, pos.up(), EnumFacing.DOWN)) return true;
        if (worldIn.isRemote) return true;

        tileEntityDeathChest.chestInventory.setTileEntityDeathChest(tileEntityDeathChest);
        playerIn.displayGUIChest(tileEntityDeathChest.chestInventory);
        return true;
    }

    @Nullable
    public TileEntityDeathChest getContainer(World worldIn, BlockPos pos, boolean allowBlocking) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (!(tileEntity instanceof TileEntityDeathChest)) return null;

        return (TileEntityDeathChest) tileEntity;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityDeathChest && placer instanceof EntityPlayer)
            ((TileEntityDeathChest) tileEntity).setChestOwner((EntityPlayer) placer);
    }

    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param player The player who died.
     */
    public BlockPos placeDeathChest(@NotNull EntityPlayer player) {
        ItemStack deathStack = new ItemStack(this);
        BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
        float hitX = (float) pos.getX();
        float hitY = (float) pos.getY();
        float hitZ = (float) pos.getZ();
        if (!player.world.getWorldBorder().contains(pos)) {
            logger.error("Death chest position out of world borders. Block:{} World{}", pos, player.world.getWorldBorder());
            return pos;
        }
        IBlockState iblockstate1 = getStateForPlacement(
                player.world, pos, player.getHorizontalFacing(), hitX, hitY, hitZ, 0, player, EnumHand.MAIN_HAND);

        if (new ItemBlock(this).placeBlockAt(deathStack, player, player.world, pos, player.getHorizontalFacing(), hitX, hitY, hitZ, iblockstate1)) {
            iblockstate1 = player.world.getBlockState(pos);
            SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, player.world, pos, player);
            player.world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            deathStack.shrink(1);
        }
        return pos;
    }

}
