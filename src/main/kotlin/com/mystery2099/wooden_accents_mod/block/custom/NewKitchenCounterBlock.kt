package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.isIn
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.kitchenCounters
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.group
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.enums.StairShape
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemPlacementContext
import net.minecraft.recipe.book.RecipeCategory
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
import java.util.function.Consumer

class NewKitchenCounterBlock(val baseBlock: Block, val topBlock: Block) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)), GroupedBlock, TaggedBlock, RecipeBlockData {

    override val itemGroup: CustomItemGroup
        get() = ModItemGroups.kitchenItemGroup

    override val tag: TagKey<Block>
        get() = ModBlockTags.kitchenCounters


    init {
        defaultState = defaultState.with(FACING, Direction.NORTH).with(SHAPE, StairShape.STRAIGHT)
    }


    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        val stairShape = state[SHAPE]
        return VoxelShapes.union(
            TOP_SHAPE, when (state[FACING]) {
                Direction.NORTH -> when (stairShape) {
                    StairShape.STRAIGHT -> NORTH_SHAPE
                    StairShape.INNER_LEFT -> NORTH_WEST_INNER
                    StairShape.INNER_RIGHT -> NORTH_EAST_INNER
                    StairShape.OUTER_LEFT -> NORTH_WEST_OUTER
                    StairShape.OUTER_RIGHT -> NORTH_EAST_OUTER
                    else -> VoxelShapes.fullCube()
                }

                Direction.SOUTH -> when (stairShape) {
                    StairShape.STRAIGHT -> SOUTH_SHAPE
                    StairShape.INNER_LEFT -> SOUTH_EAST_INNER
                    StairShape.INNER_RIGHT -> SOUTH_WEST_INNER
                    StairShape.OUTER_LEFT -> SOUTH_EAST_OUTER
                    StairShape.OUTER_RIGHT -> SOUTH_WEST_OUTER
                    else -> VoxelShapes.fullCube()
                }

                Direction.EAST -> when (stairShape) {
                    StairShape.STRAIGHT -> EAST_SHAPE
                    StairShape.INNER_LEFT -> NORTH_EAST_INNER
                    StairShape.INNER_RIGHT -> SOUTH_EAST_INNER
                    StairShape.OUTER_LEFT -> NORTH_EAST_OUTER
                    StairShape.OUTER_RIGHT -> SOUTH_EAST_OUTER
                    else -> VoxelShapes.fullCube()
                }

                Direction.WEST -> when (stairShape) {
                    StairShape.STRAIGHT -> WEST_SHAPE
                    StairShape.INNER_LEFT -> SOUTH_WEST_INNER
                    StairShape.INNER_RIGHT -> NORTH_WEST_INNER
                    StairShape.OUTER_LEFT -> SOUTH_WEST_OUTER
                    StairShape.OUTER_RIGHT -> NORTH_WEST_OUTER
                    else -> VoxelShapes.fullCube()
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
        return super.getPlacementState(ctx)?.run {
            with(FACING, ctx.horizontalPlayerFacing.opposite)
            .with(SHAPE, getCounterShape(this, ctx.world, ctx.blockPos))
        }

    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState {
        var stateForNeighborUpdate =
            super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        if (direction!!.axis.isHorizontal) {
            stateForNeighborUpdate = stateForNeighborUpdate?.withIfExists(SHAPE, getCounterShape(state, world, pos!!))
                ?: stateForNeighborUpdate
        }
        return stateForNeighborUpdate!!
    }

    @Deprecated("Deprecated in Java")
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state[FACING]))
    }

    @Deprecated("Deprecated in Java")
    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        val direction = state[FACING]
        val stairShape = state[SHAPE]
        return when (mirror) {
            BlockMirror.LEFT_RIGHT -> {
                if (direction.axis != Direction.Axis.Z) {
                    return state.rotate(BlockRotation.CLOCKWISE_180)
                }
                when (stairShape) {
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

                    else -> state.rotate(BlockRotation.CLOCKWISE_180)
                }
            }

            BlockMirror.FRONT_BACK -> {
                if (direction.axis != Direction.Axis.X) {
                    return state.rotate(BlockRotation.CLOCKWISE_180)
                }
                when (stairShape) {
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

                    else -> state
                }
            }

            else -> state
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (state.isOf(state.block)) return
        world.updateNeighbor(this.baseBlock.defaultState, pos, Blocks.AIR, pos, false)
        this.baseBlock.onBlockAdded(this.baseBlock.defaultState, world, pos, oldState, false)
    }

