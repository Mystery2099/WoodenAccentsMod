package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.defaultItemStack
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

/**
 * Custom item group
 *
 * @constructor
 *
 * @param name
 */
data class CustomItemGroup(val name: String) {
    val itemGroup: ItemGroup = FabricItemGroup.builder(name.toIdentifier()).apply {
        icon { entries[0] }
    }.build()
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
     * Get
     *
     */
    fun get() = itemGroup

    /**
     * Contains
     *
     * @param stack
     */
    infix operator fun contains(stack: ItemStack) = itemGroup.contains(stack)

    companion object {
        val instances: List<CustomItemGroup>
            get() = mutableInstances

        private val mutableInstances: MutableList<CustomItemGroup> = mutableListOf()
    }
}