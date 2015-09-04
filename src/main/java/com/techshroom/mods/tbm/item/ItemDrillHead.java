package com.techshroom.mods.tbm.item;

import static com.techshroom.mods.tbm.TBMMod.store_get;
import static com.techshroom.mods.tbm.Tutils.cast;
import static com.techshroom.mods.tbm.Tutils.indexEqualsIndexArray;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDrillHead extends ItemBase {

    public static final int DIAMOND_META, IRON_META, WOOD_META, COUNT_META;

    static {
        int metaCounter = 0;
        WOOD_META = metaCounter++;
        IRON_META = metaCounter++;
        DIAMOND_META = metaCounter++;
        COUNT_META = metaCounter++;
    }

    public ItemDrillHead() {
        super("drillhead");
        this.setHasSubtypes(true)
                .setCreativeTab((CreativeTabs) store_get("itemTab"));
    }
    
    @Override
    protected void addSubtypesToVariants() {
        addVariants(indexEqualsIndexArray(COUNT_META));
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_,
            @SuppressWarnings("rawtypes") List p_150895_3_) {
        List<ItemStack> list = cast(p_150895_3_);
        for (int i = 0; i < COUNT_META; i++) {
            list.add(new ItemStack(p_150895_1_, 1, i));
        }
    }

}
