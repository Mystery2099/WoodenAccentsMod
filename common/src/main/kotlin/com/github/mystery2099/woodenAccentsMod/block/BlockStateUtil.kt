package com.github.mystery2099.woodenAccentsMod.block

import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase
import net.minecraft.world.level.block.Block
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.state.BlockBehaviour

object BlockStateUtil {
    infix fun BlockStateBase?.isOf(block: Block): Boolean = this?.`is`(block) ?: false

    infix fun BlockStateBase?.isIn(tag: TagKey<Block>?): Boolean = tag?.let { this?.`is`(it) } ?: false
}
