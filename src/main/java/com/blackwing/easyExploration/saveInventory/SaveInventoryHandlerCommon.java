package com.blackwing.easyExploration.saveInventory;

import com.blackwing.easyExploration.blocks.Blocks;
import com.blackwing.easyExploration.config.Configuration;
import com.blackwing.easyExploration.utilities.EventHandlerBase;
import com.blackwing.easyExploration.utilities.FileStorage;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile;
import net.minecraftforge.event.entity.player.PlayerEvent.SaveToFile;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.io.File;
import java.io.IOException;

public class SaveInventoryHandlerCommon extends EventHandlerBase {

    private static final Configuration.SubCategorySaveInventory config = Configuration.saveInventory;
    private static final SaveInventory saveInventory = SaveInventory.instance();
    private static final FileStorage fileStorage = FileStorage.instance("saveinventory");

    private boolean canNotDo(Entity entity) {
        // feature is disabled
        if (!config.enabled) return true;
        // not a player
        if (!(entity instanceof EntityPlayer)) return true;
        // MC own features trump mine
        if (entity.world.getGameRules().getBoolean("keepInventory")) return true;
        // player is not really here
        if (((EntityPlayer) entity).isSpectator()) return true;
        // all right, let's do that
        return false;
    }

    private BlockPos placeDeathChest(EntityPlayer player) {
        ItemStack deathStack = new ItemStack(Blocks.DeathChest);
        BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
        float hitX = (float) pos.getX();
        float hitY = (float) pos.getY();
        float hitZ = (float) pos.getZ();
        if (!player.world.getWorldBorder().contains(pos)) {
            logger.error("Death chest position out of world borders. Block:{} World{}", pos, player.world.getWorldBorder());
            return pos;
        }
        IBlockState iblockstate1 = Blocks.DeathChest.getStateForPlacement(
                player.world, pos, player.getHorizontalFacing(), hitX, hitY, hitZ, 0, player, EnumHand.MAIN_HAND);

        if (new ItemBlock(Blocks.DeathChest).placeBlockAt(deathStack, player, player.world, pos, player.getHorizontalFacing(), hitX, hitY, hitZ, iblockstate1)) {
            iblockstate1 = player.world.getBlockState(pos);
            SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, player.world, pos, player);
            player.world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            deathStack.shrink(1);
        }
        return pos;
    }

    /**
     * The player is being loaded from the world save.
     * Load an additional file from the players directory containing additional mod related player data.
     */
    @SubscribeEvent
    public void onLoad(LoadFromFile event) {
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = event.getEntityPlayer();
        final InventoryPlayer playerInventory = new InventoryPlayer(player);

        try {
            final File playerFile = fileStorage.getPlayerSaveFile(event);
            // this is legit if the player is new in this game world
            if (!playerFile.exists()) logger.warn("Player file not found. " + playerFile.getPath());
            else {
                final NBTTagCompound compound = CompressedStreamTools.read(playerFile);
                if (compound == null) throw new IOException("Can't read from file " + playerFile.getPath());

                playerInventory.readFromNBT(compound.getTagList("Inventory", 10));
                playerInventory.currentItem = compound.getInteger("SelectedItemSlot");

                logger.info("Loaded {} item stacks for {}.", SaveInventory.count(playerInventory), player.getName());
            }
        } catch (final Exception e) {
            logger.error("Could not load inventory for {}. {}", player.getName(), e.getMessage());
            logger.catching(e);
        } finally {
            saveInventory.put(player, playerInventory);
        }
    }

    /**
     * If the player is a valid server side player, their data will be synced to the client.
     * Syncs a client's data with the data that is on the server. This can only be called server side.
     */
    @SubscribeEvent
    public void onLoggedIn(PlayerLoggedInEvent event) {
        // should we do something?
        if (canNotDo(event.player)) return;
        EntityPlayerMP player = (EntityPlayerMP) event.player;

        logger.info("Player {} logged in. Sending sync package to player client.", player.getName());
        // Send Client a package containing the inventory to be synced
        player.sendContainerToPlayer(player.inventoryContainer);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = (EntityPlayer) event.getEntity();
        final int totalInventoryCount = SaveInventory.count(player.inventory);

        logger.info(player.getName() + " died. Attempting to save inventory.");
        saveInventory.destroyVanishingCursedItems(player.inventory);
        InventoryBasic storeInventory = saveInventory.keepInventory(player);

        BlockPos pos = placeDeathChest(player);
        ILockableContainer storeContainer = ((BlockChest) Blocks.DeathChest).getLockableContainer(player.world, pos);
        if (storeContainer instanceof TileEntityChest)
            saveInventory.storeInventory(storeInventory, (TileEntityChest) storeContainer);

        logger.info("Kept {} and stored {} of {} item stacks.", SaveInventory.count(saveInventory.get(player)), SaveInventory.count(storeContainer), totalInventoryCount);
    }

    @SubscribeEvent
    public void onClone(Clone event) {
        // noone died
        if (!event.isWasDeath()) return;
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = event.getEntityPlayer();
        final InventoryPlayer playerInventory = saveInventory.get(player);

        logger.info(player.getName() + " respawned. Attempting to restore inventory.");
        player.inventory.copyInventory(playerInventory);
        logger.info("Restored {} of {} item stacks.", SaveInventory.count(player.inventory), SaveInventory.count(playerInventory));

        saveInventory.remove(player);
    }

    /**
     * The player is being saved to the world store.
     * This event saves the additional mod related player data to the world store.
     * <em>WARNING</em>: Do not overwrite the player's .dat file here. You will corrupt the world state.
     */
    @SubscribeEvent
    public void onSave(SaveToFile event) {
        // should we do something?
        if (canNotDo(event.getEntity())) return;
        final EntityPlayer player = event.getEntityPlayer();
        final InventoryPlayer playerInventory = saveInventory.get(player);

        try {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("Inventory", playerInventory.writeToNBT(new NBTTagList()));
            compound.setInteger("SelectedItemSlot", playerInventory.currentItem);

            CompressedStreamTools.safeWrite(compound, fileStorage.getPlayerSaveFile(event));

            logger.info("Saved {} item stacks for {}.", SaveInventory.count(playerInventory), player.getName());
        } catch (Exception e) {
            logger.error("Could not save inventory for {}. {}", player.getName(), e.getMessage());
            logger.catching(e);
        }
    }

    //TODO: test if temp inventory is restored for other players (p1 dies, p2 dies, p1 respawns, p2 respawns)
    //TODO: place deathchest
    //TODO: lock deathchest
    //TODO: make deathchest indestructable
    // TODO: store xp
}
