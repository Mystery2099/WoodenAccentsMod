package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.ModItemGroups
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.item.ItemGroup
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class ThinBookshelfBlock(val baseBlock : Block) : ChiseledBookshelfBlock(FabricBlockSettings.copyOf(baseBlock).requires(FeatureFlags.UPDATE_1_20)),
    GroupedBlock {
    override val itemGroup: ItemGroup
        get() = ModItemGroups.livingRoomItemGroup

    companion object {
        private val northShape = Block.createCuboidShape(0.0, 0.0, 11.0, 16.0, 16.0, 16.0)!!
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = when (state.get(HorizontalFacingBlock.FACING)) {
            Direction.NORTH -> northShape
            Direction.EAST -> northShape.rotateLeft()
            Direction.SOUTH -> northShape.flip()
            Direction.WEST -> northShape.rotateRight()
            else -> super.getOutlineShape(state, world, pos, context)
        }


}