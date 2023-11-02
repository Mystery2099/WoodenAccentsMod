package com.github.mystery2099.woodenAccentsMod.util

import net.minecraft.data.client.When
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

/**
 * WhenUtil is a utility object for creating conditions to be used in block state code generation.
 */
object WhenUtil {

    // Property Conditions for specific block state properties
    val up: When.PropertyCondition = When.create().set(Properties.UP, true)
    val down: When.PropertyCondition = When.create().set(Properties.DOWN, true)
    val north: When.PropertyCondition = When.create().set(Properties.NORTH, true)
    val east: When.PropertyCondition = When.create().set(Properties.EAST, true)
    val south: When.PropertyCondition = When.create().set(Properties.SOUTH, true)
    val west: When.PropertyCondition = When.create().set(Properties.WEST, true)

    // Combined Property Conditions for diagonals
    val whenNorthEast: When by lazy { north and east }
    val whenSouthEast: When by lazy { south and east }
    val whenNorthWest: When by lazy { north and west }
    val whenSouthWest: When by lazy { south and west }

    // Negated Property Conditions
    val notUp: When.PropertyCondition = When.create().set(Properties.UP, false)
    val notDown: When.PropertyCondition = When.create().set(Properties.DOWN, false)
    val notNorth: When.PropertyCondition = When.create().set(Properties.NORTH, false)
    val notEast: When.PropertyCondition = When.create().set(Properties.EAST, false)
    val notSouth: When.PropertyCondition = When.create().set(Properties.SOUTH, false)
    val notWest: When.PropertyCondition = When.create().set(Properties.WEST, false)

    // Combined Negated Property Conditions for diagonals
    val notNorthEast: When = notNorth and notEast
    val notSouthEast: When = notSouth and notEast
    val notNorthWest: When = notNorth and notWest
    val notSouthWest: When = notSouth and notWest

    // Combined Negated Property Conditions for diagonals
    val facingNorth: When.PropertyCondition by lazy { When.create().set(Properties.FACING, Direction.NORTH) }
    val facingEast: When.PropertyCondition by lazy { When.create().set(Properties.FACING, Direction.EAST) }
    val facingSouth: When.PropertyCondition by lazy { When.create().set(Properties.FACING, Direction.SOUTH) }
    val facingWest: When.PropertyCondition by lazy { When.create().set(Properties.FACING, Direction.WEST) }
    val facingUp: When.PropertyCondition by lazy { When.create().set(Properties.FACING, Direction.UP) }
    val facingDown: When.PropertyCondition by lazy { When.create().set(Properties.FACING, Direction.DOWN) }

    // Property Conditions for horizontal facing directions
    val facingNorthHorizontal: When.PropertyCondition = When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH)
    val facingEastHorizontal: When.PropertyCondition = When.create().set(Properties.HORIZONTAL_FACING, Direction.EAST)
    val facingSouthHorizontal: When.PropertyCondition = When.create().set(Properties.HORIZONTAL_FACING, Direction.SOUTH)
    val facingWestHorizontal: When.PropertyCondition = When.create().set(Properties.HORIZONTAL_FACING, Direction.WEST)


    /**
     * Combines the current condition with another.
     *
     * @param other The other condition to combine with.
     * @return The combined condition.
     */
    infix fun When.and(other: When): When = When.allOf(this, other)

    /**
     * Combines the current condition with a collection of other conditions.
     *
     * @param others The collection of other conditions to combine with.
     * @return The combined condition.
     */
    infix fun When.and(others: Collection<When>): When = When.allOf(this, *others.toTypedArray())

    /**
     * Combines the current condition with an array of other conditions.
     *
     * @param others The array of other conditions to combine with.
     * @return The combined condition.
     */
    infix fun When.and(others: Array<When>): When = When.allOf(this, *others)

    /**
     * Combines the current condition with another.
     *
     * @param other The other condition to combine with.
     * @return The combined condition.
     */
    infix operator fun When.plus(other: When): When = this and other

    /**
     * Combines the current condition with another.
     *
     * @param other The other condition to combine with.
     * @return The combined condition.
     */
    infix fun When.or(other: When): When = When.anyOf(this, other)
}