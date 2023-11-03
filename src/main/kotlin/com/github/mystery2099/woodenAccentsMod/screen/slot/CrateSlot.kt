package com.github.mystery2099.woodenAccentsMod.screen.slot

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class CrateSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
    override fun canInsert(stack: ItemStack): Boolean = stack.item.canBeNested()
}