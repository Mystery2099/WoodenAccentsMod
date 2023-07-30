package com.mystery2099.block.custom

import com.mystery2099.util.VoxelShapeHelper.union
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

abstract class AbstractPillarBlock(val baseBlock: Block, private val size: Size) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)) {

    init {
        defaultState = defaultState.with(up, false).with(down, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(up, down)
    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState? {
        val newState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        if (newState != null) return newState.with(up, world.checkUp(pos!!)).with(down, world.checkDown(pos))
        return Blocks.AIR.defaultState
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = size.run {
        this.centerShape.union(
            if (!state.get(up)) this.topShape else VoxelShapes.empty()
        ).union(
            if (!state.get(down)) this.baseShape else VoxelShapes.empty()
        )
    }


    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return super.getPlacementState(ctx)?.apply {
            val world = ctx.world
            val pos = ctx.blockPos
            with(up, world.checkUp(pos))?.with(down, world.checkDown(pos))
        }
    }
    //Up & Down
    open fun WorldAccess.getStateAtPos(blockPos: BlockPos): BlockState = getBlockState(blockPos)
    open fun WorldAccess.getUpState(pos: BlockPos): BlockState = getStateAtPos(pos.up())

    open fun WorldAccess.getDownState(pos: BlockPos): BlockState = getStateAtPos(pos.down())

    abstract fun WorldAccess.checkUp(pos: BlockPos): Boolean
    abstract fun WorldAccess.checkDown(pos: BlockPos): Boolean


    @JvmRecord
    data class Size(val topShape: VoxelShape, val centerShape: VoxelShape, val baseShape: VoxelShape)
    companion object {
        @JvmStatic
        val up: BooleanProperty = Properties.UP!!
        @JvmStatic
        val down: BooleanProperty = Properties.DOWN!!
    }
}