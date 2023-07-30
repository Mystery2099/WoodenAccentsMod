package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.util.VoxelShapeHelper.rotate
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class ThinBookshelfBlock(val baseBlock : Block) : ChiseledBookshelfBlock(FabricBlockSettings.copyOf(baseBlock).requires(FeatureFlags.UPDATE_1_20)) {
    init {
        WoodenAccentsModItemGroups.livingRoomItems += this
    }
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
                Direction.EAST -> NORTH_SHAPE.rotate(Direction.SOUTH)
                Direction.SOUTH -> NORTH_SHAPE.rotate(Direction.WEST)
                Direction.WEST -> NORTH_SHAPE.rotate(Direction.NORTH)
                else -> super.getOutlineShape(state, world, pos, context)
            }
    }
}