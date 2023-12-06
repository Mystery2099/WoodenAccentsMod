package com.github.mystery2099.woodenAccentsMod.screen.slot

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

/**
 * Represents a slot in the CrateScreenHandler's inventory.
 *
 * @param inventory The inventory that this slot belongs to.
 * @param index The index of this slot within the inventory.
 * @param x The X-coordinate of the slot's position in the GUI.
 * @param y The Y-coordinate of the slot's position in the GUI.
 */
class CrateSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
    override fun canInsert(stack: ItemStack): Boolean = stack.item.canBeNested()
}