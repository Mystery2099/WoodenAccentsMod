package com.github.mystery2099.woodenAccentsMod.util

import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.withProperties
import net.minecraft.block.AbstractBlock.AbstractBlockState
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.property.Property

/**
 * A utility object providing additional functions to be used with [BlockState]
 */
object BlockStateUtil {
    /**
     * Stores a copy of the [BlockState] it is applied to inside an instance of [BlockStateConfigurer].
     *
     * This allows you to set the properties of the stored [BlockState] in a more readable way and returns the newly modified [BlockState].
     *
     * @param configure The functions to be applied to the [BlockState] after storing it within a [BlockStateConfigurer] instance.
     * @receiver [BlockState]
     * @return The configured [BlockState].
     *
     * @see BlockStateConfigurer
     * @see BlockStateConfigurer.setTo
     * @see BlockStateConfigurer.set
     */
    @JvmStatic
    inline fun BlockState.withProperties(configure: BlockStateConfigurer.() -> Unit): BlockState {
        val builder = BlockStateConfigurer(this)
        builder.configure()
        return builder.blockState
    }

    /**
     * Checks if an instance of [AbstractBlockState] is of a specific [Block].
     * An infix and null-checking version of [AbstractBlockState.isOf].
     *
     * @param block The [Block] instance to check against.
     * @return `true` if the [BlockState] is of the [block], `false` otherwise.
     *
     * @see AbstractBlockState.isOf
     */
    infix fun AbstractBlockState?.isOf(block: Block): Boolean = this?.isOf(block) ?: false

    infix fun AbstractBlockState?.isIn(tag: TagKey<Block>?): Boolean = this?.isIn(tag) ?: false

    infix fun FluidState?.isOf(fluid: Fluid): Boolean = this?.isOf(fluid) ?: false

}

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


