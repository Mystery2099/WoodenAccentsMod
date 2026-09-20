package com.github.mystery2099.woodenAccentsMod.item

import com.github.mystery2099.woodenAccentsMod.block.custom.CrateBlock
import com.github.mystery2099.woodenAccentsMod.block.item
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags.contains
import net.minecraft.world.level.block.Block
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag


/**
 * Applies the mod's container-item rules to its blocks.
 *
 * Crates cannot contain items tagged as unnestable, and spill their stored contents when their item entity is destroyed.
 */
class CustomBlockItem(block: Block, settings: Item.Properties) : BlockItem(block, settings) {
    override fun canFitInsideContainerItems(): Boolean =
        super.canFitInsideContainerItems() && block.item.defaultInstance !in ModItemTags.unnestable

    override fun onDestroyed(entity: ItemEntity) {
        super.onDestroyed(entity)
        if (block is CrateBlock) {
            getBlockEntityData(entity.item)?.let { nbtCompound ->
                if (nbtCompound.contains("Items", Tag.TAG_LIST.toInt())) {
                    val nbtList = nbtCompound.getList("Items", Tag.TAG_COMPOUND.toInt())
                    ItemUtils.onContainerDestroyed(entity, nbtList.stream()
                        .map { it as CompoundTag }
                        .map(ItemStack::of))
                }
            }
        }
    }
}
