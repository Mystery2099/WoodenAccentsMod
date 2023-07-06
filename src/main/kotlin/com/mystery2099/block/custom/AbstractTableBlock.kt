package com.mystery2099.block.custom

import com.mystery2099.block.custom.enums.CoffeeTableType
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
        defaultState = defaultState.apply {
            with(east, false)
            with(south, false)
            with(west, false)
        }
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
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos!!, neighborPos)?.apply {
            withIfExists(north, world.checkNorth(pos))
            withIfExists(east, world.checkEast(pos))
            withIfExists(south, world.checkSouth(pos))
            withIfExists(west, world.checkWest(pos))
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(north, east, south, west)
    }
    abstract fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean
    private fun WorldAccess.checkNorth(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.NORTH)
    }

    private fun WorldAccess.checkEast(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.EAST)
    }

    private fun WorldAccess.checkSouth(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.SOUTH)
    }

    private fun WorldAccess.checkWest(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.WEST)
    }
    companion object {
        @JvmStatic
        val north: BooleanProperty = Properties.NORTH
        @JvmStatic
        val east: BooleanProperty = Properties.EAST
        @JvmStatic
        val south: BooleanProperty = Properties.SOUTH
        @JvmStatic
        val west: BooleanProperty = Properties.WEST
    }
}