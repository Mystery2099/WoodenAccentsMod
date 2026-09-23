package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.with
import com.github.mystery2099.woodenAccentsMod.block.BlockStateUtil.isIn
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.SidewaysConnectionShape
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.withYRotationOf
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.state.property.ModProperties
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.data.models.*
import net.minecraft.data.models.blockstates.*
import net.minecraft.data.models.model.*
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockBehaviour

class ConnectingLadderBlock(val baseBlock: Block) :
    AbstractCustomLadderBlock(BlockBehaviour.Properties.of().apply {
        mapColor(baseBlock.defaultMapColor())
        destroyTime(Blocks.LADDER.defaultDestroyTime())
        explosionResistance(Blocks.LADDER.explosionResistance)
        sound(baseBlock.defaultBlockState().soundType)
        instrument(baseBlock.defaultBlockState().instrument())
        if (baseBlock.defaultBlockState().ignitedByLava()) ignitedByLava()
    }) {
    override val tag: TagKey<Block> = ModBlockTags.connectingLadders

    init {
        registerDefaultState(defaultBlockState().withShape(left = false, right = false))
    }

    @Deprecated("Deprecated in Java")
    override fun updateShape(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        val updatedState = super.updateShape(state, direction, neighborState, world, pos, neighborPos)
        if (!updatedState.`is`(this)) return updatedState
        val leftState = world.getBlockState(pos.relative(state.getValue(FACING).getClockWise()))
        val rightState = world.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise()))
        return updatedState.withShape(
                left = state.canConnectTo(leftState),
                right = state.canConnectTo(rightState)
            )
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val state = super.getStateForPlacement(ctx) ?: return null
        return state.withShape(
            left = state.canConnectTo(ctx.level.getBlockState(ctx.clickedPos.relative(state.getValue(FACING).getClockWise()))),
            right = state.canConnectTo(ctx.level.getBlockState(ctx.clickedPos.relative(state.getValue(FACING).getCounterClockWise())))
        )
    }

    private fun BlockState.isConnectingLadder(): Boolean = this isIn tag || this.block is ConnectingLadderBlock

    private fun BlockState.canConnectTo(other: BlockState): Boolean {
        return this.isConnectingLadder() && other.isConnectingLadder() && this.getValue(FACING) == other.getValue(FACING)
    }

    private fun BlockState.withShape(left: Boolean, right: Boolean): BlockState = this.with {
        shape to when {
            left && right -> SidewaysConnectionShape.CENTER
            left -> SidewaysConnectionShape.RIGHT
            right -> SidewaysConnectionShape.LEFT
            else -> SidewaysConnectionShape.SINGLE
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(shape)
    }

    @Deprecated("Deprecated in Java")
    override fun getShape(
        state: BlockState,
        world: BlockGetter?,
        pos: BlockPos?,
        context: CollisionContext?
    ): VoxelShape =
        shapeMap[state.getValue(shape)]?.get(state.getValue(FACING)) ?: super.getShape(state, world, pos, context)

    override fun offerRecipeTo(recipeExporter: RecipeOutput) {
        super.offerRecipe(recipeExporter, baseBlock, 8, "connecting_ladder")
    }

    override fun generateBlockStateModels(generator: BlockModelGenerators) {
        val textureMap = TextureMapping.cube(this.baseBlock)
        val singleModel = ModModels.connectingLadder.create(this, textureMap, generator.modelOutput)
        val leftModel = ModModels.connectingLadderLeft.create(this, textureMap, generator.modelOutput)
        val centerModel = ModModels.connectingLadderCenter.create(this, textureMap, generator.modelOutput)
        val rightModel = ModModels.connectingLadderRight.create(this, textureMap, generator.modelOutput)
        generator.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(this).with(
                PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, ModProperties.sidewaysConnectionShape).apply {
                    val northSingleVariant = singleModel.asBlockStateVariant()
                    val northLeftVariant = leftModel.asBlockStateVariant()
                    val northCenterVariant = centerModel.asBlockStateVariant()
                    val northRightVariant = rightModel.asBlockStateVariant()

                    select(Direction.NORTH, SidewaysConnectionShape.SINGLE, northSingleVariant)
                    select(Direction.NORTH, SidewaysConnectionShape.LEFT, northLeftVariant)
                    select(Direction.NORTH, SidewaysConnectionShape.CENTER, northCenterVariant)
                    select(Direction.NORTH, SidewaysConnectionShape.RIGHT, northRightVariant)

                    select(
                        Direction.EAST, SidewaysConnectionShape.SINGLE, northSingleVariant.withYRotationOf(
                            VariantProperties.Rotation.R90
                        )
                    )
                    select(
                        Direction.EAST, SidewaysConnectionShape.LEFT, northLeftVariant.withYRotationOf(
                            VariantProperties.Rotation.R90
                        )
                    )
                    select(
                        Direction.EAST, SidewaysConnectionShape.CENTER, northCenterVariant.withYRotationOf(
                            VariantProperties.Rotation.R90
                        )
                    )
                    select(
                        Direction.EAST, SidewaysConnectionShape.RIGHT, northRightVariant.withYRotationOf(
                            VariantProperties.Rotation.R90
                        )
                    )

                    select(
                        Direction.SOUTH, SidewaysConnectionShape.SINGLE, northSingleVariant.withYRotationOf(
                            VariantProperties.Rotation.R180
                        )
                    )
                    select(
                        Direction.SOUTH, SidewaysConnectionShape.LEFT, northLeftVariant.withYRotationOf(
                            VariantProperties.Rotation.R180
                        )
                    )
                    select(
                        Direction.SOUTH, SidewaysConnectionShape.CENTER, northCenterVariant.withYRotationOf(
                            VariantProperties.Rotation.R180
                        )
                    )
                    select(
                        Direction.SOUTH, SidewaysConnectionShape.RIGHT, northRightVariant.withYRotationOf(
                            VariantProperties.Rotation.R180
                        )
                    )

                    select(
                        Direction.WEST, SidewaysConnectionShape.SINGLE, northSingleVariant.withYRotationOf(
                            VariantProperties.Rotation.R270
                        )
                    )
                    select(
                        Direction.WEST, SidewaysConnectionShape.LEFT, northLeftVariant.withYRotationOf(
                            VariantProperties.Rotation.R270
                        )
                    )
                    select(
                        Direction.WEST, SidewaysConnectionShape.CENTER, northCenterVariant.withYRotationOf(
                            VariantProperties.Rotation.R270
                        )
                    )
                    select(
                        Direction.WEST, SidewaysConnectionShape.RIGHT, northRightVariant.withYRotationOf(
                            VariantProperties.Rotation.R270
                        )
                    )

                }
            )
        )
    }

    companion object {
        val shape = ModProperties.sidewaysConnectionShape

        private val singleShape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(2, 0, 15, 4, 16, 16),
            VoxelAssembly.createCuboidShape(12, 0, 15, 14, 16, 16),
            VoxelAssembly.createCuboidShape(2, 1, 14.5, 14, 15, 15)
        )

        private val leftShape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(12, 0, 15, 14, 16, 16),
            VoxelAssembly.createCuboidShape(0, 1, 14.5, 14, 15, 15)
        )

        private val rightShape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(2, 0, 15, 4, 16, 16),
            VoxelAssembly.createCuboidShape(2, 1, 14.5, 16, 15, 15)
        )

        private val singleShapeMap = mapOf(
            Direction.NORTH to singleShape,
            Direction.EAST to singleShape.rotateLeft(),
            Direction.SOUTH to singleShape.flip(),
            Direction.WEST to singleShape.rotateRight()
        )
        private val centerShapeMap =
            VoxelAssembly.createCuboidShape(0, 1, 14.5, 16, 15, 15).let {
                mapOf(
                    Direction.NORTH to it,
                    Direction.EAST to it.rotateLeft(),
                    Direction.SOUTH to it.flip(),
                    Direction.WEST to it.rotateRight()
                )
            }

        private val leftShapeMap = mapOf(
            Direction.NORTH to leftShape,
            Direction.EAST to leftShape.rotateLeft(),
            Direction.SOUTH to leftShape.flip(),
            Direction.WEST to leftShape.rotateRight()
        )

        private val rightShapeMap = mapOf(
            Direction.NORTH to rightShape,
            Direction.EAST to rightShape.rotateLeft(),
            Direction.SOUTH to rightShape.flip(),
            Direction.WEST to rightShape.rotateRight()
        )
        private val shapeMap = mapOf(
            SidewaysConnectionShape.SINGLE to singleShapeMap,
            SidewaysConnectionShape.CENTER to centerShapeMap,
            SidewaysConnectionShape.LEFT to leftShapeMap,
            SidewaysConnectionShape.RIGHT to rightShapeMap
        )

    }
}
