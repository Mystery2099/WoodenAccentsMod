package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.isOf
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.Waterloggable
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

/**
 * Waterloggable block with entity
 *
 * @constructor
 *
 * @param settings
 */
abstract class WaterloggableBlockWithEntity(settings: Settings) : BlockWithEntity(settings), Waterloggable {
    init {
        this.defaultState = stateManager.defaultState.with(waterlogged, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(waterlogged)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx)?.with(
            waterlogged,
            ctx.world.getFluidState(ctx.blockPos) isOf Fluids.WATER)
            ?: defaultState
    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState = state.also {
        if (state[AbstractWaterloggableBlock.waterlogged]) {
            world.scheduleFluidTick(
                pos,
                Fluids.WATER,
                Fluids.WATER.getTickRate(world)
            )
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "if (state[waterlogged]) Fluids.WATER.getStill(false) else super.getFluidState(state)",
        "com.mystery2099.wooden_accents_mod.block.custom.WaterloggableBlockWithEntity.Companion.waterlogged",
        "net.minecraft.fluid.Fluids",
        "net.minecraft.block.BlockWithEntity"
    )
    )
    override fun getFluidState(state: BlockState): FluidState {
        return if (state[waterlogged]) Fluids.WATER.getStill(false)
        else Fluids.EMPTY.defaultState
    }
    companion object {
        val waterlogged: BooleanProperty = Properties.WATERLOGGED
    }
}