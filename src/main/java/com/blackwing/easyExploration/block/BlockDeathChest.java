package com.blackwing.easyExploration.block;

import com.blackwing.easyExploration.EasyExploration;
import com.blackwing.easyExploration.block.base.ChestBase;
import com.blackwing.easyExploration.tileEntity.TileEntityDeathChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class BlockDeathChest extends ChestBase {

    private final Logger log = LogManager.getLogger(EasyExploration.MODID + "." + getClass());

    @Override
    public String getId() {
        return "deathchest";
    }

    @Override
    public String getName() {
        return "deathchest";
    }

    public BlockDeathChest() {
        super(BlockChest.Type.BASIC);
        setLightLevel(8);
        setBlockUnbreakable();
        setResistance(10);
        setSoundType(SoundType.WOOD);
        log.info("BlockDeathChest created");
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
    public boolean onBlockActivated(World world, @NotNull BlockPos pos, IBlockState state, @NotNull EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // get the tile entity
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityDeathChest)) return true;
        log.info("It's a death chest!");
        TileEntityDeathChest tileEntityDeathChest = (TileEntityDeathChest) tileEntity;

        if (!tileEntityDeathChest.isUsableByPlayer(player)) return true;
        log.info("It's usable!");
        if (world.getBlockState(pos.up()).doesSideBlockChestOpening(world, pos.up(), EnumFacing.DOWN)) return true;
        log.info("It's facing right!");
        if (world.isRemote) return true;
        log.info("It's on the server!");

        player.displayGUIChest(getInventory(world, pos));

        return true;
    }

    @Nullable
    public ILockableContainer getLockableContainer(@NotNull World world, @NotNull BlockPos pos) {
        return getContainer(world, pos, true);
    }

    @Nullable
    public ILockableContainer getContainer(World world, @NotNull BlockPos pos, boolean allowBlocking) {
        return (ILockableContainer) getInventory(world, pos);
    }

    @Nullable
    public IInventory getInventory(@NotNull World world, @NotNull BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity == null) {
            log.error("TileEnitiy is null.");
            return null;
        }

        if (!(tileEntity instanceof TileEntityDeathChest)) {
            log.error("TileEnitiy is not a death chest. " + tileEntity.getClass());
            return null;
        }

        return ((TileEntityDeathChest) tileEntity).getInventory();
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

        if (!player.world.getWorldBorder().contains(pos)) return pos;

        AtomicReference<IBlockState> blockState = new AtomicReference<>(getStateForPlacement(
                player.world, pos, player.getHorizontalFacing(), hitX, hitY, hitZ, 0, player, EnumHand.MAIN_HAND));

        if (new ItemBlock(this).placeBlockAt(deathStack, player, player.world, pos, player.getHorizontalFacing(), hitX, hitY, hitZ, blockState.get())) {
            blockState.set(player.world.getBlockState(pos));
            SoundType soundtype = blockState.get().getBlock().getSoundType(blockState.get(), player.world, pos, player);
            player.world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            deathStack.shrink(1);
            log.info("death chest placed");
        }
        return pos;
    }
}
