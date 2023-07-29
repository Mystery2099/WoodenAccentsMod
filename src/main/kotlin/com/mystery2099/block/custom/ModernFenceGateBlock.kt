package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsMod.woodType
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FenceGateBlock
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class ModernFenceGateBlock(baseGate: FenceGateBlock, val baseBlock: Block) : FenceGateBlock(FabricBlockSettings.copyOf(baseGate), baseGate.woodType()) {
    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val northSouthShape = VoxelShapes.union(
            Block.createCuboidShape(0.0, 0.0, 7.5, 1.0, 15.0, 8.5),
            Block.createCuboidShape(15.0, 0.0, 7.5, 16.0, 15.0, 8.5),
            Block.createCuboidShape(11.0, 1.0, 7.5, 13.0, 16.0, 8.5),
            Block.createCuboidShape(3.0, 1.0, 7.5, 5.0, 16.0, 8.5),
            Block.createCuboidShape(7.0, 1.0, 7.5, 9.0, 15.0, 8.5),
            Block.createCuboidShape(0.0, 11.0, 7.0, 16.0, 14.0, 9.0),
            Block.createCuboidShape(0.0, 2.0, 7.0, 16.0, 5.0, 9.0),
            Block.createCuboidShape(0.0, 2.0, 7.5, 16.0, 14.0, 8.5)
        )
        return when (state.get(FACING)) {
            Direction.NORTH, Direction.SOUTH -> northSouthShape
            else -> super.getOutlineShape(state, world, pos, context)
        }
    }
}