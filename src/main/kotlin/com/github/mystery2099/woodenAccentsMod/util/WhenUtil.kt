package com.github.mystery2099.woodenAccentsMod.util

import net.minecraft.data.models.blockstates.Condition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.core.Direction

object WhenUtil {

    // Connections
    val up: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.UP, true)
    val down: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.DOWN, true)
    val north: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.NORTH, true)
    val east: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.EAST, true)
    val south: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.SOUTH, true)
    val west: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.WEST, true)

    // Missing connections
    val notUp: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.UP, false)
    val notDown: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.DOWN, false)
    val notNorth: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.NORTH, false)
    val notEast: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.EAST, false)
    val notSouth: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.SOUTH, false)
    val notWest: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.WEST, false)

    // Missing diagonal connections (neither of the two directions)
    val notNorthEast: Condition = allOf(notNorth, notEast)
    val notSouthEast: Condition = allOf(notSouth, notEast)
    val notNorthWest: Condition = allOf(notNorth, notWest)
    val notSouthWest: Condition = allOf(notSouth, notWest)

    // Horizontal facing
    val facingNorthHorizontal: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
    val facingEastHorizontal: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
    val facingSouthHorizontal: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
    val facingWestHorizontal: Condition.TerminalCondition = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)

    fun allOf(vararg conditions: Condition): Condition = Condition.and(*conditions)
}
