package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroup
import net.minecraft.world.item.ItemStack

/** Places a block in its primary item group and, optionally, a variant group. */
interface CustomItemGroupProvider {
    val itemGroup: ModItemGroup

    val hasVariantItemGroupStack: Boolean
        get() = variantItemGroupStack != ItemStack.EMPTY

    val variantItemGroupStack: ItemStack
        get() = ItemStack.EMPTY
}
