package com.techshroom.mods.tbm.item;

import static com.techshroom.mods.tbm.Tutils.addressMod;

import com.techshroom.mods.tbm.TBMMod;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBase extends Item {

    public ItemBase(String name) {
        setUnlocalizedName(name);
        GameRegistry.registerItem(this, name);
        if (TBMMod.proxy().isClient()) {
            ModelLoader.setCustomModelResourceLocation(this, 0,
                    new ModelResourceLocation(addressMod(name), "inventory"));
        }
        addSubtypesToVariants();
    }

    protected void addSubtypesToVariants() {
    }

    protected void addVariants(int... meta) {
        if (!this.hasSubtypes) {
            throw new IllegalStateException(
                    "there's no metadata in your item!");
        }
        String[] var = new String[meta.length];
        for (int i = 0; i < meta.length; i++) {
            String name = getMetaUnlocalizedName(meta[i]).replace("item.", "");
            var[i] = addressMod(name);
            ModelLoader.setCustomModelResourceLocation(this, i,
                    new ModelResourceLocation(addressMod(name), "inventory"));
        }
        ModelBakery.addVariantName(this, var);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (this.hasSubtypes) {
            return getMetaUnlocalizedName(stack.getItemDamage());
        }
        return getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName() {
        if (this.hasSubtypes) {
            return getMetaUnlocalizedName(0);
        }
        return super.getUnlocalizedName();
    }

    public String getMetaUnlocalizedName(int meta) {
        return super.getUnlocalizedName() + "[" + meta + "]";
    }

}
