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

/**
 * Represents a custom item group for organizing related items.
 *
 * @property name The name of the custom item group.
 * @property itemGroup The associated [ItemGroup] for this custom item group.
 * @property entries A list of item stack entries for this item group.
 */
data class CustomItemGroup(val name: String) {
    init {
        mutableInstances += this
    }
    /**
     * Represents an item group for a set of related items.
     *
     * @property itemGroup The associated ItemGroup for this item group.
     */
    val itemGroup: ItemGroup = FabricItemGroup.builder(name.toIdentifier()).apply {
        icon { getEntries()[0] }
    }.build()

    /**
     * Retrieves a list of [ItemStack] entries for the CustomItemGroup.
     *
     * This method retrieves a list of [ItemStack] entries for the CustomItemGroup. It first calls the
     * getBlocksWithMatchingItemGroup() function to obtain a list of blocks that have a matching itemGroup.
     * Then, it calls the getStackListWithVariants() function to extract the itemStack entries from
     * the list of CustomItemGroupProvider objects. Finally, it calls the [addDefaultStackIfEmpty] function
     * to add a default DIRT item stack to the list if it is empty.
     *
     * @return The list of [ItemStack] entries for the CustomItemGroup.
     */
    internal fun getEntries(): List<ItemStack> {
        val matchingItems = getBlocksWithMatchingItemGroup()
        val stacksList = getStackListWithVariants(matchingItems)

        return addDefaultStackIfEmpty(stacksList)
    }

    /**
     * Retrieves a list of [ItemStack] entries for a specific CustomItemGroup that have variants.
     *
     * This method takes a list of [CustomItemGroupProvider] objects and extracts the [ItemStack] entries from them.
     * For each provider in the list, the method checks if it has a variant item stack. If it does, the variant
     * item stack is added to the list. The method also adds the default item stack for each provider that
     * implements the [Block] interface. Finally, the method rearranges the list by moving the variant item stacks
     * next to their corresponding default item stacks, where the order is determined by the order of appearance in the
     * original list.
     *
     * @param matchingItems The list of [CustomItemGroupProvider] objects to extract the [ItemStack] entries from.
     * @return The list of [ItemStack] entries for the CustomItemGroup with variants.
     */
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
                //(it.item as CustomBlockItem).tagFromProvider == (alt.item as CustomBlockItem).tagFromProvider
            }
            list.indexOf(element).let {
                if (it > -1) list.add(it + 1, alt)
            }
        }
        return list
    }

    /**
     * Adds a default [ItemStack] for the DIRT item to the list if it is empty.
     *
     * @param list The list of [ItemStack] to be checked and modified.
     * @return The modified list of [ItemStack] with the default DIRT item added if it was empty.
     */
    private fun addDefaultStackIfEmpty(list: MutableList<ItemStack>): List<ItemStack> {
        return list.ifEmpty { list + Items.DIRT.defaultStack }
    }

    /**
     * Retrieves a list of blocks with a matching item group.
     *
     * This method filters the blocks in the `ModBlocks` registry that implement the `CustomItemGroupProvider` interface
     * and have a matching itemGroup. The filtered blocks are then returned as a list.
     *
     * @return The list of blocks with a matching item group.
     */
    private fun getBlocksWithMatchingItemGroup() = ModBlocks.blocks.filterIsInstance<CustomItemGroupProvider>()
        .filter { it.itemGroup == this }




    /**
     * Retrieves the item group associated with the CustomItemGroupProvider.
     *
     * @return The CustomItemGroup associated with the provider.
     */
    fun get() = itemGroup


    infix operator fun contains(stack: ItemStack) = itemGroup.contains(stack)

    companion object {
        val instances: List<CustomItemGroup>
            get() = mutableInstances

        private val mutableInstances: MutableList<CustomItemGroup> = mutableListOf()
    }
}