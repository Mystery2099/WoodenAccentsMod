package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.enums.SidewaysConnectionShape
import com.mystery2099.wooden_accents_mod.block_entity.custom.DeskDrawerBlockEntity
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateUtil.isIn
import com.mystery2099.wooden_accents_mod.util.BlockStateUtil.withProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flipped
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedRight
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.screen.ScreenHandler
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

/**
 * Desk drawer block
 *
 * @property edgeBlock
 * @property baseBlock
 * @constructor
 *
 * @param settings
 */
class DeskDrawerBlock(settings: Settings, val edgeBlock: Block, val baseBlock: Block) :
    WaterloggableBlockWithEntity(settings), CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>,
    CustomBlockStateProvider {

    override val itemGroup: CustomItemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.deskDrawers

    init {
        this.defaultState =
            this.stateManager.defaultState.withProperties { facing setTo Direction.NORTH }
                .withShape(left = false, right = false)
    }

    private fun BlockState.withShape(left: Boolean, right: Boolean): BlockState = this.withProperties {
        shape setTo when {
        left && right -> SidewaysConnectionShape.CENTER
        left -> SidewaysConnectionShape.RIGHT
        right -> SidewaysConnectionShape.LEFT
        else -> SidewaysConnectionShape.SINGLE
    }
    }

    private fun BlockState.isDeskDrawer(): Boolean = this isIn tag
    private fun BlockState.isDesk(): Boolean = this isIn ModBlockTags.desks

    private fun BlockState.canConnectTo(other: BlockState): Boolean {
        return ((this.isDeskDrawer() || this.isDesk()) && (other.isDeskDrawer() || other.isDesk())) && (this[facing] == other[facing])
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(facing, shape)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx).with(facing, ctx.horizontalPlayerFacing.opposite).withShape(
            left = false,
            right = false
        )
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        val blockEntity: BlockEntity? = world.getBlockEntity(pos)
        if (itemStack.hasCustomName() && blockEntity is DeskDrawerBlockEntity) {
            blockEntity.customName = itemStack.name
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is DeskDrawerBlockEntity) {
            player.openHandledScreen(blockEntity)
            PiglinBrain.onGuardedBlockInteracted(player, true)
        }
        return ActionResult.CONSUME
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
        val leftState = world.getBlockState(pos.offset(state[facing].rotateYClockwise()))
        val rightState = world.getBlockState(pos.offset(state[facing].rotateYCounterclockwise()))
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
            .withShape(
                left = state.canConnectTo(leftState),
                right = state.canConnectTo(rightState)
            )
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = shapeMap[state[shape]]?.get(state[facing]) ?: super.getOutlineShape(
        state, world, pos,
        context
    )

    @Deprecated("Deprecated in Java")
    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL

    @Deprecated("Deprecated in Java")
    override fun hasComparatorOutput(state: BlockState) = true

    @Deprecated("Deprecated in Java")
    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
    }

    @Deprecated("Deprecated in Java")
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(facing, rotation.rotate(state[facing]))
    }

    @Deprecated("Deprecated in Java")
    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state[facing]))
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = DeskDrawerBlockEntity(pos, state)

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val textureMap = TextureMap().apply {
            put(TextureKey.SIDE, baseBlock.textureId)
            put(TextureKey.EDGE, edgeBlock.textureId)
        }

        val singleModel = ModModels.deskDrawer.upload(this, textureMap, generator.modelCollector)
        val leftModel = ModModels.deskDrawerLeft.upload(this, textureMap, generator.modelCollector)
        val centerModel = ModModels.deskDrawerCenter.upload(this, textureMap, generator.modelCollector)
        val rightModel = ModModels.deskDrawerRight.upload(this, textureMap, generator.modelCollector)

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

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {

    }

    companion object {
        val facing: DirectionProperty = Properties.HORIZONTAL_FACING
        val shape: EnumProperty<SidewaysConnectionShape> = ModProperties.sidewaysConnectionShape
        private val nonSingleShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(1, 1, 1, 15, 15, 16),
            VoxelShapeHelper.createCuboidShape(2, 9, 0, 14, 14, 1),
            VoxelShapeHelper.createCuboidShape(6, 11, -1, 11, 12, 0),
            VoxelShapeHelper.createCuboidShape(6, 4, -1, 11, 5, 0),
            VoxelShapeHelper.createCuboidShape(2, 2, 0, 14, 7, 1)
        )
        private val leftLegsShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(1, 0, 1, 2, 1, 2),
            VoxelShapeHelper.createCuboidShape(1, 0, 15, 2, 1, 16)
        )
        private val rightLegsShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(14, 0, 1, 15, 1, 2),
            VoxelShapeHelper.createCuboidShape(14, 0, 15, 15, 1, 16)
        )

        private val northSingleShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(0, 15, 0, 16, 16, 16),
            VoxelShapeHelper.createCuboidShape(1, 1, 1, 15, 15, 15),
            VoxelShapeHelper.createCuboidShape(2, 2, 0, 14, 7, 1),
            VoxelShapeHelper.createCuboidShape(2, 9, 0, 14, 14, 1),
            VoxelShapeHelper.createCuboidShape(6, 11, -1, 11, 12, 0),
            VoxelShapeHelper.createCuboidShape(6, 4, -1, 11, 5, 0),
            VoxelShapeHelper.createCuboidShape(1, 0, 1, 2, 1, 2),
            VoxelShapeHelper.createCuboidShape(14, 0, 1, 15, 1, 2),
            VoxelShapeHelper.createCuboidShape(14, 0, 14, 15, 1, 15),
            VoxelShapeHelper.createCuboidShape(1, 0, 14, 2, 1, 15)
        )

        private val northCenterShape = VoxelShapeHelper.union(
            nonSingleShape, leftLegsShape, rightLegsShape, DeskBlock.northCenterShape
        )
        private val northLeftShape = VoxelShapeHelper.union(
            nonSingleShape, leftLegsShape, DeskBlock.northLeftShape
        )
        private val northRightShape = VoxelShapeHelper.union(
            nonSingleShape, rightLegsShape, DeskBlock.northRightShape
        )

        private val singleShapeMap = mapOf(
            Direction.NORTH to northSingleShape,
            Direction.EAST to northSingleShape.rotatedLeft,
            Direction.SOUTH to northSingleShape.flipped,
            Direction.WEST to northSingleShape.rotatedRight
        )
        private val centerShapeMap = mapOf(
            Direction.NORTH to northCenterShape,
            Direction.EAST to northCenterShape.rotatedLeft,
            Direction.SOUTH to northCenterShape.flipped,
            Direction.WEST to northCenterShape.rotatedRight
        )
        private val leftShapeMap = mapOf(
            Direction.NORTH to northLeftShape,
            Direction.EAST to northLeftShape.rotatedLeft,
            Direction.SOUTH to northLeftShape.flipped,
            Direction.WEST to northLeftShape.rotatedRight
        )
        private val rightShapeMap = mapOf(
            Direction.NORTH to northRightShape,
            Direction.EAST to northRightShape.rotatedLeft,
            Direction.SOUTH to northRightShape.flipped,
            Direction.WEST to northRightShape.rotatedRight
        )
        private val shapeMap = mapOf(
            SidewaysConnectionShape.SINGLE to singleShapeMap,
            SidewaysConnectionShape.CENTER to centerShapeMap,
            SidewaysConnectionShape.LEFT to leftShapeMap,
            SidewaysConnectionShape.RIGHT to rightShapeMap,
        )
    }
}