package com.github.mystery2099.woodenAccentsMod.util

import net.minecraft.data.client.When
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

object WhenUtil {

    // Connections
    val up: When.PropertyCondition = When.create().set(Properties.UP, true)
    val down: When.PropertyCondition = When.create().set(Properties.DOWN, true)
    val north: When.PropertyCondition = When.create().set(Properties.NORTH, true)
    val east: When.PropertyCondition = When.create().set(Properties.EAST, true)
    val south: When.PropertyCondition = When.create().set(Properties.SOUTH, true)
    val west: When.PropertyCondition = When.create().set(Properties.WEST, true)

    // Missing connections
    val notUp: When.PropertyCondition = When.create().set(Properties.UP, false)
    val notDown: When.PropertyCondition = When.create().set(Properties.DOWN, false)
    val notNorth: When.PropertyCondition = When.create().set(Properties.NORTH, false)
    val notEast: When.PropertyCondition = When.create().set(Properties.EAST, false)
    val notSouth: When.PropertyCondition = When.create().set(Properties.SOUTH, false)
    val notWest: When.PropertyCondition = When.create().set(Properties.WEST, false)

    // Missing diagonal connections (neither of the two directions)
    val notNorthEast: When = notNorth and notEast
    val notSouthEast: When = notSouth and notEast
    val notNorthWest: When = notNorth and notWest
    val notSouthWest: When = notSouth and notWest

    // Horizontal facing
    val facingNorthHorizontal: When.PropertyCondition = When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH)
    val facingEastHorizontal: When.PropertyCondition = When.create().set(Properties.HORIZONTAL_FACING, Direction.EAST)
    val facingSouthHorizontal: When.PropertyCondition = When.create().set(Properties.HORIZONTAL_FACING, Direction.SOUTH)
    val facingWestHorizontal: When.PropertyCondition = When.create().set(Properties.HORIZONTAL_FACING, Direction.WEST)

    infix fun When.and(other: When): When = When.allOf(this, other)

    infix fun When.or(other: When): When = When.anyOf(this, other)
}