    @Deprecated("Deprecated in Java")
    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        if (state.isOf(newState.block)) return
        this.baseBlock.defaultState.onStateReplaced(world, pos, newState, moved)
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', baseBlock)
            input('_', topBlock)
            pattern("___")
            pattern("###")
            pattern("###")
            group(this@NewKitchenCounterBlock, "kitchen_counters")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

    companion object {
        val SHAPE: EnumProperty<StairShape> = Properties.STAIR_SHAPE
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        val TOP_SHAPE: VoxelShape = Block.createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)

        // Straight
        val NORTH_SHAPE: VoxelShape = Block.createCuboidShape(0.0, 0.0, 2.0, 16.0, 14.0, 16.0)
        val EAST_SHAPE: VoxelShape = Block.createCuboidShape(0.0, 0.0, 0.0, 14.0, 14.0, 16.0)
        val SOUTH_SHAPE: VoxelShape = NORTH_SHAPE.offset(0.0, 0.0, -2.0 / 16.0)
        val WEST_SHAPE: VoxelShape = EAST_SHAPE.offset(2.0 / 16.0, 0.0, 0.0)

        // Inner Corners
        private val NORTH_WEST_INNER: VoxelShape = VoxelShapes.union(NORTH_SHAPE, WEST_SHAPE)
        private val SOUTH_WEST_INNER: VoxelShape = VoxelShapes.union(SOUTH_SHAPE, WEST_SHAPE)
        private val SOUTH_EAST_INNER: VoxelShape = VoxelShapes.union(SOUTH_SHAPE, EAST_SHAPE)
        private val NORTH_EAST_INNER: VoxelShape = VoxelShapes.union(NORTH_SHAPE, EAST_SHAPE)

        // Outer Corners
        private val NORTH_EAST_OUTER: VoxelShape = Block.createCuboidShape(0.0, 0.0, 2.0, 14.0, 14.0, 16.0)
        private val NORTH_WEST_OUTER: VoxelShape = NORTH_EAST_OUTER.offset(2.0 / 16.0, 0.0, 0.0)
        private val SOUTH_EAST_OUTER: VoxelShape = NORTH_EAST_OUTER.offset(0.0, 0.0, -2.0 / 16.0)
        private val SOUTH_WEST_OUTER: VoxelShape = NORTH_WEST_OUTER.offset(0.0, 0.0, -2.0 / 16.0)

        private fun getCounterShape(state: BlockState, world: BlockView, pos: BlockPos): StairShape {
            lateinit var direction3: Direction
            lateinit var direction2: Direction
            val direction = state.get(FACING)
            val blockState = world.getBlockState(pos.offset(direction.opposite))
            if (blockState.isCounter() && blockState.get(FACING).also {
                    direction2 = it
                }.axis !== state.get(FACING).axis && isDifferentOrientation(
                    state,
                    world,
                    pos,
                    direction2
                )
            ) {
                return if (direction2 == direction.rotateYCounterclockwise()) {
                    StairShape.OUTER_LEFT
                } else StairShape.OUTER_RIGHT
            }
            val blockState2 = world.getBlockState(pos.offset(direction))
            if (blockState2.isCounter() && blockState2.get(FACING).also {
                    direction3 = it
                }.axis !== state.get(FACING).axis && isDifferentOrientation(
                    state,
                    world,
                    pos,
                    direction3.opposite
                )
            ) {
                return if (direction3 == direction.rotateYCounterclockwise()) {
                    StairShape.INNER_LEFT
                } else StairShape.INNER_RIGHT
            }
            return StairShape.STRAIGHT
        }

        private fun BlockState.isCounter(): Boolean = this isIn kitchenCounters


        private fun isDifferentOrientation(
            state: BlockState,
            world: BlockView,
            pos: BlockPos,
            dir: Direction
        ): Boolean = world.getBlockState(pos.offset(dir)).run{
            !this.isCounter() || this[FACING] != state[FACING]
        }
    }


}


