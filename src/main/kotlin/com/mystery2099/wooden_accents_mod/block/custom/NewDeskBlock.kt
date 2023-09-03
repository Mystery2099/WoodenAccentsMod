package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.enums.DeskShape
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.isIn
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class NewDeskBlock(settings: Settings, val baseBlock: Block, val topBlock: Block) :
    AbstractWaterloggableBlock(settings), CustomItemGroupProvider, CustomRecipeProvider, CustomBlockStateProvider,
    CustomTagProvider {
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

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(facing, shape)
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
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
            .withShape(
                left = world.getBlockState(pos?.offset(state[facing].rotateYClockwise())).isDesk,
                right = world.getBlockState(pos?.offset(state[facing].rotateYCounterclockwise())).isDesk,
                forward = world.getBlockState(pos?.offset(state[facing])).isDesk
            )
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
    }
}