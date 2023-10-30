package com.github.mystery2099.woodenAccentsMod.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.property.Property

object BlockStateUtil {
    /**
     * With properties
     *
     * @param configure
     * @receiver
     * @return the configured block state
     */
    @JvmStatic
    infix fun BlockState.withProperties(configure: BlockStateConfigurer.() -> Unit): BlockState {
        val builder = BlockStateConfigurer(this)
        builder.configure()
        return builder.blockState
    }

    infix fun BlockState?.isOf(block: Block): Boolean = this?.isOf(block) ?: false
    infix fun BlockState?.isIn(tag: TagKey<Block>?): Boolean = this?.isIn(tag) ?: false

}

/**
 * Block state configurer
 *
 * @property blockState
 * @constructor Create Block state configurer containing a block state
 */
class BlockStateConfigurer(var blockState: BlockState) {
    /**
     * Set to
     *
     * @param T
     * @param value
     */
    infix fun <T : Comparable<T>> Property<T>.setTo(value: T) {
        set(this, value)
    }

    /**
     * Set
     *
     * @param T
     * @param property
     * @param value
     */
    operator fun <T : Comparable<T>> set(property: Property<T>, value: T) {
        blockState = blockState.with(property, value)
    }

    /**
     * Get
     *
     * @param T
     * @param property
     * @return
     */
    operator fun <T : Comparable<T>> get(property: Property<T>): T = blockState[property]
}


