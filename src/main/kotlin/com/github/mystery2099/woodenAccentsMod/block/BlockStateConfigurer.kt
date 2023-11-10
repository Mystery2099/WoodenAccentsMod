package com.github.mystery2099.woodenAccentsMod.block

import com.github.mystery2099.woodenAccentsMod.block.BlockStateUtil.withProperties
import net.minecraft.block.BlockState
import net.minecraft.state.property.Property

/**
 * Stores a [BlockState] instance and provides methods for adding properties to it in a more readable way than using [BlockState.with] for each property.
 *
 * @property blockState The [BlockState] to be stored and modified in this class's instance.
 * @constructor Creates a Block state configurer containing a [BlockState].
 *
 * @see BlockStateUtil.withProperties
 */
class BlockStateConfigurer(var blockState: BlockState) {
    /**
     * Sets the value of a [Property] to a [value] in the [blockState]. This is the infix version of [set].
     *
     * @param T The type of property to set in the [blockState].
     * @param value The value to set to the [Property] of [T].
     *
     * @see set
     */

    infix fun <T : Comparable<T>> Property<T>.setTo(value: T) {
        set(this, value)
    }

    /**
     * Sets the value of a [property] to a [value] in the [blockState].
     *
     * @param T The type of [Property] to set in the [blockState].
     * @param property The [Property] to set in the [blockState].
     * @param value The value to set to the [Property] of [T].
     *
     * @see BlockState.with
     * @see setTo
     */
    fun <T : Comparable<T>> set(property: Property<T>, value: T) {
        blockState = blockState.with(property, value)
    }
}