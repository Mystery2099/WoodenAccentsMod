package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.enums.SidewaysConnectionShape
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateUtil.isIn
import com.mystery2099.wooden_accents_mod.util.BlockStateUtil.withProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotated
import com.mystery2099.wooden_accents_mod.util.VoxelShapeTransformation
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class ConnectingLadderBlock(val baseBlock: Block) :
    AbstractCustomLadderBlock(FabricBlockSettings.of(baseBlock.defaultState.material, baseBlock.defaultMapColor).apply {
        hardness(Blocks.LADDER.hardness)
        resistance(Blocks.LADDER.blastResistance)
        sounds(baseBlock.getSoundGroup(baseBlock.defaultState))

        if (baseBlock.requiredFeatures.contains(FeatureFlags.UPDATE_1_20)) {
            requires(FeatureFlags.UPDATE_1_20)
        }
    }) {
    override val tag: TagKey<Block> = ModBlockTags.connectingLadders

    init {
        defaultState = defaultState.withShape(left = false, right = false)
    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState {
        val leftState = world.getBlockState(pos.offset(state[FACING].rotateYClockwise()))
        val rightState = world.getBlockState(pos.offset(state[FACING].rotateYCounterclockwise()))
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
            .withShape(
                left = state.canConnectTo(leftState),
                right = state.canConnectTo(rightState)
            )
    }

    private fun BlockState.isConnectingLadder(): Boolean = this isIn tag

    private fun BlockState.canConnectTo(other: BlockState): Boolean {
        return this.isConnectingLadder() && other.isConnectingLadder() && this[FACING] == other[FACING]
    }

    private fun BlockState.withShape(left: Boolean, right: Boolean): BlockState = this.withProperties {
        shape setTo when {
            left && right -> SidewaysConnectionShape.CENTER
            left -> SidewaysConnectionShape.RIGHT
            right -> SidewaysConnectionShape.LEFT
            else -> SidewaysConnectionShape.SINGLE
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(shape)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape =
        shapeMap[state[shape]]?.get(state[FACING]) ?: super.getOutlineShape(state, world, pos, context)

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        super.offerRecipe(exporter, baseBlock, 8, "connecting_ladder")
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val textureMap = TextureMap.all(this.baseBlock)
        val singleModel = ModModels.connectingLadder.upload(this, textureMap, generator.modelCollector)
        val leftModel = ModModels.connectingLadderLeft.upload(this, textureMap, generator.modelCollector)
        val centerModel = ModModels.connectingLadderCenter.upload(this, textureMap, generator.modelCollector)
        val rightModel = ModModels.connectingLadderRight.upload(this, textureMap, generator.modelCollector)
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(this).coordinate(
                BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, ModProperties.sidewaysConnectionShape).apply {
                    val northSingleVariant = singleModel.asBlockStateVariant()
                    val northLeftVariant = leftModel.asBlockStateVariant()
                    val northCenterVariant = centerModel.asBlockStateVariant()
                    val northRightVariant = rightModel.asBlockStateVariant()

                    register(Direction.NORTH, SidewaysConnectionShape.SINGLE, northSingleVariant)
                    register(Direction.NORTH, SidewaysConnectionShape.LEFT, northLeftVariant)
                    register(Direction.NORTH, SidewaysConnectionShape.CENTER, northCenterVariant)
                    register(Direction.NORTH, SidewaysConnectionShape.RIGHT, northRightVariant)

                    register(
                        Direction.EAST, SidewaysConnectionShape.SINGLE, northSingleVariant.withYRotationOf(
                            VariantSettings.Rotation.R90
                        )
                    )
                    register(
                        Direction.EAST, SidewaysConnectionShape.LEFT, northLeftVariant.withYRotationOf(
                            VariantSettings.Rotation.R90
                        )
                    )
                    register(
                        Direction.EAST, SidewaysConnectionShape.CENTER, northCenterVariant.withYRotationOf(
                            VariantSettings.Rotation.R90
                        )
                    )
                    register(
                        Direction.EAST, SidewaysConnectionShape.RIGHT, northRightVariant.withYRotationOf(
                            VariantSettings.Rotation.R90
                        )
                    )

                    register(
                        Direction.SOUTH, SidewaysConnectionShape.SINGLE, northSingleVariant.withYRotationOf(
                            VariantSettings.Rotation.R180
                        )
                    )
                    register(
                        Direction.SOUTH, SidewaysConnectionShape.LEFT, northLeftVariant.withYRotationOf(
                            VariantSettings.Rotation.R180
                        )
                    )
                    register(
                        Direction.SOUTH, SidewaysConnectionShape.CENTER, northCenterVariant.withYRotationOf(
                            VariantSettings.Rotation.R180
                        )
                    )
                    register(
                        Direction.SOUTH, SidewaysConnectionShape.RIGHT, northRightVariant.withYRotationOf(
                            VariantSettings.Rotation.R180
                        )
                    )

                    register(
                        Direction.WEST, SidewaysConnectionShape.SINGLE, northSingleVariant.withYRotationOf(
                            VariantSettings.Rotation.R270
                        )
                    )
                    register(
                        Direction.WEST, SidewaysConnectionShape.LEFT, northLeftVariant.withYRotationOf(
                            VariantSettings.Rotation.R270
                        )
                    )
                    register(
                        Direction.WEST, SidewaysConnectionShape.CENTER, northCenterVariant.withYRotationOf(
                            VariantSettings.Rotation.R270
                        )
                    )
                    register(
                        Direction.WEST, SidewaysConnectionShape.RIGHT, northRightVariant.withYRotationOf(
                            VariantSettings.Rotation.R270
                        )
                    )

                }
            )
        )
    }

    companion object {
        val shape = ModProperties.sidewaysConnectionShape

        private val singleShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(2, 0, 15, 4, 16, 16),
            VoxelShapeHelper.createCuboidShape(12, 0, 15, 14, 16, 16),
            VoxelShapeHelper.createCuboidShape(2, 1, 14.5, 14, 15, 15)
        )

        private val leftShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(12, 0, 15, 14, 16, 16),
            VoxelShapeHelper.createCuboidShape(0, 1, 14.5, 14, 15, 15)
        )

        private val rightShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(2, 0, 15, 4, 16, 16),
            VoxelShapeHelper.createCuboidShape(2, 1, 14.5, 16, 15, 15)
        )

        private val singleShapeMap = mapOf(
            Direction.NORTH to singleShape,
            Direction.EAST to singleShape.rotated(VoxelShapeTransformation.ROTATE_LEFT),
            Direction.SOUTH to singleShape.rotated(VoxelShapeTransformation.FLIP_HORIZONTAL),
            Direction.WEST to singleShape.rotated(VoxelShapeTransformation.ROTATE_RIGHT)
        )
        private val centerShapeMap =
            VoxelShapeHelper.createCuboidShape(0, 1, 14.5, 16, 15, 15).let {
                mapOf(
                    Direction.NORTH to it,
                    Direction.EAST to it.rotated(VoxelShapeTransformation.ROTATE_LEFT),
                    Direction.SOUTH to it.rotated(VoxelShapeTransformation.FLIP_HORIZONTAL),
                    Direction.WEST to it.rotated(VoxelShapeTransformation.ROTATE_RIGHT)
                )
            }

        private val leftShapeMap = mapOf(
            Direction.NORTH to leftShape,
            Direction.EAST to leftShape.rotated(VoxelShapeTransformation.ROTATE_LEFT),
            Direction.SOUTH to leftShape.rotated(VoxelShapeTransformation.FLIP_HORIZONTAL),
            Direction.WEST to leftShape.rotated(VoxelShapeTransformation.ROTATE_RIGHT)
        )

        private val rightShapeMap = mapOf(
            Direction.NORTH to rightShape,
            Direction.EAST to rightShape.rotated(VoxelShapeTransformation.ROTATE_LEFT),
            Direction.SOUTH to rightShape.rotated(VoxelShapeTransformation.FLIP_HORIZONTAL),
            Direction.WEST to rightShape.rotated(VoxelShapeTransformation.ROTATE_RIGHT)
        )
        private val shapeMap = mapOf(
            SidewaysConnectionShape.SINGLE to singleShapeMap,
            SidewaysConnectionShape.CENTER to centerShapeMap,
            SidewaysConnectionShape.LEFT to leftShapeMap,
            SidewaysConnectionShape.RIGHT to rightShapeMap
        )

    }
}