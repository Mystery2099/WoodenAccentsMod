package com.github.mystery2099.woodenAccentsMod.block.entity.custom

import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

/**
 * Stores the three displayed stacks of a bracket shelf. There is no screen
 * handler: items are swapped directly against the player's main hand.
 */
class BracketShelfBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.bracketShelf, pos, state), SidedInventory {

    private var inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(SLOT_COUNT, ItemStack.EMPTY)

    override fun size(): Int = inventory.size

    override fun isEmpty(): Boolean = inventory.all { it.isEmpty }

    override fun getStack(slot: Int): ItemStack = inventory[slot]

    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(inventory, slot).also {
        if (!it.isEmpty) markDirty()
    }

    override fun removeStack(slot: Int, count: Int): ItemStack = Inventories.splitStack(inventory, slot, count).also {
        if (!it.isEmpty) markDirty()
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        inventory[slot] = stack
        markDirty()
    }

    override fun clear() {
        inventory.clear()
        markDirty()
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean =
        world?.getBlockEntity(pos) === this && player.squaredDistanceTo(
            pos.x + 0.5,
            pos.y + 0.5,
            pos.z + 0.5
        ) <= 64.0

    override fun getAvailableSlots(side: Direction): IntArray = when (side) {
        Direction.UP, Direction.DOWN -> availableSlots
        else -> noSlots
    }

    override fun canInsert(slot: Int, stack: ItemStack, direction: Direction?): Boolean = direction == Direction.UP

    override fun canExtract(slot: Int, stack: ItemStack, direction: Direction?): Boolean = direction == Direction.DOWN

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        inventory = DefaultedList.ofSize(SLOT_COUNT, ItemStack.EMPTY)
        Inventories.readNbt(nbt, inventory)
    }

    override fun writeNbt(nbt: NbtCompound?) {
        super.writeNbt(nbt)
        // Keep an empty Items list so update packets can clear the client-side inventory.
        Inventories.writeNbt(nbt, inventory, true)
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

    override fun markDirty() {
        super.markDirty()
        world?.updateListeners(pos, cachedState, cachedState, Block.NOTIFY_LISTENERS)
        world?.updateComparators(pos, cachedState.block)
    }

    companion object {
        const val SLOT_COUNT = 3
        private val availableSlots = intArrayOf(0, 1, 2)
        private val noSlots = intArrayOf()
    }
}
