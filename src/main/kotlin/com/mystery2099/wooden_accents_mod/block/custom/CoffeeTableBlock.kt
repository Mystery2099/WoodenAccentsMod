package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.addIf
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks.getItemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableType
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.BlockStateGeneratorDataBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.isIn
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.customGroup
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.putModel
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import com.mystery2099.wooden_accents_mod.util.WhenUtil
import com.mystery2099.wooden_accents_mod.util.WhenUtil.and
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.client.item.TooltipContext
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class CoffeeTableBlock(val baseBlock: Block, val topBlock: Block) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    GroupedBlock, RecipeBlockData, TaggedBlock, BlockStateGeneratorDataBlock {
    override val tag: TagKey<Block> = ModBlockTags.coffeeTables
    override val itemGroup = ModItemGroups.decorations

    init {
        defaultState = defaultState.also {
            it.short().asSingle().with(waterlogged, false)
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(type,
            north,
            east,
            south,
            west
        )
    }
    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val nbt = ctx.stack.nbt
        val state = ctx.world.getBlockState(ctx.blockPos)
        return if (state.isOf(this) && nbt?.getBoolean("tall") == false) state.tall()
        else defaultState.asSingle().with(waterlogged, super.getPlacementState(ctx)?.get(waterlogged) == true).run {
            nbt?.let {
                if (it.getBoolean("tall") ) {
                    with(type, CoffeeTableType.TALL)
                } else this
            } ?: this
        }
    }
    private fun BlockState.asSingle(): BlockState {
        return this.with(north, false)
            .with(east, false)
            .with(south, false)
            .with(west, false)
    }
    private fun BlockState.short(): BlockState = this.with(type, CoffeeTableType.SHORT)
    private fun BlockState.tall(): BlockState = this.with(type, CoffeeTableType.TALL)
    @Deprecated("Deprecated in Java")
    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        return state[type] == CoffeeTableType.SHORT && context.stack.item == asItem()
    }

    private fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        val here = getBlockState(pos)
        val there = getBlockState(pos.offset(direction))
        return if (there.block is CoffeeTableBlock) here[type] == there[type]
        else there isIn tag && here isIn tag
    }
    private fun WorldAccess.checkNorthOf(pos: BlockPos): Boolean = checkDirection(pos, Direction.NORTH)

    private fun WorldAccess.checkEastOf(pos: BlockPos): Boolean = checkDirection(pos, Direction.EAST)

    private fun WorldAccess.checkSouthOf(pos: BlockPos): Boolean = checkDirection(pos, Direction.SOUTH)

    private fun WorldAccess.checkWestOf(pos: BlockPos): Boolean = checkDirection(pos, Direction.WEST)
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
            ?.withIfExists(north, world.checkNorthOf(pos))
            ?.withIfExists(east, world.checkEastOf(pos))
            ?.withIfExists(south, world.checkSouthOf(pos))
            ?.withIfExists(west, world.checkWestOf(pos)) ?: state
    }



    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val isTall: Boolean = state[type] == CoffeeTableType.TALL
        val hasNorthConnection = state[north]
        val hasSouthConnection = state[south]
        val hasEastConnection = state[east]
        val hasWestConnection = state[west]

        return mutableListOf<VoxelShape>().apply {
            add(if (isTall) tallTopShape else shortTopShape)
            when {
                !hasNorthConnection -> {
                    if (!hasEastConnection) {
                        add(shortNorthEastLeg)
                        addIf(isTall, tallNorthEastLeg)
                    }
                    if (!hasWestConnection) {
                        add(shortNorthWestLeg)
                        addIf(isTall, tallNorthWestLeg)
                    }
                }
            }
            when {
                !hasSouthConnection -> {
                    if (!hasEastConnection) {
                        add(shortSouthEastLeg)
                        addIf(isTall, tallSouthEastLeg)
                    }
                    if (!hasWestConnection) {
                        add(shortSouthWestLeg)
                        addIf(isTall, tallSouthWestLeg)
                    }
                }
            }
        }.combined
    }

    override fun getPickStack(world: BlockView, pos: BlockPos, state: BlockState): ItemStack {
        val itemStack = super.getPickStack(world, pos, state)
        NbtCompound().apply {
            putBoolean("tall", state[CoffeeTableBlock.type] == CoffeeTableType.TALL)
            itemStack.nbt = this
        }
        return itemStack
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: BlockView?,
        tooltip: MutableList<Text>,
        options: TooltipContext
    ) {
        super.appendTooltip(stack, world, tooltip, options)
        if (stack.nbt?.getBoolean("tall") == true) {
            tooltip.add(Text.literal("tall").formatted(Formatting.ITALIC, Formatting.GRAY))
        }
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 6).apply {
            input('_', topBlock)
            input('|', baseBlock)
            pattern("___")
            pattern("| |")
            customGroup(this@CoffeeTableBlock, "coffee_tables")
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
                    shortTopModel = ModModels.coffeeTableTopShort.upload(this, map, generator.modelCollector),
                    shortLegModel = "${this.woodType.name.lowercase()}_coffee_table_leg_short".toIdentifier().asBlockModelId(),
                    tallTopModel = ModModels.coffeeTableTopTall.upload(this, map, generator.modelCollector),
                    tallLegModel = "${this.woodType.name.lowercase()}_coffee_table_leg_tall".toIdentifier().asBlockModelId(),
                )
            )
            ModModels.coffeeTableInventory.upload(this.getItemModelId(), map, generator.modelCollector)
        }
    }
    private fun blockStateModelSupplier(
        shortTopModel: Identifier,
        shortLegModel: Identifier,
        tallTopModel: Identifier,
        tallLegModel: Identifier
    ): MultipartBlockStateSupplier = MultipartBlockStateSupplier.create(this).apply {
        val shortNorthEastVariant = shortLegModel.asBlockStateVariant()
        val tallNorthEastVariant = tallLegModel.asBlockStateVariant()

        val isTall = WhenUtil.newWhen.set(ModProperties.coffeeTableType, CoffeeTableType.TALL)
        val isShort = WhenUtil.newWhen.set(ModProperties.coffeeTableType, CoffeeTableType.SHORT)

        mapOf(
            isShort to BlockStateVariant().putModel(shortTopModel),
            WhenUtil.notNorthEast to shortNorthEastVariant,
            WhenUtil.notNorthWest to shortNorthEastVariant.withYRotationOf(VariantSettings.Rotation.R270),
            WhenUtil.notSouthEast to shortNorthEastVariant.withYRotationOf(VariantSettings.Rotation.R90),
            WhenUtil.notSouthWest to shortNorthEastVariant.withYRotationOf(VariantSettings.Rotation.R180),
            isTall to tallTopModel.asBlockStateVariant(),
            WhenUtil.notNorthEast and isTall to tallNorthEastVariant,
            WhenUtil.notNorthWest and isTall to tallNorthEastVariant.withYRotationOf(VariantSettings.Rotation.R270),
            WhenUtil.notSouthEast and isTall to tallNorthEastVariant.withYRotationOf(VariantSettings.Rotation.R90),
            WhenUtil.notSouthWest and isTall to tallNorthEastVariant.withYRotationOf(VariantSettings.Rotation.R180)
        ).forEach(::with)
    }



    companion object {
        val type = ModProperties.coffeeTableType
        val north: BooleanProperty = Properties.NORTH
        val east: BooleanProperty = Properties.EAST
        val south: BooleanProperty = Properties.SOUTH
        val west: BooleanProperty = Properties.WEST

        private const val SHAPE_VERTICAL_OFFSET = 7.0 / 16

        // Top shapes
        @JvmStatic
        private val shortTopShape = VoxelShapeHelper.createCuboidShape(0, 7, 0, 16, 9, 16)
        @JvmStatic
        private val tallTopShape = shortTopShape.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)

        // Short North Shapes
        @JvmStatic
        private val shortNorthEastLeg = VoxelShapeHelper.createCuboidShape(13.75, 0, 0.25, 15.75, 7, 2.25)
        @JvmStatic
        private val shortNorthWestLeg = shortNorthEastLeg.rotateRight()

        // Short South Shapes
        @JvmStatic
        private val shortSouthEastLeg = shortNorthWestLeg.flip()
        @JvmStatic
        private val shortSouthWestLeg = shortNorthEastLeg.flip()

        // Tall North Shapes
        @JvmStatic
        private val tallNorthEastLeg = shortNorthEastLeg.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)
        @JvmStatic
        private val tallNorthWestLeg = tallNorthEastLeg.rotateRight()

        // Tall South Shapes
        @JvmStatic
        private val tallSouthEastLeg = tallNorthWestLeg.flip()
        @JvmStatic
        private val tallSouthWestLeg = tallNorthEastLeg.flip()
    }

}
