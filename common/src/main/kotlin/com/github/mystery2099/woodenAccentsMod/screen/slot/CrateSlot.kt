package com.github.mystery2099.woodenAccentsMod.screen.slot

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.inventory.Slot

/** Prevents nested containers according to the item's own nesting rules. */
class CrateSlot(inventory: Container, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
    override fun mayPlace(stack: ItemStack): Boolean = stack.item.canFitInsideContainerItems()
}
