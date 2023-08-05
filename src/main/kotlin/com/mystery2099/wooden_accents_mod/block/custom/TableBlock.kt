package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class TableBlock(val baseBlock: Block, val topBlock: Block) : AbstractTableBlock(baseBlock, topBlock) {
    //init { WoodenAccentsModItemGroups.livingRoomItems += this }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = state.run {
        val north = this[north]
        val east = this[east]
        val south = this[south]
        val west = this[west]
        mutableListOf<VoxelShape>().apply {
            add(topShape)
            if (!north && !east && !south && !west) add(legShape)
            //Ends
            if (!north && !east && south && !west) add(northEndLegShape)
            if (!north && !east && !south && west) add(eastEndLegShape)
            if (north && !east && !south && !west) add(southEndLegShape)
            if (!north && east && !south && !west) add(westEndLegShape)
            //Corners
            if (!north && !east && south && west) add(northEastLegShape)
            if (!north && east && south && !west) add(northWestLegShape)
            if (north && !east && !south && west) add(southEastLegShape)
            if (north && east && !south && !west) add(southWestLegShape)

        }.combined
    }

    override val itemGroup: ItemGroup
        get() = ModItemGroups.kitchenItemGroup

    override fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        return getBlockState(pos.offset(direction)).block is TableBlock
    }

    companion object {
        @JvmStatic
        val topShape: VoxelShape = createCuboidShape(0.0, 13.0, 0.0, 16.0, 16.0, 16.0)
        @JvmStatic
        val legShape: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 13.0, 10.0)
        @JvmStatic
        val northEndLegShape: VoxelShape = createCuboidShape(6.0, 0.0, 1.0, 10.0, 13.0, 5.0)
        @JvmStatic
        val eastEndLegShape: VoxelShape = northEndLegShape.rotateLeft()
        @JvmStatic
        val southEndLegShape : VoxelShape = northEndLegShape.flip()
        @JvmStatic
        val westEndLegShape: VoxelShape = northEndLegShape.rotateRight()
        @JvmStatic
        val northEastLegShape: VoxelShape = createCuboidShape(11.0, 0.0, 1.0, 15.0, 13.0, 5.0)
        @JvmStatic
        val northWestLegShape: VoxelShape = northEastLegShape.rotateRight()
        @JvmStatic
        val southEastLegShape: VoxelShape = northWestLegShape.flip()
        @JvmStatic
        val southWestLegShape: VoxelShape = northEastLegShape.flip()
    }
}