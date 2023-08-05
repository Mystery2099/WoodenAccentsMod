package com.mystery2099.wooden_accents_mod.util

import net.minecraft.data.client.When
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

//For "When" related variables and functions.
// "When" is a class used for creating conditions to be used in block state code generation
object WhenUtil {
    val whenUp: When.PropertyCondition = newWhen.set(Properties.UP, true)
    val whenDown: When.PropertyCondition = newWhen.set(Properties.DOWN, true)
    val whenNorth: When.PropertyCondition = newWhen.set(Properties.NORTH, true)
    val whenEast: When.PropertyCondition = newWhen.set(Properties.EAST, true)
    val whenSouth: When.PropertyCondition = newWhen.set(Properties.SOUTH, true)
    val whenWest: When.PropertyCondition = newWhen.set(Properties.WEST, true)

    val whenNorthEast: When = whenAllOf(whenNorth, whenEast)
    val whenSouthEast: When = whenAllOf(whenSouth, whenEast)
    val whenNorthWest: When = whenAllOf(whenNorth, whenWest)
    val whenSouthWest: When = whenAllOf(whenSouth, whenWest)

    val whenNotUp: When.PropertyCondition = newWhen.set(Properties.UP, false)
    val whenNotDown: When.PropertyCondition = newWhen.set(Properties.DOWN, false)
    val whenNotNorth: When.PropertyCondition = newWhen.set(Properties.NORTH, false)
    val whenNotEast: When.PropertyCondition = newWhen.set(Properties.EAST, false)
    val whenNotSouth: When.PropertyCondition = newWhen.set(Properties.SOUTH, false)
    val whenNotWest: When.PropertyCondition = newWhen.set(Properties.WEST, false)

    val whenNotNorthEast: When = whenAllOf(whenNotNorth, whenNotEast)
    val whenNotSouthEast: When = whenAllOf(whenNotSouth, whenNotEast)
    val whenNotNorthWest: When = whenAllOf(whenNotNorth, whenNotWest)
    val whenNotSouthWest: When = whenAllOf(whenNotSouth, whenNotWest)

    val whenFacingNorth: When.PropertyCondition = newWhen.set(Properties.FACING, Direction.NORTH)
    val whenFacingEast: When.PropertyCondition = newWhen.set(Properties.FACING, Direction.EAST)
    val whenFacingSouth: When.PropertyCondition = newWhen.set(Properties.FACING, Direction.SOUTH)
    val whenFacingWest: When.PropertyCondition = newWhen.set(Properties.FACING, Direction.WEST)
    val whenFacingUp: When.PropertyCondition = newWhen.set(Properties.FACING, Direction.UP)
    val whenFacingDown: When.PropertyCondition = newWhen.set(Properties.FACING, Direction.DOWN)

    //Horizontal Facing
    val whenFacingNorthHorizontal: When.PropertyCondition = newWhen.set(Properties.HORIZONTAL_FACING, Direction.NORTH)
    val whenFacingEastHorizontal: When.PropertyCondition = newWhen.set(Properties.HORIZONTAL_FACING, Direction.EAST)
    val whenFacingSouthHorizontal: When.PropertyCondition = newWhen.set(Properties.HORIZONTAL_FACING, Direction.SOUTH)
    val whenFacingWestHorizontal: When.PropertyCondition = newWhen.set(Properties.HORIZONTAL_FACING, Direction.WEST)


    inline val newWhen: When.PropertyCondition
        get() = When.create()
    fun whenAllOf(vararg others: When): When = When.allOf(*others)
    fun When.and(other: When): When = whenAllOf(this, other)
    operator fun When.plus(other: When): When = and(other)

}