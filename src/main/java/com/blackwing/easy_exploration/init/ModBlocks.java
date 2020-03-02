package com.blackwing.easy_exploration.init;

import com.blackwing.easy_exploration.block.DeathChestBlock;
import com.blackwing.easy_exploration.EasyExploration;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Holds a list of all our {@link Block}s.
 * Suppliers that create Blocks are added to the DeferredRegister.
 * The DeferredRegister is then added to our mod event bus in our constructor.
 * When the Block Registry Event is fired by Forge and it is time for the mod to
 * register its Blocks, our Blocks are created and registered by the DeferredRegister.
 * The Block Registry Event will always be called before the Item registry is filled.
 * Note: This supports registry overrides.
 */
public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, EasyExploration.MODID);

    // This block has the ROCK material, meaning it needs at least a wooden pickaxe to break it. It is very similar to Iron Ore
    // This block has the IRON material, meaning it needs at least a stone pickaxe to break it. It is very similar to the Iron Block
    // This block has the MISCELLANEOUS material. It is very similar to the Redstone Torch
    // This block has the ROCK material, meaning it needs at least a wooden pickaxe to break it. It is very similar to the Furnace
    public static final RegistryObject<Block> DEATH_CHEST = BLOCKS.register("death_chest", () ->
            new DeathChestBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13)));
}
