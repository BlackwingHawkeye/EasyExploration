package com.blackwing.easy_exploration.init;

import com.blackwing.easy_exploration.EasyExploration;
import com.blackwing.easy_exploration.tileEntity.DeathChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Holds a list of all our {@link TileEntityType}s.
 * Suppliers that create TileEntityTypes are added to the DeferredRegister.
 * The DeferredRegister is then added to our mod event bus in our constructor.
 * When the TileEntityType Registry Event is fired by Forge and it is time for the mod to
 * register its TileEntityTypes, our TileEntityTypes are created and registered by the DeferredRegister.
 * The TileEntityType Registry Event will always be called after the Block and Item registries are filled.
 * Note: This supports registry overrides.
 */
public final class ModTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, EasyExploration.MODID);

    // We don't have a datafixer for our TileEntities, so we pass null into build.
    public static final RegistryObject<TileEntityType<DeathChestTileEntity>> DEATH_CHEST =
            TILE_ENTITY_TYPES.register("death_chest", () -> TileEntityType.Builder
                    .create(DeathChestTileEntity::new, ModBlocks.DEATH_CHEST.get())
					.build(null)
            );
}
