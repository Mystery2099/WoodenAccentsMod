package com.github.mystery2099.woodenAccentsMod.block

import net.minecraft.block.BlockState
import net.minecraft.state.property.Property

/**
 * A utility class for configuring properties in a [BlockState] instance.
 *
 * @param blockState The initial [BlockState] instance to configure.
 */
class BlockStateConfigurer(var blockState: BlockState) {
    /**
     * Sets the value of a Property to a specified value.
     *
     * @param value The value to set the Property to.
     * @param T The type of the Property, must extend Comparable.
     *
     * @see Property
     * @see set
     * @see withProperties
     */
    infix fun <T : Comparable<T>> Property<T>.setTo(value: T) {
        set(this, value)
    }

    /**
     * Sets the value of a property in the current BlockState instance.
     *
     * @param property The property to set the value for.
     * @param value The value to set the property to.
     * @param T The type of the property, must extend Comparable.
     *
     * @see Property
     * @see setTo
     * @see withProperties
     */
    fun <T : Comparable<T>> set(property: Property<T>, value: T) {
        blockState = blockState.with(property, value)
    }

    companion object {
        /**
         * Sets the values of properties in the current [BlockState] instance.
         *
         * @param configure A lambda function that configures the [BlockStateConfigurer] by setting properties.
         * @return The [BlockState] with the modified properties.
         *
         * @see BlockStateConfigurer
         */
        @JvmStatic
        inline fun BlockState.withProperties(configure: BlockStateConfigurer.() -> Unit): BlockState {
            val builder = BlockStateConfigurer(this)
            builder.configure()
            return builder.blockState
        }
    }
}