package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.with
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.core.Direction

class BridgeBlock(settings: Properties?) : HorizontalDirectionalBlock(settings), SimpleWaterloggedBlock {
    init {
        this.registerDefaultState(this.defaultBlockState().with {
            waterlogged to false
            FACING to Direction.NORTH
        })
    }

    companion object {
        val waterlogged: BooleanProperty = BlockStateProperties.WATERLOGGED
    }
}