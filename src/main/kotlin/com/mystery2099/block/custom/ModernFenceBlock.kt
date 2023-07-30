package com.mystery2099.block.custom

import com.mystery2099.data.ModBlockTags
import com.mystery2099.util.VoxelShapeHelper.combined
import com.mystery2099.util.VoxelShapeHelper.rotate
import com.mystery2099.util.VoxelShapeHelper.union
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class ModernFenceBlock(settings: Block, val sideBlock: Block, val postBlock: Block) : FenceBlock(FabricBlockSettings.copyOf(settings)) {

    override fun canConnect(state: BlockState, neighborIsFullSquare: Boolean, dir: Direction): Boolean {
        return !cannotConnect(state) && neighborIsFullSquare || state.run {
            (isIn(BlockTags.FENCES) && isIn(ModBlockTags.modernFences) == this@ModernFenceBlock.defaultState.isIn(ModBlockTags.modernFences)
            ) || block is FenceGateBlock && FenceGateBlock.canWallConnect(this, dir)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
        .union(run {
            mutableSetOf(
                Block.createCuboidShape(7.5, 0.0, 0.0, 8.5, 15.0, 6.0),
                Block.createCuboidShape(7.0, 11.0, 0.0, 9.0, 14.0, 6.0),
                Block.createCuboidShape(7.0, 2.0, 0.0, 9.0, 5.0, 6.0)
            ).let {
                setOf(if (state.get(NORTH)) it.combined else VoxelShapes.empty(),
                    if (state.get(EAST)) it.rotate(Direction.SOUTH) else VoxelShapes.empty(),
                    if (state.get(SOUTH)) it.rotate(Direction.WEST) else VoxelShapes.empty(),
                    if (state.get(WEST)) it.rotate(Direction.NORTH) else VoxelShapes.empty()
                ).combined
            }
        })
}