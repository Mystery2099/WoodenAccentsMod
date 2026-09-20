package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.fluid.isOf
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.BaseEntityBlock
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

abstract class WaterloggableBlockWithEntity(settings: Properties) : BaseEntityBlock(settings), SimpleWaterloggedBlock {
    init {
        this.registerDefaultState(stateDefinition.any().setValue(waterlogged, false))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(waterlogged)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return super.getStateForPlacement(ctx)?.setValue(
            waterlogged,
            ctx.level.getFluidState(ctx.clickedPos) isOf Fluids.WATER)
            ?: defaultBlockState()
    }

    @Deprecated("Deprecated in Java")
    override fun updateShape(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState = scheduleWaterTickIfWaterlogged(state, world, pos)

    private fun scheduleWaterTickIfWaterlogged(
        state: BlockState,
        world: LevelAccessor,
        pos: BlockPos
    ) : BlockState {
        if (state.getValue(waterlogged)) {
            world.scheduleTick(
                pos,
                Fluids.WATER,
                Fluids.WATER.getTickDelay(world)
            )
        }
        return state
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "if (state.getValue(waterlogged)) Fluids.WATER.getSource(false) else super.getFluidState(state)",
        "com.mystery2099.wooden_accents_mod.block.custom.WaterloggableBlockWithEntity.Companion.waterlogged",
        "net.minecraft.world.level.material.Fluids",
        "net.minecraft.world.level.block.BaseEntityBlock"
    )
    )
    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(waterlogged)) Fluids.WATER.getSource(false)
        else Fluids.EMPTY.defaultFluidState()
    }
    companion object {
        val waterlogged: BooleanProperty = BlockStateProperties.WATERLOGGED
    }
}
