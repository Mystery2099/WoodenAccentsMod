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
 * Abstract waterloggable block.
 *
 * This abstract class represents a [Block] that can be waterlogged, allowing it to interact with [FluidState]s, specifically water.
 *
 * @constructor Creates an instance of `AbstractWaterloggableBlock` with the specified [settings].
 * @param settings The settings for the waterloggable block.
 */
abstract class AbstractWaterloggableBlock(settings: Settings) : Block(settings), Waterloggable {

    init { defaultState = defaultState.with(waterlogged, false) }

    /**
     * Appends the [waterlogged] property to the [BlockState] properties.
     *
     * @param builder The [BlockState] builder for this [Block].
     */
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(waterlogged)
    }

    /**
     * Determines the [BlockState] when placing the block, considering nearby [FluidState]s.
     *
     * @param ctx The placement context.
     * @return The [BlockState] of the waterloggable block after placement.
     */
    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx)?.with(waterlogged, ctx.world.getFluidState(ctx.blockPos).fluid == Fluids.WATER)
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
    ): BlockState {
        if (state[waterlogged]) world.scheduleFluidTick(
            pos,
            Fluids.WATER,
            Fluids.WATER.getTickRate(world)
        )
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
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