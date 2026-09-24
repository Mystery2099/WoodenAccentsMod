package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.with
import com.mojang.serialization.MapCodec
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.core.Direction

class BridgeBlock(settings: Properties?) : HorizontalDirectionalBlock(settings), SimpleWaterloggedBlock {
    override fun codec(): MapCodec<BridgeBlock> = commonCodec

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(FACING, waterlogged)
    }

    init {
        this.registerDefaultState(this.defaultBlockState().with {
            waterlogged to false
            FACING to Direction.NORTH
        })
    }

    companion object {
        val waterlogged: BooleanProperty = BlockStateProperties.WATERLOGGED
        val commonCodec: MapCodec<BridgeBlock> = simpleCodec(::BridgeBlock)
    }
}