package com.github.mystery2099.woodenAccentsMod.block.entity.custom

import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags.contains
import com.github.mystery2099.woodenAccentsMod.screen.CrateScreenHandler
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.sounds.SoundSource
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.network.chat.Component
import net.minecraft.core.NonNullList
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.gameevent.GameEvent
import java.util.stream.IntStream

/** A nine-slot container that rejects items tagged as unnestable. */
class CrateBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    RandomizableContainerBlockEntity(ModBlockEntities.crate, blockPos, blockState), WorldlyContainer {
    private var inventory = NonNullList.withSize(9, ItemStack.EMPTY)
    private var viewerCount = 0
    override fun getContainerSize(): Int = inventory.size


    private fun playSoundAtPos(soundEvent: SoundEvent) {
        level?.let {level ->
            level.playSound(
                null,
                blockPos,
                soundEvent,
                SoundSource.BLOCKS,
                0.5f,
                level.random.nextFloat() * 0.1f + 0.9f
            )
        }
    }

    private fun emitGameEventAtPos(player: Player, viewerCount: Int, gameEvent: GameEvent) {
        level?.let { level ->
            level.blockEvent(blockPos, blockState.block, 1, viewerCount)
            if ((gameEvent == GameEvent.CONTAINER_OPEN && viewerCount == 1) ||
                (gameEvent == GameEvent.CONTAINER_CLOSE && viewerCount == 0)) {
                level.gameEvent(player as Entity, gameEvent, blockPos)
            }
        }
    }

    override fun startOpen(player: Player) {
        if (!isRemoved && !player.isSpectator) {
            if (viewerCount < 0) {
                viewerCount = 0
            }
            ++viewerCount
            emitGameEventAtPos(player, viewerCount, GameEvent.CONTAINER_OPEN)
            playSoundAtPos(SoundEvents.BARREL_OPEN)
        }
    }

    override fun stopOpen(player: Player) {
        if (!isRemoved && !player.isSpectator) {
            if (viewerCount == 0) return
            --viewerCount
            emitGameEventAtPos(player, viewerCount, GameEvent.CONTAINER_CLOSE)
            playSoundAtPos(SoundEvents.BARREL_CLOSE)
        }
    }

    override fun getSlotsForFace(side: Direction): IntArray = availableSlots
    override fun triggerEvent(type: Int, data: Int): Boolean {
        if (type == 1) {
            viewerCount = data
            return true
        }
        return super.triggerEvent(type, data)
    }

    override fun getDefaultName(): Component = Component.translatable(blockState.block.descriptionId)
    override fun load(nbt: CompoundTag) {
        super.load(nbt)
        readInventoryNbt(nbt)
    }

    override fun saveAdditional(nbt: CompoundTag) {
        super.saveAdditional(nbt)
        if (!trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, inventory, false)
        }
    }

    private fun readInventoryNbt(nbt: CompoundTag) {
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY)
        if (!tryLoadLootTable(nbt) && nbt.contains(ITEMS_KEY, Tag.TAG_LIST.toInt())) {
            ContainerHelper.loadAllItems(nbt, inventory)
        }
    }

    override fun getItems(): NonNullList<ItemStack> = inventory

    override fun setItems(list: NonNullList<ItemStack>) {
        inventory = list
    }


    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, dir: Direction?): Boolean = stack !in ModItemTags.unnestable

    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, dir: Direction): Boolean = true

    override fun createMenu(syncId: Int, playerInventory: Inventory): AbstractContainerMenu {
        return CrateScreenHandler(syncId, playerInventory, this)
    }

    companion object {
        const val ITEMS_KEY = "Items"
        private val availableSlots = IntStream.range(0, 9).toArray()
    }
}
