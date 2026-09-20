package com.github.mystery2099.woodenAccentsMod.block.entity.custom

import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.core.NonNullList
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

/**
 * Stores the three displayed stacks of a bracket shelf. There is no screen
 * handler: items are swapped directly against the player's main hand.
 */
class BracketShelfBlockEntity(blockPos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntities.bracketShelf, blockPos, state), WorldlyContainer {

    private var inventory: NonNullList<ItemStack> = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY)

    override fun getContainerSize(): Int = inventory.size

    override fun isEmpty(): Boolean = inventory.all { it.isEmpty }

    override fun getItem(slot: Int): ItemStack = inventory[slot]

    override fun removeItemNoUpdate(slot: Int): ItemStack = ContainerHelper.takeItem(inventory, slot).also {
        if (!it.isEmpty) setChanged()
    }

    override fun removeItem(slot: Int, count: Int): ItemStack = ContainerHelper.removeItem(inventory, slot, count).also {
        if (!it.isEmpty) setChanged()
    }

    override fun setItem(slot: Int, stack: ItemStack) {
        inventory[slot] = stack
        setChanged()
    }

    override fun clearContent() {
        inventory.clear()
        setChanged()
    }

    override fun stillValid(player: Player): Boolean =
        level?.getBlockEntity(blockPos) === this && player.distanceToSqr(
            blockPos.x + 0.5,
            blockPos.y + 0.5,
            blockPos.z + 0.5
        ) <= 64.0

    override fun getSlotsForFace(side: Direction): IntArray = when (side) {
        Direction.UP, Direction.DOWN -> availableSlots
        else -> noSlots
    }

    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, direction: Direction?): Boolean = direction == Direction.UP

    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, direction: Direction?): Boolean = direction == Direction.DOWN

    override fun load(nbt: CompoundTag) {
        super.load(nbt)
        inventory = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY)
        ContainerHelper.loadAllItems(nbt, inventory)
    }

    override fun saveAdditional(nbt: CompoundTag?) {
        super.saveAdditional(nbt)
        // Keep an empty Items list so update packets can clear the client-side inventory.
        ContainerHelper.saveAllItems(nbt, inventory, true)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(): CompoundTag = saveWithoutMetadata()

    override fun setChanged() {
        super.setChanged()
        level?.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS)
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    companion object {
        const val SLOT_COUNT = 3
        private val availableSlots = intArrayOf(0, 1, 2)
        private val noSlots = intArrayOf()
    }
}
