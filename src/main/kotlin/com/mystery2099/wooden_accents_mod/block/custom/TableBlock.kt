package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.getItemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.BlockStateGeneratorDataBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.group
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
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

class TableBlock(val baseBlock: Block, val topBlock: Block) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    GroupedBlock, RecipeBlockData, TaggedBlock, BlockStateGeneratorDataBlock {
    override val itemGroup
        get() = ModItemGroups.decorations

    override val tag: TagKey<Block>
        get() = ModBlockTags.tables
    init {
        defaultState = stateManager.defaultState.apply {
            with(north, false)
            with(east, false)
            with(south, false)
            with(west, false)
            with(waterlogged, false)
        }
    }
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(
            north,
            east,
            south,
            west
        )
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return defaultState.with(north, false)
            .with(east, false)
            .with(south, false)
            .with(west, false)
            .with(waterlogged, super.getPlacementState(ctx)?.get(waterlogged) ?: false)
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
    ): BlockState? {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos!!, neighborPos)
            ?.withIfExists(north, world.checkNorthOf(pos))
            ?.withIfExists(east, world.checkEastOf(pos))
            ?.withIfExists(south, world.checkSouthOf(pos))
            ?.withIfExists(west, world.checkWestOf(pos))
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = state.run {
        val north = this[north]
        val east = this[east]
        val south = this[south]
        val west = this[west]
        mutableListOf<VoxelShape>().apply {
            add(topShape)
            if (!north && !east && !south && !west) add(legShape)
            //Ends
            if (!north && !east && south && !west) add(northEndLegShape)
            if (!north && !east && !south && west) add(eastEndLegShape)
            if (north && !east && !south && !west) add(southEndLegShape)
            if (!north && east && !south && !west) add(westEndLegShape)
            //Corners
            if (!north && !east && south && west) add(northEastLegShape)
            if (!north && east && south && !west) add(northWestLegShape)
            if (north && !east && !south && west) add(southEastLegShape)
            if (north && east && !south && !west) add(southWestLegShape)

        }.combined
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', topBlock)
            input('|', baseBlock)
            pattern("###")
            pattern(" | ")
            pattern(" | ")
            group(this@TableBlock, "tables")
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
                    "${woodType.name.lowercase()}_table_single_leg".asWamId().asBlockModelId(),
                    "${woodType.name.lowercase()}_table_end_leg".asWamId().asBlockModelId(),
                    "${woodType.name.lowercase()}_table_corner_leg".asWamId().asBlockModelId(),
                )
            )
            ModModels.tableItem.upload(this.getItemModelId(), map, generator.modelCollector)
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
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.south, WhenUtil.notWest),
            northEndLegVariant
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
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.south, WhenUtil.west),
            northEastCornerVariant
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

        val topShape: VoxelShape = createCuboidShape(0.0, 13.0, 0.0, 16.0, 16.0, 16.0)
        val legShape: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 13.0, 10.0)
        val northEndLegShape: VoxelShape = createCuboidShape(6.0, 0.0, 1.0, 10.0, 13.0, 5.0)
        val eastEndLegShape: VoxelShape = northEndLegShape.rotateLeft()
        val southEndLegShape: VoxelShape = northEndLegShape.flip()
        val westEndLegShape: VoxelShape = northEndLegShape.rotateRight()
        val northEastLegShape: VoxelShape = createCuboidShape(11.0, 0.0, 1.0, 15.0, 13.0, 5.0)
        val northWestLegShape: VoxelShape = northEastLegShape.rotateRight()
        val southEastLegShape: VoxelShape = northWestLegShape.flip()
        val southWestLegShape: VoxelShape = northEastLegShape.flip()
    }
}