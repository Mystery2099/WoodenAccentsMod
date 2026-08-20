package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.defaultItemStack
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

/** A creative tab populated from blocks that opt into this group. */
data class CustomItemGroup(val name: String) {
    init {
        mutableInstances += this
    }
    val itemGroup: ItemGroup = FabricItemGroup.builder(name.toIdentifier()).apply {
        icon { getEntries()[0] }
    }.build()

    internal fun getEntries(): List<ItemStack> {
        val matchingItems = getBlocksWithMatchingItemGroup()
        val stacksList = getStackListWithVariants(matchingItems)

        return addDefaultStackIfEmpty(stacksList)
    }

    /** Keeps each variant directly after the last default stack backed by the same block class. */
    private fun getStackListWithVariants(matchingItems: List<CustomItemGroupProvider>): MutableList<ItemStack> {
        val list = mutableListOf<ItemStack>()
        val altList = mutableListOf<ItemStack>()

        matchingItems.forEach {
            list += (it as? Block)?.defaultItemStack ?: ItemStack.EMPTY
            if (it.hasVariantItemGroupStack) {
                altList += it.variantItemGroupStack
            }
        }

        altList.forEach { alt ->
            val element = list.lastOrNull {
                (it.item as BlockItem).block.javaClass == (alt.item as BlockItem).block.javaClass
            }
            list.indexOf(element).let {
                if (it > -1) list.add(it + 1, alt)
            }
        }
        return list
    }

    /** The group icon reads entry zero, so empty groups need a harmless fallback. */
    private fun addDefaultStackIfEmpty(list: MutableList<ItemStack>): List<ItemStack> {
        return list.ifEmpty { list + Items.DIRT.defaultStack }
    }

    private fun getBlocksWithMatchingItemGroup() = ModBlocks.blocks.filterIsInstance<CustomItemGroupProvider>()
        .filter { it.itemGroup == this }
    fun get() = itemGroup


    infix operator fun contains(stack: ItemStack) = itemGroup.contains(stack)

    companion object {
        val instances: List<CustomItemGroup>
            get() = mutableInstances

        private val mutableInstances: MutableList<CustomItemGroup> = mutableListOf()
    }
}
