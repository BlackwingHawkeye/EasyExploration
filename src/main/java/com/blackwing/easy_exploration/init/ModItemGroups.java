package com.blackwing.easy_exploration.init;

import com.blackwing.easy_exploration.EasyExploration;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.function.Supplier;

public class ModItemGroups {

    /*
     * An ItemGroup (previously called a CreativeTab) contains our mod’s items in the creative menu.
     */
    public static final ItemGroup MOD_ITEM_GROUP =
            new ModItemGroup(EasyExploration.MODID, () -> new ItemStack(Items.LIGHT_BLUE_BANNER));

    public static class ModItemGroup extends ItemGroup {

        private final Supplier<ItemStack> iconSupplier;

        public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier) {
            super(name);
            this.iconSupplier = iconSupplier;
        }

        @Override
        public ItemStack createIcon() {
            return iconSupplier.get();
        }
    }

}
