package com.github.mystery2099.woodenAccentsMod.block.entity.custom

import com.github.mystery2099.woodenAccentsMod.block.custom.KitchenCabinetBlock
import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.entity.ViewerCountManager
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
import net.minecraft.world.World


/**
 * KitchenCabinetBlockEntity represents a block entity for a kitchen cabinet block.
 *
 * @property inventory The inventory of the cabinet block.
 * @property stateManager The manager responsible for tracking container open/close events and viewer counts.
 * @constructor Creates a new instance of KitchenCabinetBlockEntity.
 * @param pos The position of the block entity.
 * @param state The block state of the block entity.
 */
class KitchenCabinetBlockEntity(pos: BlockPos, state: BlockState) :
    LootableContainerBlockEntity(ModBlockEntities.kitchenCabinet, pos, state) {

        private var inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(27, ItemStack.EMPTY)

        private val stateManager: ViewerCountManager = object : ViewerCountManager() {
        override fun onContainerOpen(world: World, pos: BlockPos, state: BlockState) {
            playSound(state, SoundEvents.BLOCK_BARREL_OPEN)
            setOpen(state, true)
        }

        override fun onContainerClose(world: World, pos: BlockPos, state: BlockState) {
            playSound(state, SoundEvents.BLOCK_BARREL_CLOSE)
            setOpen(state, false)
        }

        override fun onViewerCountUpdate(
            world: World,
            pos: BlockPos,
            state: BlockState,
            oldViewerCount: Int,
            newViewerCount: Int
        ) {
        }

        override fun isPlayerViewing(player: PlayerEntity): Boolean {
            val screenHandler = player.currentScreenHandler
            if (screenHandler is GenericContainerScreenHandler) {
                return screenHandler.inventory === this@KitchenCabinetBlockEntity
            }
            return false
        }
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

    override fun size(): Int = 27

    override fun getInvStackList(): DefaultedList<ItemStack> = inventory

    override fun setInvStackList(list: DefaultedList<ItemStack>) {
        inventory = list
    }

    override fun getContainerName(): Text = Text.translatable(cachedState.block.translationKey)

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory?): ScreenHandler? {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this)
    }

    override fun onOpen(player: PlayerEntity) {
        if (!this.removed && !player.isSpectator) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.cachedState)
        }
    }

    override fun onClose(player: PlayerEntity) {
        if (!this.removed && !player.isSpectator) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.cachedState)
        }
    }


    fun tick() {
        if (!this.removed) {
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.cachedState)
        }
    }

    /**
     * Set open
     *
     * @param state
     * @param open
     */
    fun setOpen(state: BlockState, open: Boolean) {
        this.world?.setBlockState(this.getPos(), state.with(KitchenCabinetBlock.open, open), Block.NOTIFY_ALL)
    }

    /**
     * Play sound
     *
     * @param state
     * @param soundEvent
     */
    fun playSound(state: BlockState, soundEvent: SoundEvent?) {
        val (d, e, f) = getSoundLocation(state)
        with(world!!) {
            playSound(
                null, d, e, f, soundEvent,
                SoundCategory.BLOCKS, 0.5f,
                random.nextFloat() * 0.1f + 0.9f
            )
        }
    }

    private fun getSoundLocation(state: BlockState): Triple<Double, Double, Double> {
        val vec3i = state.get(KitchenCabinetBlock.facing).vector
        val d = this.pos.x.toDouble() + 0.5 + vec3i.x.toDouble() / 2.0
        val e = this.pos.y.toDouble() + 0.5 + vec3i.y.toDouble() / 2.0
        val f = this.pos.z.toDouble() + 0.5 + vec3i.z.toDouble() / 2.0
        return Triple(d, e, f)
    }
}

