package com.mystery2099.block.custom

import com.mystery2099.datagen.BlockLootTableDataGen.Companion.dropsSelf
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class TableBlock(val baseBlock: Block, val topBlock: Block) : AbstractTableBlock(baseBlock, topBlock) {
    init {
        instances += this
        this.dropsSelf()
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val north = state?.get(north) ?: false
        val east = state?.get(east) ?: false
        val south = state?.get(south) ?: false
        val west = state?.get(west) ?: false

        return mutableListOf<VoxelShape>().apply {
            add(TOP_SHAPE)
            if (!north && !east && !south && !west) add(LEG_SHAPE)
            if (!north && !east && south && west) add(NORTH_EAST_LEG_SHAPE)
            if (!north && east && south && !west) add(NORTH_WEST_LEG_SHAPE)
            if (north && !east && !south && west) add(SOUTH_EAST_LEG_SHAPE)
            if (north && east && !south && !west) add(SOUTH_WEST_LEG_SHAPE)
        }.reduce(VoxelShapes::union)
    }

    override fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        return this.getBlockState(pos.offset(direction)).block is TableBlock
    }

    companion object {
        @JvmStatic
        val instances = HashSet<TableBlock>()

        @JvmStatic
        val TOP_SHAPE: VoxelShape = createCuboidShape(0.0, 13.0, 0.0, 16.0, 16.0, 16.0)
        @JvmStatic
        val LEG_SHAPE: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 13.0, 10.0)
        @JvmStatic
        val NORTH_EAST_LEG_SHAPE: VoxelShape = createCuboidShape(11.0, 0.0, 1.0, 15.0, 13.0, 5.0)
        @JvmStatic
        val NORTH_WEST_LEG_SHAPE: VoxelShape = createCuboidShape(1.0, 0.0, 1.0, 5.0, 13.0, 5.0)
        @JvmStatic
        val SOUTH_EAST_LEG_SHAPE: VoxelShape = createCuboidShape(11.0, 0.0, 11.0, 15.0, 13.0, 15.0)
        @JvmStatic
        val SOUTH_WEST_LEG_SHAPE: VoxelShape = createCuboidShape(1.0, 0.0, 11.0, 5.0, 13.0, 15.0)
    }
}