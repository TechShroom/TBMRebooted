package com.techshroom.mods.tbm.item;

import static com.techshroom.mods.tbm.TBMMod.*;
import static com.techshroom.mods.tbm.Tutils.*;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemDrillHead extends ItemBase {
    public static final int DIAMOND_META, IRON_META, WOOD_META, COUNT_META;
    static {
        int metaCounter = 0;
        WOOD_META = metaCounter++;
        IRON_META = metaCounter++;
        DIAMOND_META = metaCounter++;
        COUNT_META = metaCounter++;
    }

    private final IIcon[] icons = new IIcon[COUNT_META];

    public ItemDrillHead() {
        super("drillhead");
        this.setHasSubtypes(true)
                .setTextureName(
                        address(mod().id(), "drillhead_base-tex").toString())
                .setCreativeTab((CreativeTabs) store_get("itemTab"));
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_,
            @SuppressWarnings("rawtypes") List p_150895_3_) {
        List<ItemStack> list = cast(p_150895_3_);
        for (int i = 0; i < COUNT_META; i++) {
            list.add(new ItemStack(p_150895_1_, 1, i));
        }
    }

    @Override
    public void registerIcons(IIconRegister p_94581_1_) {
        super.registerIcons(p_94581_1_);
        icons[0] = icons[1] = icons[2] = itemIcon;
    }

    @Override
    public IIcon getIconFromDamage(int p_77617_1_) {
        return icons[p_77617_1_];
    }
}
