package com.github.mystery2099.woodenAccentsMod.item

import com.github.mystery2099.woodenAccentsMod.block.custom.CrateBlock
import com.github.mystery2099.woodenAccentsMod.block.item
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags.contains
import net.minecraft.core.component.DataComponents
import net.minecraft.world.level.block.Block
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents


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
            // Contents travel in DataComponents.CONTAINER (same as the vanilla shulker).
            ItemUtils.onContainerDestroyed(
                entity,
                entity.item.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems()
            )        }
    }
}
