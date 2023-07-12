package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class PlankLadderBlock(val baseBlock: Block) : LadderBlock(FabricBlockSettings.copyOf(Blocks.LADDER)) {
    init {
        WoodenAccentsModItemGroups.outsideItems += this
    }

    companion object {
        private val NORTH_SHAPE = VoxelShapes.union(
            createCuboidShape(2.0, 1.0, 15.0, 14.0, 4.0, 16.0),
            createCuboidShape(2.0, 12.0, 15.0, 14.0, 15.0, 16.0),
            createCuboidShape(2.0, 6.0, 15.0, 14.0, 10.0, 16.0)
        )
        private val EAST_SHAPE = VoxelShapes.union(
            createCuboidShape(0.0, 1.0, 2.0, 1.0, 4.0, 14.0),
            createCuboidShape(0.0, 12.0, 2.0, 1.0, 15.0, 14.0),
            createCuboidShape(0.0, 6.0, 2.0, 1.0, 10.0, 14.0)
        )
        private val SOUTH_SHAPE = VoxelShapes.union(
            createCuboidShape(2.0, 1.0, 0.0, 14.0, 4.0, 1.0),
            createCuboidShape(2.0, 12.0, 0.0, 14.0, 15.0, 1.0),
            createCuboidShape(2.0, 6.0, 0.0, 14.0, 10.0, 1.0)
        )
        private val WEST_SHAPE = VoxelShapes.union(
            createCuboidShape(15.0, 1.0, 2.0, 16.0, 4.0, 14.0),
            createCuboidShape(15.0, 12.0, 2.0, 16.0, 15.0, 14.0),
            createCuboidShape(15.0, 6.0, 2.0, 16.0, 10.0, 14.0)
        )
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        return when (state[FACING]) {
            Direction.NORTH -> NORTH_SHAPE
            Direction.EAST -> EAST_SHAPE
            Direction.SOUTH -> SOUTH_SHAPE
            Direction.WEST -> WEST_SHAPE
            else -> super.getOutlineShape(state, world, pos, context)
        }
    }

}