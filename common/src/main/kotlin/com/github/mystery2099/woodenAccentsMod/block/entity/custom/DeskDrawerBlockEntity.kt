package com.github.mystery2099.woodenAccentsMod.block.entity.custom

import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.sounds.SoundSource
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.network.chat.Component
import net.minecraft.core.NonNullList
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.level.gameevent.GameEvent

/** A 27-slot desk inventory stored with vanilla container NBT. */
class DeskDrawerBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    RandomizableContainerBlockEntity(ModBlockEntities.deskDrawer, blockPos, blockState) {
    private var inventory = NonNullList.withSize(27, ItemStack.EMPTY)
    private var viewerCount = 0
    override fun getContainerSize(): Int = inventory.size

    override fun getDefaultName(): Component = Component.translatable(blockState.block.descriptionId)

    override fun createMenu(syncId: Int, playerInventory: Inventory): AbstractContainerMenu {
        return ChestMenu.threeRows(syncId, playerInventory, this)
    }

    override fun getItems(): NonNullList<ItemStack> = inventory

    override fun setItems(list: NonNullList<ItemStack>) {
        inventory = list
    }

    override fun triggerEvent(type: Int, data: Int): Boolean {
        if (type == 1) {
            viewerCount = data
            return true
        }
        return super.triggerEvent(type, data)
    }

    override fun saveAdditional(nbt: CompoundTag) {
        super.saveAdditional(nbt)
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, inventory)
        }
    }

    override fun load(nbt: CompoundTag) {
        super.load(nbt)
        inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY)
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, inventory)
        }
    }

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

    private fun emitGameEventAtPos(player: Player, viewerCount: Int, gameEvent: Holder<GameEvent>) {
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
}
