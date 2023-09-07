package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.enums.DeskShape
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.isIn
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flipped
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedRight
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class DeskBlock(settings: Settings, val baseBlock: Block, val topBlock: Block) :
    AbstractWaterloggableBlock(settings), CustomItemGroupProvider, CustomRecipeProvider, CustomBlockStateProvider,
    CustomTagProvider<Block> {


    override val itemGroup: CustomItemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.desks
    private val BlockState.isDesk: Boolean
        get() = this isIn tag


    init {
        defaultState = defaultState.with(facing, Direction.NORTH).withShape(
            left = false,
            right = false,
            forward = false
        )
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape =
        shapeMap[state[shape]]?.get(state[facing]) ?: super.getOutlineShape(
            state, world, pos,
            context
        )

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(facing, shape)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx).with(facing, ctx.horizontalPlayerFacing.opposite)
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
        val left = world.getBlockState(pos?.offset(state[facing].rotateYClockwise())).isDesk
        val right = world.getBlockState(pos?.offset(state[facing].rotateYCounterclockwise())).isDesk
        val forward = world.getBlockState(pos?.offset(state[facing])).let {
            it.isDesk && if (left) it[facing] == state[facing].rotateYClockwise()
            else if (right) it[facing] == state[facing].rotateYCounterclockwise() else true
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
            .withShape(left, right, forward)
    }

    private fun BlockState.withShape(left: Boolean, right: Boolean, forward: Boolean = false): BlockState {
        return this.withIfExists(
            shape,
            when {
                left && right -> DeskShape.CENTER
                left && forward -> DeskShape.RIGHT_CORNER
                right && forward -> DeskShape.LEFT_CORNER
                left -> DeskShape.RIGHT
                right -> DeskShape.LEFT
                else -> DeskShape.SINGLE
            }
        )
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val block = this
        TextureMap().apply {
            put(TextureKey.TOP, block.topBlock.textureId)
            put(TextureKey.SIDE, block.baseBlock.textureId)
        }.let { map ->
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                    .coordinate(
                        variantMap(
                            singleModel = ModModels.desk.upload(block, map, generator.modelCollector),
                            leftModel = ModModels.deskLeft.upload(block, map, generator.modelCollector),
                            centerModel = ModModels.deskCenter.upload(block, map, generator.modelCollector),
                            rightModel = ModModels.deskRight.upload(block, map, generator.modelCollector),
                            leftCornerModel = ModModels.deskLeftCorner.upload(block, map, generator.modelCollector)
                        )
                    )
            )
        }
    }

    private fun variantMap(
        singleModel: Identifier,
        leftModel: Identifier,
        centerModel: Identifier,
        rightModel: Identifier,
        leftCornerModel: Identifier
    ) = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, ModProperties.deskShape).apply {
        val northSingle = singleModel.asBlockStateVariant()
        val northLeft = leftModel.asBlockStateVariant()
        val northCenter = centerModel.asBlockStateVariant()
        val northRight = rightModel.asBlockStateVariant()
        val northLeftCorner = leftCornerModel.asBlockStateVariant()

        mapOf(
            Direction.NORTH to mapOf(
                DeskShape.SINGLE to northSingle,
                DeskShape.LEFT to northLeft,
                DeskShape.CENTER to northCenter,
                DeskShape.RIGHT to northRight,
                DeskShape.LEFT_CORNER to northLeftCorner,
                DeskShape.RIGHT_CORNER to northLeftCorner.withYRotationOf(VariantSettings.Rotation.R90)
            ),
            Direction.EAST to mapOf(
                DeskShape.SINGLE to northSingle.withYRotationOf(VariantSettings.Rotation.R90),
                DeskShape.LEFT to northLeft.withYRotationOf(VariantSettings.Rotation.R90),
                DeskShape.CENTER to northCenter.withYRotationOf(VariantSettings.Rotation.R90),
                DeskShape.RIGHT to northRight.withYRotationOf(VariantSettings.Rotation.R90),
                DeskShape.LEFT_CORNER to northLeftCorner.withYRotationOf(VariantSettings.Rotation.R90),
                DeskShape.RIGHT_CORNER to northLeftCorner.withYRotationOf(VariantSettings.Rotation.R180)
            ),
            Direction.SOUTH to mapOf(
                DeskShape.SINGLE to northSingle.withYRotationOf(VariantSettings.Rotation.R180),
                DeskShape.LEFT to northLeft.withYRotationOf(VariantSettings.Rotation.R180),
                DeskShape.CENTER to northCenter.withYRotationOf(VariantSettings.Rotation.R180),
                DeskShape.RIGHT to northRight.withYRotationOf(VariantSettings.Rotation.R180),
                DeskShape.LEFT_CORNER to northLeftCorner.withYRotationOf(VariantSettings.Rotation.R180),
                DeskShape.RIGHT_CORNER to northLeftCorner.withYRotationOf(VariantSettings.Rotation.R270)
            ),
            Direction.WEST to mapOf(
                DeskShape.SINGLE to northSingle.withYRotationOf(VariantSettings.Rotation.R270),
                DeskShape.LEFT to northLeft.withYRotationOf(VariantSettings.Rotation.R270),
                DeskShape.CENTER to northCenter.withYRotationOf(VariantSettings.Rotation.R270),
                DeskShape.RIGHT to northRight.withYRotationOf(VariantSettings.Rotation.R270),
                DeskShape.LEFT_CORNER to northLeftCorner.withYRotationOf(VariantSettings.Rotation.R270),
                DeskShape.RIGHT_CORNER to northLeftCorner
            )
        ).forEach { i -> i.value.forEach { j -> register(i.key, j.key, j.value) } }
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {

    }

    companion object {
        val shape = ModProperties.deskShape
        val facing: DirectionProperty = Properties.HORIZONTAL_FACING

        //shapes
        private val northSingleShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(1, 15, 0, 15, 16, 16),
            VoxelShapeHelper.createCuboidShape(1, 8, 15, 15, 15, 16),
            VoxelShapeHelper.createCuboidShape(15, 0, 0, 16, 16, 2),
            VoxelShapeHelper.createCuboidShape(15, 14, 2, 16, 16, 14),
            VoxelShapeHelper.createCuboidShape(15, 0, 14, 16, 16, 16),
            VoxelShapeHelper.createCuboidShape(0, 0, 0, 1, 16, 2),
            VoxelShapeHelper.createCuboidShape(0, 14, 2, 1, 16, 14),
            VoxelShapeHelper.createCuboidShape(0, 0, 14, 1, 16, 16)
        )

        private val northLeftShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(0, 15, 0, 15, 16, 16),
            VoxelShapeHelper.createCuboidShape(0, 8, 15, 15, 15, 16),
            VoxelShapeHelper.createCuboidShape(15, 0, 0, 16, 16, 2),
            VoxelShapeHelper.createCuboidShape(15, 14, 2, 16, 16, 14),
            VoxelShapeHelper.createCuboidShape(15, 0, 14, 16, 16, 16)
        )

        private val northCenterShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(0, 15, 0, 16, 16, 16),
            VoxelShapeHelper.createCuboidShape(0, 8, 15, 16, 15, 16)
        )

        private val northRightShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(1, 15, 0, 16, 16, 16),
            VoxelShapeHelper.createCuboidShape(1, 8, 15, 16, 15, 16),
            VoxelShapeHelper.createCuboidShape(0, 0, 0, 1, 16, 2),
            VoxelShapeHelper.createCuboidShape(0, 14, 2, 1, 16, 14),
            VoxelShapeHelper.createCuboidShape(0, 0, 14, 1, 16, 16)
        )

        private val northLeftCornerShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(15, 0, 14, 16, 15, 16),
            VoxelShapeHelper.createCuboidShape(14, 0, 15, 15, 15, 16),
            VoxelShapeHelper.createCuboidShape(0, 8, 15, 14, 15, 16),
            VoxelShapeHelper.createCuboidShape(15, 8, 0, 16, 15, 14),
            VoxelShapeHelper.createCuboidShape(0, 15, 0, 16, 16, 16)
        )
        private val northRightCornerShape = northLeftCornerShape.rotatedLeft

        private val singleShapeMap = mapOf(
            Direction.NORTH to northSingleShape,
            Direction.EAST to northSingleShape.rotatedLeft,
            Direction.SOUTH to northSingleShape.flipped,
            Direction.WEST to northSingleShape.rotatedRight
        )
        private val centerShapeMap =
            mapOf(
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
        private val leftCornerShapeMap = mapOf(
            Direction.NORTH to northLeftCornerShape,
            Direction.EAST to northLeftCornerShape.rotatedLeft,
            Direction.SOUTH to northLeftCornerShape.flipped,
            Direction.WEST to northLeftCornerShape.rotatedRight
        )
        private val rightCornerShapeMap = mapOf(
            Direction.NORTH to northRightCornerShape,
            Direction.EAST to northRightCornerShape.rotatedLeft,
            Direction.SOUTH to northRightCornerShape.flipped,
            Direction.WEST to northRightCornerShape.rotatedRight
        )
        private val shapeMap = mapOf(
            DeskShape.SINGLE to singleShapeMap,
            DeskShape.CENTER to centerShapeMap,
            DeskShape.LEFT to leftShapeMap,
            DeskShape.RIGHT to rightShapeMap,
            DeskShape.LEFT_CORNER to leftCornerShapeMap,
            DeskShape.RIGHT_CORNER to rightCornerShapeMap
        )
    }
}