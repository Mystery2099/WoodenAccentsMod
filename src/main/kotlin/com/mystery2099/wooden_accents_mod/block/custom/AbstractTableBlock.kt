package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.ModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

abstract class AbstractTableBlock(baseBlock: Block, topBlock: Block) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    GroupedBlock {
    init {
        defaultState = defaultState.apply {
            with(north, false)
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
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos!!, neighborPos)
            ?.withIfExists(north, world.checkNorth(pos))
            ?.withIfExists(east, world.checkEast(pos))
            ?.withIfExists(south, world.checkSouth(pos))
            ?.withIfExists(west, world.checkWest(pos))

    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(north, east, south, west)
    }
    open fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        return getBlockState(pos.offset(direction)).block is AbstractTableBlock
    }
    open fun WorldAccess.checkNorth(pos: BlockPos): Boolean = checkDirection(pos, Direction.NORTH)

    open fun WorldAccess.checkEast(pos: BlockPos): Boolean = checkDirection(pos, Direction.EAST)

    open fun WorldAccess.checkSouth(pos: BlockPos): Boolean = checkDirection(pos, Direction.SOUTH)

    open fun WorldAccess.checkWest(pos: BlockPos): Boolean = checkDirection(pos, Direction.WEST)
    override val itemGroup get() = ModItemGroups.kitchenItemGroup

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