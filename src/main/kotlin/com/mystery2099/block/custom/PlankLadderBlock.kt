package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.util.VoxelShapeHelper.combined
import com.mystery2099.util.VoxelShapeHelper.rotateAndCombine
import com.mystery2099.util.VoxelShapeRotations
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class PlankLadderBlock(val baseBlock: Block) : LadderBlock(FabricBlockSettings.copyOf(Blocks.LADDER)) {
    init { WoodenAccentsModItemGroups.outsideItems += this }

    companion object {
        private val northShapes = arrayOf(
            createCuboidShape(2.0, 1.0, 15.0, 14.0, 4.0, 16.0),
            createCuboidShape(2.0, 12.0, 15.0, 14.0, 15.0, 16.0),
            createCuboidShape(2.0, 6.0, 15.0, 14.0, 10.0, 16.0)
        )
        private val northShape = northShapes.combined
        private val eastShape = northShapes.rotateAndCombine(VoxelShapeRotations.LEFT)
        private val southShape = northShapes.rotateAndCombine(VoxelShapeRotations.FLIP)
        private val westShape = northShapes.rotateAndCombine(VoxelShapeRotations.RIGHT)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = when (state[FACING]) {
        Direction.NORTH -> northShape
        Direction.EAST -> eastShape
        Direction.SOUTH -> southShape
        Direction.WEST -> westShape
        else -> super.getOutlineShape(state, world, pos, context)
    }

}