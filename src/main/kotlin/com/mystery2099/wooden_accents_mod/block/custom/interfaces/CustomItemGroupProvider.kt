package com.mystery2099.wooden_accents_mod.block.custom.interfaces

import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import net.minecraft.item.ItemStack

interface CustomItemGroupProvider {
    val itemGroup: CustomItemGroup
    val hasVariantItemGroupStack: Boolean
        get() = variantItemGroupStack != ItemStack.EMPTY
    val variantItemGroupStack: ItemStack
        get() = ItemStack.EMPTY
}