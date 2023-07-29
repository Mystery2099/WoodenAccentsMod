package com.mystery2099.block.custom

import com.mystery2099.data.ModBlockTags
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class ModernFenceBlock(settings: Block, val sideBlock: Block, val postBlock: Block) : FenceBlock(FabricBlockSettings.copyOf(settings)) {

    override fun canConnect(state: BlockState, neighborIsFullSquare: Boolean, dir: Direction): Boolean {
        val block = state.block
        val bl = state.isIn(BlockTags.FENCES) && state.isIn(ModBlockTags.modernFences) == this.defaultState.isIn(ModBlockTags.modernFences)
        val bl2 = block is FenceGateBlock && FenceGateBlock.canWallConnect(state, dir)
        return !cannotConnect(state) && neighborIsFullSquare || bl || bl2
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {

        return super.getOutlineShape(state, world, pos, context)
    }
}