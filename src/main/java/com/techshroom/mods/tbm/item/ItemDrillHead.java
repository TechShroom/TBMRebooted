package com.techshroom.mods.tbm.item;

import static com.techshroom.mods.tbm.TBMMod.store;
import static com.techshroom.mods.tbm.Tutils.cast;
import static com.techshroom.mods.tbm.Tutils.indexEqualsIndexArray;

import java.util.List;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.render.DynamicColorMetaManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDrillHead extends ItemBase {

    public ItemDrillHead() {
        super("drillhead");
        setCreativeTab(store.get(TBMKeys.ITEM_TAB).get());
    }

    @Override
    protected void addSubtypesToVariants() {
        setHasSubtypes(true);
        addVariants(
                indexEqualsIndexArray(DynamicColorMetaManager.getColorCount()));
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_,
            @SuppressWarnings("rawtypes") List p_150895_3_) {
        List<ItemStack> list = cast(p_150895_3_);
        for (int i = 0; i < DynamicColorMetaManager.getColorCount(); i++) {
            list.add(new ItemStack(p_150895_1_, 1, i));
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return DynamicColorMetaManager.getColor(stack.getItemDamage());
    }

}
