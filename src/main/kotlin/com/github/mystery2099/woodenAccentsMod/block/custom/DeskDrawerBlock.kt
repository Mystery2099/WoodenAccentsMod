package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.withProperties
import com.github.mystery2099.woodenAccentsMod.block.BlockStateUtil.isIn
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.SidewaysConnectionShape
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.DeskDrawerBlockEntity
import com.github.mystery2099.woodenAccentsMod.block.textureId
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.withYRotationOf
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.state.property.ModProperties
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
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
import net.minecraft.util.shape.VoxelShapes
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
class DeskDrawerBlock(settings: Settings, private val edgeBlock: Block, val baseBlock: Block) :
    WaterloggableBlockWithEntity(settings), CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>,
    CustomBlockStateProvider {

    override val itemGroup: CustomItemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.deskDrawers
    private inline val BlockState.isDeskDrawer: Boolean
        get() = this isIn tag
    private inline val BlockState.isDesk: Boolean
        get() = this isIn ModBlockTags.desks

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

    private fun BlockState.canConnectTo(otherState: BlockState): Boolean {
        return ((this.isDeskDrawer || this.isDesk) && (otherState.isDeskDrawer || otherState.isDesk)) && (this[facing] == otherState[facing])
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(facing, shape)
    }

    private fun getAdjacentStates(world: WorldAccess, pos: BlockPos, state: BlockState = world.getBlockState(pos)): Array<BlockState> {
        return arrayOf(
            world.getBlockState(pos.offset(state[facing].rotateYClockwise())),
            world.getBlockState(pos.offset(state[facing].rotateYCounterclockwise()))
        )
    }
    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val world = ctx.world
        val pos = ctx.blockPos

        //States
        val state = super.getPlacementState(ctx).with(facing, ctx.horizontalPlayerFacing.opposite)
        val adjacentStates = getAdjacentStates(world, pos, state)
        return state.withShape(
            left = state.canConnectTo(adjacentStates[0]),
            right = state.canConnectTo(adjacentStates[1])
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
        val adjacentStates = getAdjacentStates(world, pos)
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
            .withShape(
                left = state.canConnectTo(adjacentStates[0]),
                right = state.canConnectTo(adjacentStates[1])
            )
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = shapeMap[state[shape]]?.get(state[facing]) ?: VoxelShapes.fullCube()

    @Deprecated("Deprecated in Java", ReplaceWith("BlockRenderType.MODEL", "net.minecraft.block.BlockRenderType"))
    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL

    @Deprecated("Deprecated in Java", ReplaceWith("true"))
    override fun hasComparatorOutput(state: BlockState) = true

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))",
            "net.minecraft.screen.ScreenHandler"
        )
    )
    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "state.with(facing, rotation.rotate(state[facing]))",
            "com.mystery2099.wooden_accents_mod.block.custom.DeskDrawerBlock.Companion.facing",
            "com.mystery2099.wooden_accents_mod.block.custom.DeskDrawerBlock.Companion.facing"
        )
    )
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(facing, rotation.rotate(state[facing]))
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "state.rotate(mirror.getRotation(state[facing]))",
            "com.mystery2099.wooden_accents_mod.block.custom.DeskDrawerBlock.Companion.facing"
        )
    )
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('|', edgeBlock)
            input('_', baseBlock)
            input('#', Items.CHEST)
            pattern("___")
            pattern("|#|")
            pattern("| |")
            customGroup(this@DeskDrawerBlock, "desk_drawers")
            requires(ModBlockTags.getItemTagFrom(ModBlockTags.desks))
            offerTo(exporter)
        }
    }

    companion object {
        val facing: DirectionProperty = Properties.HORIZONTAL_FACING
        val shape: EnumProperty<SidewaysConnectionShape> = ModProperties.sidewaysConnectionShape
        private val nonSingleShape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(1, 1, 1, 15, 15, 16),
            VoxelAssembly.createCuboidShape(2, 9, 0, 14, 14, 1),
            VoxelAssembly.createCuboidShape(6, 11, -1, 11, 12, 0),
            VoxelAssembly.createCuboidShape(6, 4, -1, 11, 5, 0),
            VoxelAssembly.createCuboidShape(2, 2, 0, 14, 7, 1)
        )
        private val leftLegsShape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(1, 0, 1, 2, 1, 2),
            VoxelAssembly.createCuboidShape(1, 0, 15, 2, 1, 16)
        )
        private val rightLegsShape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(14, 0, 1, 15, 1, 2),
            VoxelAssembly.createCuboidShape(14, 0, 15, 15, 1, 16)
        )

        private val northSingleShape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(0, 15, 0, 16, 16, 16),
            VoxelAssembly.createCuboidShape(1, 1, 1, 15, 15, 15),
            VoxelAssembly.createCuboidShape(2, 2, 0, 14, 7, 1),
            VoxelAssembly.createCuboidShape(2, 9, 0, 14, 14, 1),
            VoxelAssembly.createCuboidShape(6, 11, -1, 11, 12, 0),
            VoxelAssembly.createCuboidShape(6, 4, -1, 11, 5, 0),
            VoxelAssembly.createCuboidShape(1, 0, 1, 2, 1, 2),
            VoxelAssembly.createCuboidShape(14, 0, 1, 15, 1, 2),
            VoxelAssembly.createCuboidShape(14, 0, 14, 15, 1, 15),
            VoxelAssembly.createCuboidShape(1, 0, 14, 2, 1, 15)
        )

        private val northCenterShape = VoxelAssembly.union(
            nonSingleShape, leftLegsShape, rightLegsShape, DeskBlock.northCenterShape
        )
        private val northLeftShape = VoxelAssembly.union(
            nonSingleShape, leftLegsShape, DeskBlock.northLeftShape
        )
        private val northRightShape = VoxelAssembly.union(
            nonSingleShape, rightLegsShape, DeskBlock.northRightShape
        )

        private val singleShapeMap = mapOf(
            Direction.NORTH to northSingleShape,
            Direction.EAST to northSingleShape.rotateLeft(),
            Direction.SOUTH to northSingleShape.flip(),
            Direction.WEST to northSingleShape.rotateRight()
        )
        private val centerShapeMap = mapOf(
            Direction.NORTH to northCenterShape,
            Direction.EAST to northCenterShape.rotateLeft(),
            Direction.SOUTH to northCenterShape.flip(),
            Direction.WEST to northCenterShape.rotateRight()
        )
        private val leftShapeMap = mapOf(
            Direction.NORTH to northLeftShape,
            Direction.EAST to northLeftShape.rotateLeft(),
            Direction.SOUTH to northLeftShape.flip(),
            Direction.WEST to northLeftShape.rotateRight()
        )
        private val rightShapeMap = mapOf(
            Direction.NORTH to northRightShape,
            Direction.EAST to northRightShape.rotateLeft(),
            Direction.SOUTH to northRightShape.flip(),
            Direction.WEST to northRightShape.rotateRight()
        )
        private val shapeMap = mapOf(
            SidewaysConnectionShape.SINGLE to singleShapeMap,
            SidewaysConnectionShape.CENTER to centerShapeMap,
            SidewaysConnectionShape.LEFT to leftShapeMap,
            SidewaysConnectionShape.RIGHT to rightShapeMap,
        )
    }
}