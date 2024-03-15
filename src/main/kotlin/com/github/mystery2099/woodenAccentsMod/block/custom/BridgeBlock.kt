package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.with
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.Waterloggable
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

class BridgeBlock(settings: Settings?) : HorizontalFacingBlock(settings), Waterloggable {
    init {
        this.defaultState = this.defaultState.with {
            waterlogged to false
            FACING to Direction.NORTH
        }
    }

    companion object {
        val waterlogged: BooleanProperty = Properties.WATERLOGGED
    }
}