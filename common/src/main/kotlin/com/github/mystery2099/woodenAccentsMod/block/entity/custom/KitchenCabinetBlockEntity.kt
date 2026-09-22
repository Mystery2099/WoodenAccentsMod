package com.github.mystery2099.woodenAccentsMod.block.entity.custom

import com.github.mystery2099.woodenAccentsMod.block.custom.KitchenCabinetBlock
import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity
import net.minecraft.world.level.block.entity.ContainerOpenersCounter
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
import net.minecraft.world.level.Level


/** A 27-slot cabinet with vanilla loot-table and viewer-count behavior. */
class KitchenCabinetBlockEntity(blockPos: BlockPos, state: BlockState) :
    RandomizableContainerBlockEntity(ModBlockEntities.kitchenCabinet, blockPos, state) {

        private var inventory: NonNullList<ItemStack> = NonNullList.withSize(27, ItemStack.EMPTY)

        private val openersCounter: ContainerOpenersCounter = object : ContainerOpenersCounter() {
        override fun onOpen(level: Level, blockPos: BlockPos, state: BlockState) {
            playSound(state, SoundEvents.BARREL_OPEN)
            setOpen(state, true)
        }

        override fun onClose(level: Level, blockPos: BlockPos, state: BlockState) {
            playSound(state, SoundEvents.BARREL_CLOSE)
            setOpen(state, false)
        }

        override fun openerCountChanged(
            level: Level,
            blockPos: BlockPos,
            state: BlockState,
            oldViewerCount: Int,
            newViewerCount: Int
        ) {
        }

        override fun isOwnContainer(player: Player): Boolean {
            val screenHandler = player.containerMenu
            if (screenHandler is ChestMenu) {
                return screenHandler.container === this@KitchenCabinetBlockEntity
            }
            return false
        }
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

    override fun getContainerSize(): Int = 27

    override fun getItems(): NonNullList<ItemStack> = inventory

    override fun setItems(list: NonNullList<ItemStack>) {
        inventory = list
    }

    override fun getDefaultName(): Component = Component.translatable(blockState.block.descriptionId)

    override fun createMenu(syncId: Int, playerInventory: Inventory): AbstractContainerMenu {
        return ChestMenu.threeRows(syncId, playerInventory, this)
    }

    override fun startOpen(player: Player) {
        if (!this.isRemoved && !player.isSpectator) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.blockState)
        }
    }

    override fun stopOpen(player: Player) {
        if (!this.isRemoved && !player.isSpectator) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.blockState)
        }
    }


    fun tick() {
        if (!this.isRemoved) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.blockState)
        }
    }

    fun setOpen(state: BlockState, open: Boolean) {
        this.level?.setBlock(this.getBlockPos(), state.setValue(KitchenCabinetBlock.open, open), Block.UPDATE_ALL)
    }

    fun playSound(state: BlockState, soundEvent: SoundEvent) {
        val (d, e, f) = getSoundLocation(state)
        with(level!!) {
            playSound(
                null, d, e, f, soundEvent,
                SoundSource.BLOCKS, 0.5f,
                random.nextFloat() * 0.1f + 0.9f
            )
        }
    }

    private fun getSoundLocation(state: BlockState): Triple<Double, Double, Double> {
        val vec3i = state.getValue(KitchenCabinetBlock.facing).normal
        val d = this.blockPos.x.toDouble() + 0.5 + vec3i.x.toDouble() / 2.0
        val e = this.blockPos.y.toDouble() + 0.5 + vec3i.y.toDouble() / 2.0
        val f = this.blockPos.z.toDouble() + 0.5 + vec3i.z.toDouble() / 2.0
        return Triple(d, e, f)
    }
}
