package com.mystery2099.block.custom

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

abstract class AbstractTableBlock(baseBlock: Block, topBlock: Block) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)) {
    init {
        defaultState = defaultState.with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState? {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos!!, neighborPos)
            ?.withIfExists(NORTH, world.checkNorth(pos))
            ?.withIfExists(EAST, world.checkEast(pos))
            ?.withIfExists(SOUTH, world.checkSouth(pos))
            ?.withIfExists(WEST, world.checkWest(pos))

    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(NORTH, EAST, SOUTH, WEST)
    }
    open fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        return getBlockState(pos.offset(direction)).block is AbstractTableBlock
    }
    open fun WorldAccess.checkNorth(pos: BlockPos): Boolean {
        return checkDirection(pos, Direction.NORTH)
    }

    open fun WorldAccess.checkEast(pos: BlockPos): Boolean {
        return checkDirection(pos, Direction.EAST)
    }

    open fun WorldAccess.checkSouth(pos: BlockPos): Boolean {
        return checkDirection(pos, Direction.SOUTH)
    }

    open fun WorldAccess.checkWest(pos: BlockPos): Boolean {
        return checkDirection(pos, Direction.WEST)
    }
    companion object {
        @JvmStatic
        val NORTH: BooleanProperty = Properties.NORTH
        @JvmStatic
        val EAST: BooleanProperty = Properties.EAST
        @JvmStatic
        val SOUTH: BooleanProperty = Properties.SOUTH
        @JvmStatic
        val WEST: BooleanProperty = Properties.WEST
    }
}