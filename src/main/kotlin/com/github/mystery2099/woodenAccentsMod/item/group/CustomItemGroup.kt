package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.defaultItemStack
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.block.Block
import net.minecraft.item.*

/**
 * [CustomItemGroup] is a data class representing a custom [ItemGroup] for organizing and displaying [Item]s in the creative menu.
 *
 * @constructor Create a custom [ItemGroup] with the given name.
 * @param name The name of the custom [ItemGroup].
 * @see ModItemGroups
 * @see ItemGroup
 * @see ItemGroups
 */
data class CustomItemGroup(val name: String) {
    // The ItemGroup associated with this custom item group.
    val itemGroup: ItemGroup = FabricItemGroup.builder(name.toIdentifier()).apply {
        icon { entries[0] }
    }.build()

    // Get a list of item stack entries for this item group.
    inline val entries: List<ItemStack>
        get() {
            val list = mutableListOf<ItemStack>()
            val altList = mutableListOf<ItemStack>()

            com.github.mystery2099.woodenAccentsMod.block.ModBlocks.blocks.filterIsInstance<CustomItemGroupProvider>()
                .filter { it.itemGroup == this }
                .forEach {
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

            return list.ifEmpty { list + Items.DIRT.defaultStack }
        }

    init {
        mutableInstances += this
    }

    /**
     * Get the associated [ItemGroup].
     *
     * @return The [ItemGroup] associated with this [CustomItemGroup].
     */
    fun get() = itemGroup

    /**
     * Check if a given [ItemStack] is contained in [itemGroup].
     *
     * @param stack The [ItemStack] to check.
     * @return true if the [ItemStack] is contained, false otherwise.
     */
    infix operator fun contains(stack: ItemStack) = itemGroup.contains(stack)

    companion object {
        val instances: List<CustomItemGroup>
            get() = mutableInstances

        private val mutableInstances: MutableList<CustomItemGroup> = mutableListOf()
    }
}