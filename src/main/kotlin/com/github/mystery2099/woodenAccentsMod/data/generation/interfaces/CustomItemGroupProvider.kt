package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import net.minecraft.item.ItemStack

/**
 * Represents a provider of a custom item group for organizing related items.
 */
interface CustomItemGroupProvider {
    /**
     * Represents a custom item group for organizing related items.
     */
    val itemGroup: CustomItemGroup
    /**
     * Indicates whether the variant item group stack is empty or not.
     *
     * @return `true` if the variant item group stack is not empty, `false` otherwise.
     */
    val hasVariantItemGroupStack: Boolean
        get() = variantItemGroupStack != ItemStack.EMPTY
    /**
     * Represents the variant item group stack.
     *
     * This property represents the variant item group stack. It is a read-only property
     * that returns an [ItemStack] object. By default, it returns an empty [ItemStack].
     *
     * @return The variant item group stack.
     */
    val variantItemGroupStack: ItemStack
        get() = ItemStack.EMPTY
}