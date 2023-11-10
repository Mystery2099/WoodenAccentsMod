package com.github.mystery2099.woodenAccentsMod.block

import net.minecraft.block.AbstractBlock.AbstractBlockState
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.tag.TagKey

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

}