package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks.itemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.data.generation.RecipeDataGen.Companion.customGroup
import com.mystery2099.wooden_accents_mod.data.generation.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.BlockStateUtil.withProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.appendShapes
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.createCuboidShape
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flipped
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedRight
import com.mystery2099.wooden_accents_mod.util.WhenUtil
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemPlacementContext
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

/**
 * Table block
 *
 * @property baseBlock
 * @property topBlock
 * @constructor Create empty Table block
 */
class TableBlock(val baseBlock: Block, private val topBlock: Block) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)), CustomItemGroupProvider, CustomRecipeProvider,
    CustomTagProvider<Block>, CustomBlockStateProvider {
    override val itemGroup = ModItemGroups.decorations

    override val tag: TagKey<Block> = ModBlockTags.tables

    init {
        defaultState = stateManager.defaultState.withProperties {
            north setTo false
            east setTo false
            south setTo false
            west setTo false
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(north, east, south, west)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.withDirections(
            north = false,
            east = false,
            south = false,
            west = false
        ).with(waterlogged, super.getPlacementState(ctx).get(waterlogged) ?: false)
    }

    private fun BlockState.withDirections(north: Boolean, east: Boolean, south: Boolean, west: Boolean): BlockState {
        return this.withProperties {
            TableBlock.north setTo north
            TableBlock.east setTo east
            TableBlock.south setTo south
            TableBlock.west setTo west
        }
    }

    private fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        return getBlockState(pos.offset(direction)).block is TableBlock
    }

    private infix fun WorldAccess.checkNorthOf(pos: BlockPos): Boolean = checkDirection(pos, Direction.NORTH)

    private infix fun WorldAccess.checkEastOf(pos: BlockPos): Boolean = checkDirection(pos, Direction.EAST)

    private infix fun WorldAccess.checkSouthOf(pos: BlockPos): Boolean = checkDirection(pos, Direction.SOUTH)

    private infix fun WorldAccess.checkWestOf(pos: BlockPos): Boolean = checkDirection(pos, Direction.WEST)

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos!!, neighborPos)
            .withDirections(
                world.checkNorthOf(pos),
                world.checkEastOf(pos),
                world.checkSouthOf(pos),
                world.checkWestOf(pos)
            )
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState, world: BlockView?, pos: BlockPos?, context: ShapeContext?
    ): VoxelShape {
        val north = state[north]
        val east = state[east]
        val south = state[south]
        val west = state[west]

        return topShape.appendShapes {
            singleLegShape case (!north && !east && !south && !west)
            //ends
            northEndLegShape case (!north && !east && south && !west)
            eastEndLegShape case (!north && !east && !south && west)
            southEndLegShape case (north && !east && !south && !west)
            westEndLegShape case (!north && east && !south && !west)

            //corners
            northEastLegShape case (!north && !east && south && west)
            northWestLegShape case (!north && east && south && !west)
            southEastLegShape case (north && !east && !south && west)
            southWestLegShape case (north && east && !south && !west)
        }
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', topBlock)
            input('|', baseBlock)
            pattern("###")
            pattern(" | ")
            pattern(" | ")
            customGroup(this@TableBlock, "tables")
            requires(topBlock)
            offerTo(exporter)
        }
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        TextureMap().apply {
            put(TextureKey.TOP, topBlock.textureId)
            put(ModModels.legs, baseBlock.textureId)
        }.also { map ->
            generator.blockStateCollector.accept(
                blockStateModelSupplier(
                    ModModels.tableTop.upload(this, map, generator.modelCollector),
                    "${woodType.name.lowercase()}_table_single_leg".toIdentifier().asBlockModelId(),
                    "${woodType.name.lowercase()}_table_end_leg".toIdentifier().asBlockModelId(),
                    "${woodType.name.lowercase()}_table_corner_leg".toIdentifier().asBlockModelId(),
                )
            )
            ModModels.tableItem.upload(this.itemModelId, map, generator.modelCollector)
        }
    }

    private fun blockStateModelSupplier(
        topModel: Identifier,
        singleLegModel: Identifier,
        endLegModel: Identifier,
        cornerLegModel: Identifier,
    ): MultipartBlockStateSupplier = MultipartBlockStateSupplier.create(this).apply {
        val northEndLegVariant = endLegModel.asBlockStateVariant()
        val northEastCornerVariant = cornerLegModel.asBlockStateVariant()

        with(topModel.asBlockStateVariant())
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.notSouth, WhenUtil.notWest),
            singleLegModel.asBlockStateVariant()
        )
        //Ends
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.south, WhenUtil.notWest), northEndLegVariant
        )
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.notSouth, WhenUtil.west),
            northEndLegVariant.withYRotationOf(VariantSettings.Rotation.R90)
        )
        with(
            When.allOf(WhenUtil.north, WhenUtil.notEast, WhenUtil.notSouth, WhenUtil.notWest),
            northEndLegVariant.withYRotationOf(VariantSettings.Rotation.R180)
        )
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.east, WhenUtil.notSouth, WhenUtil.notWest),
            northEndLegVariant.withYRotationOf(VariantSettings.Rotation.R270)
        )
        //Corners
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.south, WhenUtil.west), northEastCornerVariant
        )
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.east, WhenUtil.south, WhenUtil.notWest),
            northEastCornerVariant.withYRotationOf(VariantSettings.Rotation.R270)
        )
        with(
            When.allOf(WhenUtil.north, WhenUtil.notEast, WhenUtil.notSouth, WhenUtil.west),
            northEastCornerVariant.withYRotationOf(VariantSettings.Rotation.R90)
        )
        with(
            When.allOf(WhenUtil.north, WhenUtil.east, WhenUtil.notSouth, WhenUtil.notWest),
            northEastCornerVariant.withYRotationOf(VariantSettings.Rotation.R180)
        )
    }

    companion object {
        val north: BooleanProperty = Properties.NORTH
        val east: BooleanProperty = Properties.EAST
        val south: BooleanProperty = Properties.SOUTH
        val west: BooleanProperty = Properties.WEST

        val topShape: VoxelShape = createCuboidShape(0, 13, 0, 16, 16, 16)
        val singleLegShape: VoxelShape = createCuboidShape(6, 0, 6, 10, 13, 10)
        val northEndLegShape: VoxelShape = createCuboidShape(6, 0, 1, 10, 13, 5)
        val eastEndLegShape: VoxelShape = northEndLegShape.rotatedLeft
        val southEndLegShape: VoxelShape = northEndLegShape.flipped
        val westEndLegShape: VoxelShape = northEndLegShape.rotatedRight
        val northEastLegShape: VoxelShape = createCuboidShape(11, 0, 1, 15, 13, 5)
        val northWestLegShape: VoxelShape = northEastLegShape.rotatedRight
        val southEastLegShape: VoxelShape = northWestLegShape.flipped
        val southWestLegShape: VoxelShape = northEastLegShape.flipped
    }
}