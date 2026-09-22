package com.github.mystery2099.woodenAccentsMod.block.custom

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelAccessor


abstract class AbstractWaterloggableBlock(settings: Properties) : Block(settings), SimpleWaterloggedBlock {

    init { registerDefaultState(defaultBlockState().setValue(waterlogged, false)) }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(waterlogged)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return this.defaultBlockState().setValue(waterlogged, ctx.level.getFluidState(ctx.clickedPos).type == Fluids.WATER)
    }

    @Deprecated("Deprecated in Java")
	override fun updateShape(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState = updateShape(state, pos, world)

    fun updateShape(state: BlockState, pos: BlockPos, world: LevelAccessor): BlockState {
        if (state.getValue(waterlogged)) world.scheduleTick(
            pos,
            Fluids.WATER,
            Fluids.WATER.getTickDelay(world)
        )
        return state
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "if (state.getValue(waterlogged)) Fluids.WATER.getSource(false) else super.getFluidState(state)",
        "com.mystery2099.wooden_accents_mod.block.custom.AbstractWaterloggableBlock.Companion.waterlogged",
        "net.minecraft.world.level.material.Fluids",
        "net.minecraft.world.level.block.Block"
    )
    )
    @Suppress("DEPRECATION")
    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(waterlogged)) Fluids.WATER.getSource(false)
        else super.getFluidState(state)
    }

    companion object {
        val waterlogged: BooleanProperty = BlockStateProperties.WATERLOGGED
    }
}
