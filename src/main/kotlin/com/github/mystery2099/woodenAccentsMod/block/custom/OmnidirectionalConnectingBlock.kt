package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.with
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.PipeBlock
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

open class OmnidirectionalConnectingBlock(settings: Properties) : PipeBlock(2.0F / 16.0F, settings), SimpleWaterloggedBlock {

    init {
        registerDefaultState(defaultBlockState().with {
            north to false
            east to false
            south to false
            west to false
            up to false
            down to false
        })
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(waterlogged, north, east, south, west, up, down)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "if (state.getValue(waterlogged)) Fluids.WATER.getSource(false) else super.getFluidState(state)",
        "com.mystery2099.wooden_accents_mod.block.custom.OmnidirectionalConnectingBlock.Companion.waterlogged",
        "net.minecraft.world.level.material.Fluids",
        "net.minecraft.world.level.block.Block"
    )
    )
    @Suppress("DEPRECATION")
    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(waterlogged)) Fluids.WATER.getSource(false)
        else super.getFluidState(state)
    }

    @Deprecated("Deprecated in Java")
    override fun updateShape(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState {
        if (state.getValue(waterlogged)) world.scheduleTick(
            pos,
            Fluids.WATER,
            Fluids.WATER.getTickDelay(world)
        )
        return state.setDirectionalProperties(pos, world)
    }

    open fun canConnectNorthOf(pos: BlockPos, world: LevelAccessor): Boolean = world.getBlockState(pos.north()).`is`(this)

    open fun canConnectEastOf(pos: BlockPos, world: LevelAccessor): Boolean = world.getBlockState(pos.east()).`is`(this)

    open fun canConnectSouthOf(pos: BlockPos, world: LevelAccessor): Boolean = world.getBlockState(pos.south()).`is`(this)

    open fun canConnectWestOf(pos: BlockPos, world: LevelAccessor): Boolean = world.getBlockState(pos.west()).`is`(this)

    open fun canConnectAbove(pos: BlockPos, world: LevelAccessor): Boolean = world.getBlockState(pos.above()).`is`(this)

    open fun canConnectBelow(pos: BlockPos, world: LevelAccessor): Boolean = world.getBlockState(pos.below()).`is`(this)
    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(
            waterlogged,
            ctx.level.getFluidState(ctx.clickedPos).type === Fluids.WATER
        ).setDirectionalProperties(ctx.clickedPos, ctx.level)
    }

    private fun BlockState.setDirectionalProperties(pos: BlockPos, world: LevelAccessor): BlockState {
        return this.with {
            north to canConnectNorthOf(pos, world)
            east to canConnectEastOf(pos, world)
            south to canConnectSouthOf(pos, world)
            west to canConnectWestOf(pos, world)
            up to canConnectAbove(pos, world)
            down to canConnectBelow(pos, world)
        }
    }

    companion object {
        val waterlogged: BooleanProperty = BlockStateProperties.WATERLOGGED
        val north: BooleanProperty = BlockStateProperties.NORTH
        val east: BooleanProperty = BlockStateProperties.EAST
        val south: BooleanProperty = BlockStateProperties.SOUTH
        val west: BooleanProperty = BlockStateProperties.WEST
        val up: BooleanProperty = BlockStateProperties.UP
        val down: BooleanProperty = BlockStateProperties.DOWN
    }
}
