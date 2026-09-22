package com.github.mystery2099.woodenAccentsMod.screen

import com.github.mystery2099.woodenAccentsMod.screen.slot.CrateSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot

class CrateScreenHandler @JvmOverloads constructor(
    syncId: Int,
    playerInventory: Inventory,
    private val inventory: Container = SimpleContainer(9)
) : AbstractContainerMenu(MenuType.GENERIC_3x3, syncId) {

    init {
        checkContainerSize(inventory, 9)
        inventory.startOpen(playerInventory.player)
        for (i in 0 until 3) for (j in 0 until 3) {
            addSlot(CrateSlot(inventory, j + i * 3, 62 + j * 18, 17 + i * 18))
        }
        for (i in 0 until 3) for (j in 0 until 9) {
            addSlot(Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
        }
        for (i in 0 until 9) {
            addSlot(Slot(playerInventory, i, 8 + i * 18, 142))
        }
    }

    override fun stillValid(player: Player): Boolean = inventory.stillValid(player)
    override fun canTakeItemForPickAll(stack: ItemStack, slot: Slot): Boolean = slot.mayPlace(stack)
    override fun quickMoveStack(player: Player, slot: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot2 = this.slots[slot]
        if (slot2.hasItem()) {
            val itemStack2 = slot2.item
            itemStack = itemStack2.copy()
            if (!when {
                    slot < this.inventory.containerSize -> moveItemStackTo(itemStack2, this.inventory.containerSize, this.slots.size, true)
                    else -> moveItemStackTo(itemStack2, 0, this.inventory.containerSize, false)
                }
            ) return ItemStack.EMPTY
            if (itemStack2.isEmpty) slot2.set(ItemStack.EMPTY)
            else slot2.setChanged()
        }
        return itemStack
    }

    override fun removed(player: Player) {
        super.removed(player)
        inventory.stopOpen(player)
    }
}
