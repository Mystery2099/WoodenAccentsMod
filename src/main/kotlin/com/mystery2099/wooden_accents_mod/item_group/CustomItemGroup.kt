package com.mystery2099.wooden_accents_mod.item_group

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.defaultItemStack
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.item.CustomBlockItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.block.Block
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
class CustomItemGroup(name: String) {
    val itemGroup: ItemGroup
    inline val entries: List<ItemStack>
        get() {
            val list = mutableListOf<ItemStack>()
            val altList = mutableListOf<ItemStack>()

            ModBlocks.blocks.filterIsInstance<CustomItemGroupProvider>()
                .filter { it.itemGroup == this }
                .forEach {
                    list += (it as? Block)?.defaultItemStack ?: ItemStack.EMPTY
                    if (it.hasVariantItemGroupStack) {
                        altList += it.variantItemGroupStack
                    }
                }

            altList.forEach { alt ->
                val element = list.lastOrNull {
                    (it.item as CustomBlockItem).tagFromProvider == (alt.item as CustomBlockItem).tagFromProvider
                }
                list.indexOf(element).let {
                    if (it > -1) list.add(it + 1, alt)
                }
            }

            return list.ifEmpty { list + Items.DIRT.defaultStack }
        }

    init {
        itemGroup = FabricItemGroup.builder(name.toIdentifier()).apply {
            icon { entries[0] }
        }.build()
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