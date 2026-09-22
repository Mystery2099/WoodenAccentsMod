package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.defaultItemStack
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

/** Computes the display order of block items for a creative tab. */
object ItemGroupContent {
    /** Registration order keeps finishes in vanilla wood order within each family. */
    fun getEntries(group: ModItemGroup): List<ItemStack> {
        val matchingItems = getBlocksWithMatchingItemGroup(group)
        val stacksList = getStackListWithVariants(matchingItems)

        return addDefaultStackIfEmpty(stacksList)
    }

    private fun getStackListWithVariants(matchingItems: List<CustomItemGroupProvider>): MutableList<ItemStack> {
        val stacks = mutableListOf<ItemStack>()
        for (family in matchingItems.groupBy { it.javaClass }.values) {
            for (entry in family) {
                if (entry is net.minecraft.world.level.block.Block) stacks += entry.defaultItemStack
            }
            for (entry in family) {
                if (entry.hasVariantItemGroupStack) stacks += entry.variantItemGroupStack
            }
        }
        return stacks
    }

    /** The group icon reads entry zero, so empty groups need a harmless fallback. */
    private fun addDefaultStackIfEmpty(list: MutableList<ItemStack>): List<ItemStack> {
        return list.ifEmpty { list + net.minecraft.world.item.Items.DIRT.defaultInstance }
    }

    private fun getBlocksWithMatchingItemGroup(group: ModItemGroup) =
        ModBlocks.blocks.filterIsInstance<CustomItemGroupProvider>()
            .filter { it.itemGroup == group }
}
