package com.mystery2099.wooden_accents_mod.block.custom.interfaces

import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

interface CustomItemGroupProvider {
    val itemGroup: CustomItemGroup
    val hasNbtVariant: Boolean
        get() = variantWithNbt != Items.AIR.defaultStack
    val variantWithNbt: ItemStack
        get() = Items.AIR.defaultStack
}