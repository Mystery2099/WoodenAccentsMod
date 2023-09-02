package com.mystery2099.wooden_accents_mod.item_group

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

class CustomItemGroup(name: String) {
    val itemGroup: ItemGroup
    inline val entries: List<ItemStack>
        get() = run {
            val list = mutableListOf<ItemStack>()
            ModBlocks.blocks.forEach { block ->
                if (block is CustomItemGroupProvider) {
                    if (block.itemGroup == this) {
                        list += block.asItem().defaultStack
                        if (block.hasNbtVariant) {
                            list += block.variantWithNbt
                        }
                    }
                }

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