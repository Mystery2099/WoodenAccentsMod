package com.github.mystery2099.woodenAccentsMod.block

import net.minecraft.block.BlockState
import net.minecraft.state.property.Property

/**
 * A utility class for configuring properties in a [BlockState] instance.
 *
 * @param blockState The initial [BlockState] instance to configure.
 */
open class BlockStateConfigurer(var blockState: BlockState) {
    inner class ExistsWrapper<T: Comparable<T>>(val property: Property<T>) {
        infix fun to (value: T) {
            ifExistsConfigurer.also {
                it.blockState = blockState
                setProperty(property, value)
                blockState = it.blockState

            }
        }
    }

    fun <T: Comparable<T>> Property<T>.ifExists() = ExistsWrapper(this)
    inline fun ifExists(configure: BlockStateConfigurer.() -> Unit) {
        ifExistsConfigurer.also {
            it.blockState = blockState
            it.configure()
            this.blockState = it.blockState
        }
    }
    /**
     * Sets the value of a Property to a specified value.
     *
     * [setProperty] is the more conventional version of this function
     *
     * @param value The value to set the Property to.
     * @param T The type of the Property, must extend Comparable.
     *
     * @see Property
     * @see setProperty
     * @see with
     */
    infix fun <T : Comparable<T>> Property<T>.to(value: T) = setProperty(this, value)

    operator fun invoke(): BlockState = blockState

    /**
     * Sets the value of a property in the current BlockState instance.
     *
     * [to] is the 'infix' version of this, designed to be more human-readable
     *
     * @param property The property to set the value for.
     * @param value The value to set the property to.
     * @param T The type of the property, must extend Comparable.
     *
     * @see Property
     * @see to
     * @see with
     */
    open fun <T : Comparable<T>> setProperty(property: Property<T>, value: T) {
        this.blockState = blockState.with(property, value)
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
        inline fun BlockState.with(configure: BlockStateConfigurer.() -> Unit): BlockState {
            val builder = BlockStateConfigurer(this)
            builder.configure()
            return builder.blockState
        }

    }

    val ifExistsConfigurer by lazy {
        object : BlockStateConfigurer(blockState) {
            override fun <T : Comparable<T>> setProperty(property: Property<T>, value: T) {
                blockState = blockState.withIfExists(property, value)
            }
        }
    }
}



