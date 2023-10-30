package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import net.minecraft.item.ItemStack

interface CustomItemGroupProvider {
    val itemGroup: CustomItemGroup
    val hasVariantItemGroupStack: Boolean
        get() = variantItemGroupStack != ItemStack.EMPTY
    val variantItemGroupStack: ItemStack
        get() = ItemStack.EMPTY
}