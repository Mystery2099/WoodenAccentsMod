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
     * Checks if the given [Block] is the same as the [AbstractBlockState]'s block.
     *
     * @param block The block to compare with.
     * @return `true` if the [AbstractBlockState] is not null and its block is the same as the given [Block], `false` otherwise.
     */
    infix fun AbstractBlockState?.isOf(block: Block): Boolean = this?.isOf(block) ?: false

    /**
     * Determines whether the given [AbstractBlockState] is associated with the specified [TagKey].
     *
     * @param tag The TagKey representing the tag to check.
     * @return true if the [AbstractBlockState] is associated with the specified [TagKey], false otherwise.
     */
    infix fun AbstractBlockState?.isIn(tag: TagKey<Block>?): Boolean = this?.isIn(tag) ?: false

}