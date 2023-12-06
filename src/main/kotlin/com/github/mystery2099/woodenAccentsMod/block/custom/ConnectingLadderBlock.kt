package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.withProperties
import com.github.mystery2099.woodenAccentsMod.block.BlockStateUtil.isIn
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.SidewaysConnectionShape
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.withYRotationOf
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.state.property.ModProperties
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

/**
 * Connecting ladder block
 *
 * @property baseBlock
 * @constructor Create Connecting ladder block from the block settings of another block
 */
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

    private fun BlockState.isConnectingLadder(): Boolean = this isIn tag || this.block is ConnectingLadderBlock

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