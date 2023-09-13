package com.mystery2099.wooden_accents_mod.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.property.Property

object BlockStateUtil {
    infix fun BlockState.withProperties(configure: BlockStateConfigurer.() -> Unit): BlockState {
        val builder = BlockStateConfigurer(this)
        builder.configure()
        return builder.blockState
    }
    infix fun BlockState?.isOf(block: Block): Boolean = this?.isOf(block) ?: false

}
class BlockStateConfigurer(var blockState: BlockState) {
    infix fun <T : Comparable<T>> Property<T>.setTo(value: T) {
        blockState = blockState.with(this, value)
    }
}


