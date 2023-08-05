package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.ModItemGroups
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.plus
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class ModernFenceBlock(settings: Block, val sideBlock: Block, val postBlock: Block) : FenceBlock(FabricBlockSettings.copyOf(settings)),
    GroupedBlock {

    override fun canConnect(state: BlockState, neighborIsFullSquare: Boolean, dir: Direction): Boolean {
        return !cannotConnect(state) && neighborIsFullSquare || state.run {
            (isIn(BlockTags.FENCES) && isIn(ModBlockTags.modernFences) == this@ModernFenceBlock.defaultState.isIn(
                ModBlockTags.modernFences
            )
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
        .plus(
            arrayOf(
                Block.createCuboidShape(7.5, 0.0, 0.0, 8.5, 15.0, 6.0),
                Block.createCuboidShape(7.0, 11.0, 0.0, 9.0, 14.0, 6.0),
                Block.createCuboidShape(7.0, 2.0, 0.0, 9.0, 5.0, 6.0)
            ).let {
                listOf(if (state.get(NORTH)) it.combined else VoxelShapes.empty(),
                    if (state.get(EAST)) it.rotateLeft() else VoxelShapes.empty(),
                    if (state.get(SOUTH)) it.flip() else VoxelShapes.empty(),
                    if (state.get(WEST)) it.rotateRight() else VoxelShapes.empty()
                )
            }
        )

    override val itemGroup
        get() = ModItemGroups.outsideBlockItemGroup
}


