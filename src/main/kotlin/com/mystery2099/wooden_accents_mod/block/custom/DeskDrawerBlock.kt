package com.mystery2099.wooden_accents_mod.block.custom

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class DeskDrawerBlock(settings: Settings, val baseBlock: Block, val topBlock: Block) : WaterloggableBlockWithEntity(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        TODO("Not yet implemented")
    }
}