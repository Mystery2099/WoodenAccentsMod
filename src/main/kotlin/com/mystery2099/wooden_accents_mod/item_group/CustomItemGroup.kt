package com.mystery2099.wooden_accents_mod.item_group

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.block.defaultItemStack
import com.mystery2099.wooden_accents_mod.item.CustomBlockItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

class CustomItemGroup(name: String) {
    val itemGroup: ItemGroup
    inline val entries: List<ItemStack>
        get() {
            val list = mutableListOf<ItemStack>()
            val altList = mutableListOf<ItemStack>()

            ModBlocks.blocks.filterIsInstance<CustomItemGroupProvider>()
                .filter { it.itemGroup == this }
                .forEach { block ->
                    list += if (block is Block) block.defaultItemStack else ItemStack.EMPTY
                    if (block.hasVariantItemGroupStack) {
                        altList += block.variantItemGroupStack
                    }
                }

            altList.forEach { alt ->
                val element = list.lastOrNull {
                    (it.item as CustomBlockItem).tagFromProvider == (alt.item as CustomBlockItem).tagFromProvider
                }
                val index = list.indexOf(element)
                if (index > -1) list.add(index + 1, alt)
            }

            return list.ifEmpty { listOf(Items.DIRT.defaultStack) }
        }

    init {
        itemGroup = FabricItemGroup.builder(name.toIdentifier()).apply {
            icon { entries[0] }
        }.build()
        mutableInstances += this
    }

    fun get() = itemGroup

    infix operator fun contains(stack: ItemStack) = itemGroup.contains(stack)

    companion object {
        val instances: List<CustomItemGroup>
            get() = mutableInstances

        private val mutableInstances: MutableList<CustomItemGroup> = mutableListOf()
    }
}