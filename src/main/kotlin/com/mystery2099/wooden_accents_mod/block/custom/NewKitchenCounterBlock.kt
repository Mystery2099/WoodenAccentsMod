package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.kitchenCounters
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.plus
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.enums.StairShape
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.tag.TagKey
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
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class NewKitchenCounterBlock(val baseBlock: Block, val topBlock: Block) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)), GroupedBlock, TaggedBlock {
    override val tag: TagKey<Block>
        get() = ModBlockTags.kitchenCounters
    override val itemGroup
        get() = ModItemGroups.kitchenItemGroup

    init {

        defaultState = defaultState.apply {
            with(facing, Direction.NORTH)
            with(shape, StairShape.STRAIGHT)
        }
    }

    @Suppress("deprecation")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ) = state[shape].let { stairShape ->
        topShape + (
                when (state[facing]) {
                    Direction.NORTH -> when (stairShape) {
                        StairShape.INNER_LEFT -> northWestInnerShape
                        StairShape.INNER_RIGHT -> northEastInnerShape
                        StairShape.OUTER_LEFT -> northWestOuterShape
                        StairShape.OUTER_RIGHT -> northEastOuterShape
                        else -> northShape
                    }

                    Direction.SOUTH -> when (stairShape) {
                        StairShape.INNER_LEFT -> southEastInnerShape
                        StairShape.INNER_RIGHT -> southWestInnerShape
                        StairShape.OUTER_LEFT -> southEastOuterShape
                        StairShape.OUTER_RIGHT -> southWestOuterShape
                        else -> southShape
                    }

                    Direction.EAST -> when (stairShape) {
                        StairShape.INNER_LEFT -> northEastInnerShape
                        StairShape.INNER_RIGHT -> southEastInnerShape
                        StairShape.OUTER_LEFT -> northEastOuterShape
                        StairShape.OUTER_RIGHT -> southEastOuterShape
                        else -> eastShape
                    }

                    Direction.WEST -> when (stairShape) {
                        StairShape.INNER_LEFT -> southWestInnerShape
                        StairShape.INNER_RIGHT -> northWestInnerShape
                        StairShape.OUTER_LEFT -> southWestOuterShape
                        StairShape.OUTER_RIGHT -> northWestOuterShape
                        else -> westShape
                    }

                    else -> VoxelShapes.empty()
                }
                )
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(facing, shape)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = super.placementState(ctx).run {
        with(facing, ctx.horizontalPlayerFacing.opposite).with(shape, getCounterShape(this, ctx.world, ctx.blockPos))
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
        return super.getStateForNeighborUpdate(
            state, direction, neighborState,
            world, pos, neighborPos
        )?.apply {
            if (direction != null && direction.axis == Direction.Axis.Y) {
                with(shape, pos?.let { getCounterShape(state, world, it) })
            }
        }
    }

    @Suppress("deprecation")
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(facing, rotation.rotate(state[facing]))
    }

    @Suppress("deprecation")
    override fun mirror(state: BlockState, mirror: BlockMirror?): BlockState {
        val direction = state[facing]
        state[shape].let { stairShape ->
            return when (mirror) {
                BlockMirror.LEFT_RIGHT -> {
                    if (direction.axis !== Direction.Axis.Z) return super.mirror(state, mirror)
                    return when (stairShape) {
                        StairShape.INNER_LEFT -> state.rotate(BlockRotation.CLOCKWISE_180)
                            .with(shape, StairShape.INNER_RIGHT)

                        StairShape.INNER_RIGHT -> state.rotate(BlockRotation.CLOCKWISE_180)
                            .with(shape, StairShape.INNER_LEFT)

                        StairShape.OUTER_LEFT -> state.rotate(BlockRotation.CLOCKWISE_180)
                            .with(shape, StairShape.OUTER_RIGHT)

                        StairShape.OUTER_RIGHT -> state.rotate(BlockRotation.CLOCKWISE_180)
                            .with(shape, StairShape.OUTER_LEFT)

                        else -> state.rotate(BlockRotation.CLOCKWISE_180)
                    }
                }

                BlockMirror.FRONT_BACK -> {
                    if (direction.axis !== Direction.Axis.X) return super.mirror(state, mirror)
                    return when (stairShape) {
                        StairShape.INNER_LEFT -> state.rotate(BlockRotation.CLOCKWISE_180)
                            .with(shape, StairShape.INNER_LEFT)

                        StairShape.INNER_RIGHT -> state.rotate(BlockRotation.CLOCKWISE_180)
                            .with(shape, StairShape.INNER_RIGHT)

                        StairShape.OUTER_LEFT -> state.rotate(BlockRotation.CLOCKWISE_180)
                            .with(shape, StairShape.OUTER_RIGHT)

                        StairShape.OUTER_RIGHT -> state.rotate(BlockRotation.CLOCKWISE_180)
                            .with(shape, StairShape.OUTER_LEFT)

                        else -> state.rotate(BlockRotation.CLOCKWISE_180)

                    }
                }

                else -> super.mirror(state, mirror)
            }

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos?, oldState: BlockState?, notify: Boolean) {
        if (state.isOf(state.block)) return
        world.updateNeighbor(baseBlock.defaultState, pos, Blocks.AIR, pos, false)
        baseBlock.onBlockAdded(baseBlock.defaultState, world, pos, oldState, false)
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World?,
        pos: BlockPos?,
        newState: BlockState,
        moved: Boolean
    ) {
        if (state.isOf(newState.block)) return
        baseBlock.defaultState.onStateReplaced(world, pos, newState, moved)
    }


    companion object {
        val shape: EnumProperty<StairShape> = Properties.STAIR_SHAPE
        val facing: DirectionProperty = HorizontalFacingBlock.FACING
        val topShape = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)

        //Straight
        val northShape: VoxelShape = createCuboidShape(0.0, 0.0, 2.0, 16.0, 14.0, 16.0)
        val eastShape: VoxelShape = northShape.rotateLeft()
        val southShape: VoxelShape = northShape.flip()
        val westShape: VoxelShape = northShape.rotateRight()

        //Inner Corners
        private val northEastShapes = arrayOf(northShape, eastShape)
        private val northEastInnerShape = northEastShapes.combined
        private val northWestInnerShape = northEastShapes.rotateRight()
        private val southEastInnerShape = northEastShapes.rotateLeft()
        private val southWestInnerShape = northEastShapes.flip()


        //Outer Corners
        private val northEastOuterShape = createCuboidShape(0.0, 0.0, 2.0, 14.0, 14.0, 16.0)
        private val northWestOuterShape = northEastOuterShape.rotateRight()
        private val southEastOuterShape = northEastOuterShape.rotateLeft()
        private val southWestOuterShape = northWestOuterShape.flip()

        private fun getCounterShape(state: BlockState, world: BlockView, pos: BlockPos): StairShape {
            lateinit var direction3: Direction
            lateinit var direction2: Direction
            val direction = state[facing]
            val blockState = world.getBlockState(pos.offset(direction.opposite))
            if (blockState.isCounter() && blockState.get(facing).also {
                    direction2 = it
                }.axis !== state[facing].axis && isDifferentOrientation(
                    state,
                    world,
                    pos,
                    direction2
                )
            ) {
                return when (direction2) {
                    direction.rotateYCounterclockwise() -> StairShape.OUTER_LEFT
                    else -> StairShape.OUTER_RIGHT
                }
            }
            val blockState2 = world.getBlockState(pos.offset(direction))
            return if (blockState2.isCounter() && blockState2[facing].also {
                    direction3 = it
                }.axis !== state[facing].axis && isDifferentOrientation(
                    state,
                    world,
                    pos,
                    direction3.opposite
                )
            ) {
                when (direction3) {
                    direction.rotateYCounterclockwise() -> StairShape.INNER_LEFT
                    else -> StairShape.INNER_RIGHT
                }
            } else StairShape.STRAIGHT
        }

        private fun BlockState.isCounter() = canConnectToThis()
        private fun BlockState.canConnectToThis() = isIn(kitchenCounters)

        private fun isDifferentOrientation(
            state: BlockState,
            world: BlockView,
            pos: BlockPos,
            dir: Direction
        ): Boolean {
            return world.getBlockState(pos.offset(dir)).run {
                !canConnectToThis() || get(facing) != state.get(facing)
            }
        }
    }
}


