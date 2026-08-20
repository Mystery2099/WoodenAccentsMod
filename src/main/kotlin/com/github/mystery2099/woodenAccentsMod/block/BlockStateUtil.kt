package com.github.mystery2099.woodenAccentsMod.block

import net.minecraft.block.AbstractBlock.AbstractBlockState
import net.minecraft.block.Block
import net.minecraft.registry.tag.TagKey

object BlockStateUtil {
    infix fun AbstractBlockState?.isOf(block: Block): Boolean = this?.isOf(block) ?: false

    infix fun AbstractBlockState?.isIn(tag: TagKey<Block>?): Boolean = this?.isIn(tag) ?: false
}
