package com.github.mystery2099.woodenAccentsMod.block.custom

import net.minecraft.block.Block
import net.minecraft.block.BlockState
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
 * AbstractWaterloggableBlock is an abstract class that represents a waterloggable block.
 *
 * @param settings The settings for the block.
 */
abstract class AbstractWaterloggableBlock(settings: Settings) : Block(settings), Waterloggable {

    init { defaultState = defaultState.with(waterlogged, false) }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(waterlogged)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return this.defaultState.with(waterlogged, ctx.world.getFluidState(ctx.blockPos).fluid == Fluids.WATER)
    }

    @Deprecated("Deprecated in Java")
	override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState = getStateForNeighborUpdate(state, pos, world)

    fun getStateForNeighborUpdate(state: BlockState, pos: BlockPos, world: WorldAccess): BlockState {
        if (state[waterlogged]) world.scheduleFluidTick(
            pos,
            Fluids.WATER,
            Fluids.WATER.getTickRate(world)
        )
        return state
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "if (state[waterlogged]) Fluids.WATER.getStill(false) else super.getFluidState(state)",
        "com.mystery2099.wooden_accents_mod.block.custom.AbstractWaterloggableBlock.Companion.waterlogged",
        "net.minecraft.fluid.Fluids",
        "net.minecraft.block.Block"
    )
    )
    override fun getFluidState(state: BlockState): FluidState {
        return if (state[waterlogged]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)
    }

    companion object {
        val waterlogged: BooleanProperty = Properties.WATERLOGGED
    }
}