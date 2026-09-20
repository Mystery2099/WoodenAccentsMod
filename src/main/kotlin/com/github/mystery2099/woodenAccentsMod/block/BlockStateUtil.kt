package com.github.mystery2099.woodenAccentsMod.block

import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase
import net.minecraft.world.level.block.Block
import net.minecraft.tags.TagKey

object BlockStateUtil {
    infix fun BlockStateBase?.isOf(block: Block): Boolean = this?.`is`(block) ?: false

    infix fun BlockStateBase?.isIn(tag: TagKey<Block>?): Boolean = this?.`is`(tag) ?: false
}
