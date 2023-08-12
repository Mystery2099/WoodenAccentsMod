package com.mystery2099.wooden_accents_mod.screen

import com.mystery2099.wooden_accents_mod.screen.slot.CrateSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot


class CrateScreenHandler @JvmOverloads constructor(
    syncId: Int,
    playerInventory: PlayerInventory,
    private val inventory: Inventory = SimpleInventory(9)
) : ScreenHandler(ScreenHandlerType.GENERIC_3X3, syncId) {

    init {
        checkSize(inventory, 9)
        inventory.onOpen(playerInventory.player)
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

    override fun canUse(player: PlayerEntity): Boolean = inventory.canPlayerUse(player)
    override fun canInsertIntoSlot(stack: ItemStack, slot: Slot): Boolean = slot.canInsert(stack)
    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot2 = this.slots[slot]
        if (slot2.hasStack()) {
            val itemStack2 = slot2.stack
            itemStack = itemStack2.copy()
            if (!when {
                slot < this.inventory.size() -> insertItem(itemStack2, this.inventory.size(), this.slots.size, true)
                    else -> insertItem(itemStack2, 0, this.inventory.size(), false)
            }) return ItemStack.EMPTY
            if(itemStack2.isEmpty) slot2.stack = ItemStack.EMPTY
            else slot2.markDirty()
        }
        return itemStack
    }

    override fun onClosed(player: PlayerEntity) {
        super.onClosed(player)
        inventory.onClose(player)
    }
}


