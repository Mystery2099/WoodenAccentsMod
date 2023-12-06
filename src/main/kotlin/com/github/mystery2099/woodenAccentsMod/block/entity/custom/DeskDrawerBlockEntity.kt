package com.github.mystery2099.woodenAccentsMod.block.entity.custom

import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.event.GameEvent

/**
 * Desk drawer block entity
 *
 * @constructor
 *
 * @param blockPos
 * @param blockState
 */
class DeskDrawerBlockEntity(blockPos: BlockPos, blockState: BlockState) :
    LootableContainerBlockEntity(ModBlockEntities.deskDrawer, blockPos, blockState) {
    private var inventory = DefaultedList.ofSize(27, ItemStack.EMPTY)
    private var viewerCount = 0
    override fun size(): Int = inventory.size

    override fun getContainerName(): Text = Text.translatable(cachedState.block.translationKey)

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory?): ScreenHandler? {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this)
    }

    override fun getInvStackList(): DefaultedList<ItemStack> = inventory

    override fun setInvStackList(list: DefaultedList<ItemStack>) {
        inventory = list
    }

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        if (type == 1) {
            viewerCount = data
            return true
        }
        return super.onSyncedBlockEvent(type, data)
    }

    override fun writeNbt(nbt: NbtCompound?) {
        super.writeNbt(nbt)
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, inventory)
        }
    }

    override fun readNbt(nbt: NbtCompound?) {
        super.readNbt(nbt)
        inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY)
        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, inventory)
        }
    }

    private fun playSoundAtPos(soundEvent: SoundEvent) {
        world?.let {world ->
            world.playSound(
                null,
                pos,
                soundEvent,
                SoundCategory.BLOCKS,
                0.5f,
                world.random.nextFloat() * 0.1f + 0.9f
            )
        }
    }

    private fun emitGameEventAtPos(player: PlayerEntity, viewerCount: Int, gameEvent: GameEvent) {
        world?.let { world ->
            world.addSyncedBlockEvent(pos, cachedState.block, 1, viewerCount)
            if (viewerCount == 1) {
                world.emitGameEvent(player as Entity, gameEvent, pos)
            }
        }
    }

    override fun onOpen(player: PlayerEntity) {
        if (!removed && !player.isSpectator) {
            if (viewerCount < 0) {
                viewerCount = 0
            }
            ++viewerCount
            emitGameEventAtPos(player, viewerCount, GameEvent.CONTAINER_OPEN)
            playSoundAtPos(SoundEvents.BLOCK_BARREL_OPEN)
        }
    }

    override fun onClose(player: PlayerEntity) {
        if (!removed && !player.isSpectator) {
            --viewerCount
            emitGameEventAtPos(player, viewerCount, GameEvent.CONTAINER_OPEN)
            playSoundAtPos(SoundEvents.BLOCK_BARREL_CLOSE)
        }
    }
}