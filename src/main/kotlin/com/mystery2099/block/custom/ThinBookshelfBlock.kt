package com.mystery2099.block.custom

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ChiseledBookshelfBlock
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class ThinBookshelfBlock(val baseBlock : Block) : ChiseledBookshelfBlock(FabricBlockSettings.copyOf(baseBlock).requires(FeatureFlags.UPDATE_1_20)) {
    companion object {
        private val NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 11.0, 16.0, 16.0, 16.0)!!
        private val EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 5.0, 16.0, 16.0)!!
        private val SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 5.0)!!
        private val WEST_SHAPE = Block.createCuboidShape(11.0, 0.0, 0.0, 16.0, 16.0, 16.0)!!
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return when (state.get(HorizontalFacingBlock.FACING)) {
                Direction.NORTH -> NORTH_SHAPE
                Direction.EAST -> EAST_SHAPE
                Direction.SOUTH -> SOUTH_SHAPE
                Direction.WEST -> WEST_SHAPE
                else -> super.getOutlineShape(state, world, pos, context)
            }
    }
}