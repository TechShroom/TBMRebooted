package com.techshroom.mods.tbm.entity;

import static com.techshroom.mods.tbm.TBMMod.store;

import com.techshroom.mods.tbm.TBMKeys;
import com.techshroom.mods.tbm.gui.GuiTBMDrill;
import com.techshroom.mods.tbm.gui.container.ContainerTBMDrill;
import com.techshroom.mods.tbm.render.DynamicDrillheadInfo;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class TBMDrillEntity extends TBMFullGuiEntity<ContainerTBMDrill> {

    private final FakePlayer fakery;

    public TBMDrillEntity(World w) {
        super(w, TBMKeys.GuiId.DRILL, new InventoryBasic("Drill", false, 1));
        this.fakery = (w instanceof WorldServer)
                ? FakePlayerFactory.getMinecraft((WorldServer) w) : null;
        if (this.fakery != null) {
            this.fakery.inventory.currentItem = 0;
            this.fakery.setGameType(GameType.SURVIVAL);
            this.fakery.setPosition(0, Double.POSITIVE_INFINITY, 0);
        }
    }

    @Override
    public Block blockBase() {
        return store.get(TBMKeys.Blocks.DRILL).get();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return super.isItemValidForSlot(index, stack)
                && stack.getItem() == store.get(TBMKeys.Items.DRILLHEAD).get();
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (this.fakery != null) {
            this.fakery.inventory.setInventorySlotContents(0,
                    DynamicDrillheadInfo.lookupByMeta(stack.getItemDamage())
                            .get().getPickaxe().convertToItemStack(3));
        }
        super.setInventorySlotContents(index, stack);
    }

    private void damageTools(int amt) {
        if (this.fakery != null) {
            InventoryPlayer inventory = this.fakery.inventory;
            for (int i = 0; i < amt; i++) {
                if (inventory.getCurrentItem().attemptDamageItem(1,
                        this.rand)) {
                    inventory.decrStackSize(inventory.currentItem, 1);
                }
            }
        }
    }

    @Override
    public Container container(EntityPlayer player) {
        return new ContainerTBMDrill(this, player.inventory);
    }

    @Override
    public GuiScreen guiScreen(ContainerTBMDrill c) {
        return new GuiTBMDrill(c);
    }

    @Override
    public void onMoveToBlock(BlockPos pos) {
        if (doHarvestBlock(pos)) {
            damageTools(1);
        }
    }

    // ItemInWorldManager.tryHarvestBlock except with no sounds/xp
    // (at least, no xp now *wink*)
    private boolean doHarvestBlock(BlockPos pos) {
        int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(
                this.worldObj, GameType.SURVIVAL, this.fakery, pos);
        if (exp == -1) {
            return false;
        } else {
            IBlockState state = this.worldObj.getBlockState(pos);
            TileEntity tile = this.worldObj.getTileEntity(pos);

            ItemStack stack = this.fakery.getCurrentEquippedItem();
            if (stack != null && stack.getItem().onBlockStartBreak(stack, pos,
                    this.fakery))
                return false;

            ItemStack currentItem = this.fakery.getCurrentEquippedItem();
            boolean canHarvest = state.getBlock().canHarvestBlock(this.worldObj,
                    pos, this.fakery);

            if (currentItem != null) {
                currentItem.onBlockDestroyed(this.worldObj, state.getBlock(),
                        pos, this.fakery);

                if (currentItem.stackSize == 0) {
                    this.fakery.destroyCurrentEquippedItem();
                }
            }

            boolean removed = this.removeBlock(pos, canHarvest);
            if (removed && canHarvest) {
                state.getBlock().harvestBlock(this.worldObj, this.fakery, pos,
                        state, tile);
            }

            // Drop experiance
            // if (!this.isCreative() && flag1 && exp > 0) {
            // iblockstate.getBlock().dropXpOnBlockBreak(theWorld, pos, exp);
            // }
            return removed;
        }
    }

    private boolean removeBlock(BlockPos pos, boolean canHarvest) {
        IBlockState state = this.worldObj.getBlockState(pos);
        state.getBlock().onBlockHarvested(this.worldObj, pos, state,
                this.fakery);
        boolean removed = state.getBlock().removedByPlayer(this.worldObj, pos,
                this.fakery, canHarvest);

        if (removed) {
            state.getBlock().onBlockDestroyedByPlayer(this.worldObj, pos,
                    state);
        }

        return removed;
    }

}
