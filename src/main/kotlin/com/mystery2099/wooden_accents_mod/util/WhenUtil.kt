package com.mystery2099.wooden_accents_mod.util

import net.minecraft.data.client.When
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

//For "When" related variables and functions.
// "When" is a class used for creating conditions to be used in block state code generation
object WhenUtil {
    val up: When.PropertyCondition = newWhen.set(Properties.UP, true)
    val down: When.PropertyCondition = newWhen.set(Properties.DOWN, true)
    val north: When.PropertyCondition = newWhen.set(Properties.NORTH, true)
    val east: When.PropertyCondition = newWhen.set(Properties.EAST, true)
    val south: When.PropertyCondition = newWhen.set(Properties.SOUTH, true)
    val west: When.PropertyCondition = newWhen.set(Properties.WEST, true)

    val whenNorthEast: When by lazy { north and east }
    val whenSouthEast: When by lazy { south and east }
    val whenNorthWest: When by lazy { north and west }
    val whenSouthWest: When by lazy { south and west }

    val notUp: When.PropertyCondition = newWhen.set(Properties.UP, false)
    val notDown: When.PropertyCondition = newWhen.set(Properties.DOWN, false)
    val notNorth: When.PropertyCondition = newWhen.set(Properties.NORTH, false)
    val notEast: When.PropertyCondition = newWhen.set(Properties.EAST, false)
    val notSouth: When.PropertyCondition = newWhen.set(Properties.SOUTH, false)
    val notWest: When.PropertyCondition = newWhen.set(Properties.WEST, false)

    val notNorthEast: When = notNorth and notEast
    val notSouthEast: When = notSouth and notEast
    val notNorthWest: When = notNorth and notWest
    val notSouthWest: When = notSouth and notWest

    val facingNorth: When.PropertyCondition by lazy { newWhen.set(Properties.FACING, Direction.NORTH) }
    val facingEast: When.PropertyCondition by lazy { newWhen.set(Properties.FACING, Direction.EAST) }
    val facingSouth: When.PropertyCondition by lazy { newWhen.set(Properties.FACING, Direction.SOUTH) }
    val facingWest: When.PropertyCondition by lazy { newWhen.set(Properties.FACING, Direction.WEST) }
    val facingUp: When.PropertyCondition by lazy { newWhen.set(Properties.FACING, Direction.UP) }
    val facingDown: When.PropertyCondition by lazy { newWhen.set(Properties.FACING, Direction.DOWN) }

    //Horizontal Facing

    val facingNorthHorizontal: When.PropertyCondition = newWhen.set(Properties.HORIZONTAL_FACING, Direction.NORTH)
    val facingEastHorizontal: When.PropertyCondition = newWhen.set(Properties.HORIZONTAL_FACING, Direction.EAST)
    val facingSouthHorizontal: When.PropertyCondition = newWhen.set(Properties.HORIZONTAL_FACING, Direction.SOUTH)
    val facingWestHorizontal: When.PropertyCondition = newWhen.set(Properties.HORIZONTAL_FACING, Direction.WEST)

    inline val newWhen: When.PropertyCondition
        get() = When.create()

    infix fun When.and(other: When): When = When.allOf(this, other)
    infix fun When.and(others: Collection<When>): When = When.allOf(this, *others.toTypedArray())
    infix fun When.and(others: Array<When>): When = When.allOf(this, *others)
    infix operator fun When.plus(other: When): When = and(other)
    infix fun When.or(other: When): When = When.anyOf(this, other)
}