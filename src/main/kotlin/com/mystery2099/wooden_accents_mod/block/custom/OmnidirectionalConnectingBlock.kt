package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.util.BlockStateUtil.withProperties
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.appendShapes
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

open class OmnidirectionalConnectingBlock(settings: Settings) : Block(settings), Waterloggable {
    open val centerShape: VoxelShape = VoxelShapes.fullCube()
    open val northShape: VoxelShape = VoxelShapes.empty()
    open val eastShape: VoxelShape = VoxelShapes.empty()
    open val southShape: VoxelShape = VoxelShapes.empty()
    open val westShape: VoxelShape = VoxelShapes.empty()
    open val upShape: VoxelShape = VoxelShapes.empty()
    open val downShape: VoxelShape = VoxelShapes.empty()

    init {
        defaultState = defaultState.withProperties {
            north setTo false
            east setTo false
            south setTo false
            west setTo false
            up setTo false
            down setTo false
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(waterlogged, north, east, south, west, up, down)
    }

    @Deprecated("Deprecated in Java")
    override fun getFluidState(state: BlockState): FluidState {
        return if (state[waterlogged]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return centerShape.appendShapes {
            northShape case state[north]
            eastShape case state[east]
            southShape case state[south]
            westShape case state[west]
            upShape case state[up]
            downShape case state[down]
        }
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
        return state.setDirectionalProperties(pos, world)
    }

    open fun canConnectNorthOf(pos: BlockPos, world: WorldAccess): Boolean = world.getBlockState(pos.north()).isOf(this)
    open fun canConnectEastOf(pos: BlockPos, world: WorldAccess): Boolean = world.getBlockState(pos.east()).isOf(this)
    open fun canConnectSouthOf(pos: BlockPos, world: WorldAccess): Boolean = world.getBlockState(pos.south()).isOf(this)
    open fun canConnectWestOf(pos: BlockPos, world: WorldAccess): Boolean = world.getBlockState(pos.west()).isOf(this)
    open fun canConnectAbove(pos: BlockPos, world: WorldAccess): Boolean = world.getBlockState(pos.up()).isOf(this)
    open fun canConnectBelow(pos: BlockPos, world: WorldAccess): Boolean = world.getBlockState(pos.down()).isOf(this)
    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(
            waterlogged,
            ctx.world.getFluidState(ctx.blockPos).fluid === Fluids.WATER
        ).setDirectionalProperties(ctx.blockPos, ctx.world)
    }

    private fun BlockState.setDirectionalProperties(pos: BlockPos, world: WorldAccess): BlockState {
        return this.withProperties {
            north setTo canConnectNorthOf(pos, world)
            east setTo canConnectEastOf(pos, world)
            south setTo canConnectSouthOf(pos, world)
            west setTo canConnectWestOf(pos, world)
            up setTo canConnectAbove(pos, world)
            down setTo canConnectBelow(pos, world)
        }
    }

    companion object {
        val waterlogged: BooleanProperty = Properties.WATERLOGGED
        val north: BooleanProperty = Properties.NORTH
        val east: BooleanProperty = Properties.EAST
        val south: BooleanProperty = Properties.SOUTH
        val west: BooleanProperty = Properties.WEST
        val up: BooleanProperty = Properties.UP
        val down: BooleanProperty = Properties.DOWN
    }
}