package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.datagen.BlockLootTableDataGen.Companion.dropsSelf
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.block.enums.StairShape
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.*

open class KitchenCounterBlock(val baseBlock: Block, val topBlock: Block, settings: Settings) :
    AbstractWaterloggableBlock(settings) {

    init {
        defaultState =
            stateManager.defaultState.with(
                FACING,
                Direction.NORTH
            ).with(SHAPE, StairShape.STRAIGHT)
        this.dropsSelf()
        WoodenAccentsModItemGroups.kitchenItems.add(this)
        INSTANCES.add(this)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        //southShapes
        val southInnerLeftShape: VoxelShape =
            VoxelShapes.union(SOUTH_SHAPE, createCuboidShape(2.0, 0.0, 0.0, 16.0, 14.0, 2.0))
        val southInnerRightShape: VoxelShape =
            VoxelShapes.union(SOUTH_SHAPE, createCuboidShape(0.0, 0.0, 0.0, 14.0, 14.0, 2.0))
        val southOuterLeftShape: VoxelShape = createCuboidShape(2.0, 0.0, 2.0, 16.0, 14.0, 16.0)
        val southOuterRightShape: VoxelShape = createCuboidShape(0.0, 0.0, 2.0, 14.0, 14.0, 16.0)

        //northShapes
        val northInnerLeftShape: VoxelShape =
            VoxelShapes.union(NORTH_SHAPE, createCuboidShape(0.0, 0.0, 14.0, 14.0, 14.0, 16.0))
        val northInnerRightShape: VoxelShape =
            VoxelShapes.union(NORTH_SHAPE, createCuboidShape(2.0, 0.0, 14.0, 16.0, 14.0, 16.0))
        val northOuterLeftShape: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 14.0, 14.0, 14.0)
        val northOuterRightShape: VoxelShape = createCuboidShape(2.0, 0.0, 0.0, 16.0, 14.0, 14.0)
        val stairShape: StairShape = state.get<StairShape>(SHAPE)
        return VoxelShapes.union(
            TOP_SHAPE, when (state.get<Direction>(FACING)) {
                Direction.NORTH -> when (stairShape) {
                    StairShape.STRAIGHT -> NORTH_SHAPE
                    StairShape.INNER_LEFT -> northInnerLeftShape
                    StairShape.INNER_RIGHT -> northInnerRightShape
                    StairShape.OUTER_LEFT -> northOuterLeftShape
                    StairShape.OUTER_RIGHT -> northOuterRightShape
                }

                Direction.SOUTH -> when (stairShape) {
                    StairShape.STRAIGHT -> SOUTH_SHAPE
                    StairShape.INNER_LEFT -> southInnerLeftShape
                    StairShape.INNER_RIGHT -> southInnerRightShape
                    StairShape.OUTER_LEFT -> southOuterLeftShape
                    StairShape.OUTER_RIGHT -> southOuterRightShape
                }

                Direction.EAST -> when (stairShape) {
                    StairShape.STRAIGHT -> EAST_SHAPE
                    StairShape.INNER_LEFT -> northInnerRightShape
                    StairShape.INNER_RIGHT -> southInnerLeftShape
                    StairShape.OUTER_LEFT -> northOuterRightShape
                    StairShape.OUTER_RIGHT -> southOuterLeftShape
                }

                Direction.WEST -> when (stairShape) {
                    StairShape.STRAIGHT -> WEST_SHAPE
                    StairShape.INNER_LEFT -> southInnerRightShape
                    StairShape.INNER_RIGHT -> northInnerLeftShape
                    StairShape.OUTER_LEFT -> southOuterRightShape
                    StairShape.OUTER_RIGHT -> northOuterLeftShape
                }

                else -> VoxelShapes.fullCube()
            }
        )
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, SHAPE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val blockPos: BlockPos = ctx.blockPos
        val blockState: BlockState = Objects.requireNonNull(super.getPlacementState(ctx))!!.with<Direction, Direction>(
            FACING, ctx.horizontalPlayerFacing
        )
        return blockState.with<StairShape, StairShape>(SHAPE, getCounterShape(blockState, ctx.getWorld(), blockPos))
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
        val stateForNeighborUpdate: BlockState? =
            super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        return if (direction!!.axis.isHorizontal) {
            stateForNeighborUpdate?.withIfExists(
                SHAPE,
                pos?.let { getCounterShape(state, world, it) }
            )
        } else stateForNeighborUpdate
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "state.with<Direction, Direction>(FACING, rotation.rotate(state.get<Direction>(FACING)))",
        "net.minecraft.util.math.Direction",
        "net.minecraft.util.math.Direction",
        "com.mystery2099.block.custom.KitchenCounterBlock.Companion.FACING",
        "net.minecraft.util.math.Direction",
        "com.mystery2099.block.custom.KitchenCounterBlock.Companion.FACING"
    )
    )
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    @Deprecated("Deprecated in Java")
    override fun mirror(state: BlockState, mirror: BlockMirror?): BlockState {
        state.get(FACING)
        val stairShape = state.get(SHAPE)
        when (mirror) {
            BlockMirror.LEFT_RIGHT -> {
                return when (stairShape) {
                    StairShape.INNER_LEFT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT)
                    }

                    StairShape.INNER_RIGHT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT)
                    }

                    StairShape.OUTER_LEFT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_RIGHT)
                    }

                    StairShape.OUTER_RIGHT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_LEFT)
                    }
                    else -> {
                        state.rotate(BlockRotation.CLOCKWISE_180)
                    }
                }
            }

            BlockMirror.FRONT_BACK -> {
                return when (stairShape) {
                    StairShape.INNER_LEFT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT)
                    }

                    StairShape.INNER_RIGHT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT)
                    }

                    StairShape.OUTER_LEFT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_RIGHT)
                    }

                    StairShape.OUTER_RIGHT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_LEFT)
                    }

                    StairShape.STRAIGHT -> {
                        state.rotate(BlockRotation.CLOCKWISE_180)
                    }
                    else -> {
                        return super.mirror(state, mirror)
                    }
                }
            }
            else -> {return super.mirror(state, mirror)}
        }
    }

    companion object {
        val INSTANCES: MutableSet<KitchenCounterBlock> = HashSet()
        val SHAPE: EnumProperty<StairShape> = Properties.STAIR_SHAPE
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        protected val TOP_SHAPE: VoxelShape = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
        protected val SOUTH_SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 2.0, 16.0, 14.0, 16.0)
        protected val EAST_SHAPE: VoxelShape = createCuboidShape(2.0, 0.0, 0.0, 16.0, 14.0, 16.0)
        private const val SHAPE_OFFSET = -(2.0 / 16)
        protected val NORTH_SHAPE: VoxelShape = SOUTH_SHAPE.offset(0.0, 0.0, SHAPE_OFFSET)
        protected val WEST_SHAPE: VoxelShape = EAST_SHAPE.offset(SHAPE_OFFSET, 0.0, 0.0)

        private fun getCounterShape(state: BlockState, world: BlockView, pos: BlockPos): StairShape {
            val direction3: Direction
            val direction2: Direction
            val direction = state.get(FACING)
            val blockState = world.getBlockState(pos.offset(direction))
            direction2 = blockState.get(FACING)
            if (canConnectTo(blockState) && direction2.axis !== state.get(FACING).axis &&
                isDifferentOrientation(state, world, pos, direction2.opposite)
            ) {
                return if (direction2 == direction.rotateYCounterclockwise()) StairShape.OUTER_LEFT else StairShape.OUTER_RIGHT
            }
            val blockState2 = world.getBlockState(pos.offset(direction.opposite))
            direction3 = blockState2.get(FACING)
            return if (canConnectTo(blockState2) && direction3.axis !== state.get(FACING).axis &&
                isDifferentOrientation(state, world, pos, direction3)
            ) {
                if (direction3 == direction.rotateYCounterclockwise()) StairShape.INNER_LEFT else StairShape.INNER_RIGHT
            } else StairShape.STRAIGHT
        }

        private fun canConnectTo(blockState: BlockState): Boolean {
            return blockState.block is KitchenCounterBlock // || blockState.getBlock() is KitchenCabinetBlock
        }

        private fun isDifferentOrientation(
            state: BlockState,
            world: BlockView,
            pos: BlockPos?,
            dir: Direction
        ): Boolean {
            val blockState: BlockState = world.getBlockState(pos?.offset(dir))
            return !canConnectTo(blockState) || blockState.get(FACING) != state.get(FACING)
        }
    }
}
