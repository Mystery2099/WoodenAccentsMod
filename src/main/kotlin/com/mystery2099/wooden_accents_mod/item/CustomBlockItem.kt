package com.mystery2099.item;

import com.mystery2099.wooden_accents_mod.block.custom.CrateBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class CustomBlockItem extends BlockItem {
    public CustomBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public boolean canBeNested() {
        return super.canBeNested() || !(this.getBlock() instanceof CrateBlock);
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        super.onItemEntityDestroyed(entity);
        NbtCompound nbtCompound;
        if (this.getBlock() instanceof CrateBlock && (nbtCompound = BlockItem.getBlockEntityNbt(entity.getStack())) != null && nbtCompound.contains("Items", NbtElement.LIST_TYPE)) {
            NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
            ItemUsage.spawnItemContents(entity, nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt));
        }
    }
}
