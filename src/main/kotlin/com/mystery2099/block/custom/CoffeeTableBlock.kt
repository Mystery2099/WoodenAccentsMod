package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
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
import kotlin.collections.HashSet

class CoffeeTableBlock(val topBlock: Block, val legBlock: Block) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(topBlock)) {
    init {
        defaultState = defaultState.apply {
            with(east, false)
            with(south, false)
            with(west, false)
            with(type, CoffeeTableType.SHORT)
        }
        instances += this
        WoodenAccentsModItemGroups.livingRoomItems += this
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
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos!!, neighborPos)?.apply {
            withIfExists(north, world.checkNorth(pos))
            withIfExists(east, world.checkEast(pos))
            withIfExists(south, world.checkSouth(pos))
            withIfExists(west, world.checkWest(pos))
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(north, east, south, west, type)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val isTall: Boolean = state.get(type) == CoffeeTableType.TALL
        val hasNorthConnection = state.get(north)
        val hasSouthConnection = state.get(south)
        val hasEastConnection = state.get(east)
        val hasWestConnection = state.get(west)

        return mutableListOf<VoxelShape>().apply {
            add(if (isTall) tallTopShape else shortTopShape)
            if (!hasNorthConnection) {
                if (!hasEastConnection) {
                    add(SHORT_NORTH_EAST_LEG_SHAPE)
                    if (isTall) add(TALL_NORTH_EAST_LEG_SHAPE)
                }
                if (!hasWestConnection) {
                    add(SHORT_NORTH_WEST_LEG_SHAPE)
                    if (isTall) add(TALL_NORTH_WEST_LEG_SHAPE)
                }
            }
            if (!hasSouthConnection) {
                if (!hasEastConnection) {
                    add(SHORT_SOUTH_EAST_LEG_SHAPE)
                    if (isTall) add(TALL_SOUTH_EAST_LEG_SHAPE)
                }
                if (!hasWestConnection) {
                    add(SHORT_SOUTH_WEST_LEG_SHAPE)
                    if (isTall) add(TALL_SOUTH_WEST_LEG_SHAPE)
                }
            }
        }.reduce(VoxelShapes::union)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val state = ctx.world.getBlockState(ctx.blockPos)
        return if (state.isOf(this)) state.with(type, CoffeeTableType.TALL) else super.getPlacementState(ctx)?.withIfExists(type, CoffeeTableType.SHORT)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "state.get(type) == CoffeeTableType.SHORT && context.stack.item == asItem()",
        "com.mystery2099.block.custom.CoffeeTableBlock.Companion.type",
        "com.mystery2099.block.custom.enums.CoffeeTableType"
    )
    )
    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        return state.get(type) == CoffeeTableType.SHORT && context.stack.item == asItem()
    }

    private fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        val here = this.getBlockState(pos)
        val there = this.getBlockState(pos.offset(direction))
        return there.block is CoffeeTableBlock && here.get(type) == there.get(type)
    }

    private fun WorldAccess.checkNorth(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.NORTH)
    }

    private fun WorldAccess.checkEast(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.EAST)
    }

    private fun WorldAccess.checkSouth(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.SOUTH)
    }

    private fun WorldAccess.checkWest(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.WEST)
    }

    companion object {
        @JvmStatic
        val instances = HashSet<CoffeeTableBlock>()
        @JvmStatic
        val north: BooleanProperty = Properties.NORTH
        @JvmStatic
        val east: BooleanProperty = Properties.EAST
        @JvmStatic
        val south: BooleanProperty = Properties.SOUTH
        @JvmStatic
        val west: BooleanProperty = Properties.WEST
        @JvmStatic
        val type = ModProperties.coffeeTableType

        // Shape offsets
        private const val shapeHorizontalOffset = 13.5 / 16.0
        private const val shapeVerticalOffset = 7.0 / 16

        // Top shapes
        @JvmStatic
        private val shortTopShape = createCuboidShape(0.0, 7.0, 0.0, 16.0, 9.0, 16.0)
        @JvmStatic
        private val tallTopShape = shortTopShape.offset(0.0, shapeVerticalOffset, 0.0)

        // Short North Shapes
        @JvmStatic
        private val SHORT_NORTH_EAST_LEG_SHAPE = createCuboidShape(13.75, 0.0, 0.25, 15.75, 7.0, 2.25)
        @JvmStatic
        private val SHORT_NORTH_WEST_LEG_SHAPE = SHORT_NORTH_EAST_LEG_SHAPE.offset(-shapeHorizontalOffset, 0.0, 0.0)

        // Short South Shapes
        @JvmStatic
        private val SHORT_SOUTH_EAST_LEG_SHAPE = SHORT_NORTH_EAST_LEG_SHAPE.offset(0.0, 0.0, shapeHorizontalOffset)
        @JvmStatic
        private val SHORT_SOUTH_WEST_LEG_SHAPE = SHORT_NORTH_WEST_LEG_SHAPE.offset(0.0, 0.0, shapeHorizontalOffset)

        // Tall North Shapes
        @JvmStatic
        private val TALL_NORTH_EAST_LEG_SHAPE = SHORT_NORTH_EAST_LEG_SHAPE.offset(0.0, shapeVerticalOffset, 0.0)
        @JvmStatic
        private val TALL_NORTH_WEST_LEG_SHAPE = SHORT_NORTH_WEST_LEG_SHAPE.offset(0.0, shapeVerticalOffset, 0.0)

        // Tall South Shapes
        @JvmStatic
        private val TALL_SOUTH_EAST_LEG_SHAPE = SHORT_SOUTH_EAST_LEG_SHAPE.offset(0.0, shapeVerticalOffset, 0.0)
        @JvmStatic
        private val TALL_SOUTH_WEST_LEG_SHAPE = SHORT_SOUTH_WEST_LEG_SHAPE.offset(0.0, shapeVerticalOffset, 0.0)
    }
}
