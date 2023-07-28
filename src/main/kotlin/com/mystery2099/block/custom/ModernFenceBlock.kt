package com.mystery2099.block.custom

import com.mystery2099.data.ModBlockTags
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FenceBlock
import net.minecraft.block.FenceGateBlock
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.Direction

class ModernFenceBlock(settings: Block, val sideBlock: Block, val postBlock: Block) : FenceBlock(FabricBlockSettings.copyOf(settings)) {

    override fun canConnect(state: BlockState, neighborIsFullSquare: Boolean, dir: Direction): Boolean {
        val block = state.block
        val bl = state.isIn(BlockTags.FENCES) && state.isIn(ModBlockTags.modernFences) == this.defaultState.isIn(ModBlockTags.modernFences)
        val bl2 = block is FenceGateBlock && FenceGateBlock.canWallConnect(state, dir)
        return !cannotConnect(state) && neighborIsFullSquare || bl || bl2
    }
}