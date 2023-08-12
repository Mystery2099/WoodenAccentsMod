package com.mystery2099.wooden_accents_mod.screen.slot

import com.mystery2099.wooden_accents_mod.data.ModItemTags
import com.mystery2099.wooden_accents_mod.data.ModItemTags.contains
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class CrateSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
    override fun canInsert(stack: ItemStack): Boolean = stack.item.canBeNested() && stack !in ModItemTags.uncrateable
}