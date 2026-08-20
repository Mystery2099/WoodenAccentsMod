package com.github.mystery2099.woodenAccentsMod.block

import net.minecraft.block.BlockState
import net.minecraft.state.property.Property

/**
 * Small mutable DSL for applying several properties without repeating intermediate [BlockState] values.
 * [ifExists] is intended for shared configuration where a property is not present on every block.
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
    infix fun <T : Comparable<T>> Property<T>.to(value: T) = setProperty(this, value)

    operator fun invoke(): BlockState = blockState

    open fun <T : Comparable<T>> setProperty(property: Property<T>, value: T) {
        this.blockState = blockState.with(property, value)
    }

    companion object {
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
