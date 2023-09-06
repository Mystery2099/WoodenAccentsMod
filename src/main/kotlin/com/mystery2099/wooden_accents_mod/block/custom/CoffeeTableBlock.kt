package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableType
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.*
import com.mystery2099.wooden_accents_mod.block.defaultItemStack
import com.mystery2099.wooden_accents_mod.block.isOf
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.isIn
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.datagen.ModelDataGen
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.customGroup
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.datagen.conditionally
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.putModel
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.createCuboidShape
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flipped
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.plus
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedRight
import com.mystery2099.wooden_accents_mod.util.WhenUtil
import com.mystery2099.wooden_accents_mod.util.WhenUtil.and
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.client.item.TooltipContext
import net.minecraft.data.client.*
import net.minecraft.data.server.loottable.BlockLootTableGenerator
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.BlockStatePropertyLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.function.SetNbtLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.nbt.NbtCompound
import net.minecraft.predicate.StatePredicate
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
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.*
import java.util.function.Consumer


class CoffeeTableBlock(val baseBlock: Block, val topBlock: Block) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider, CustomBlockStateProvider,
    CustomLootTableProvider {


    override val tag: TagKey<Block> = ModBlockTags.coffeeTables
    override val itemGroup = ModItemGroups.decorations
    private val BlockState.isTall: Boolean
        get() = getOrEmpty(type) == Optional.of(CoffeeTableType.TALL)
    override val variantItemGroupStack: ItemStack
        get() = this.defaultItemStack.apply {
            orCreateNbt.setType(CoffeeTableType.TALL)
        }
    private val NbtCompound.isTall: Boolean
        get() = this.getString("coffee_table_type") == CoffeeTableType.TALL.asString()

    init {
        defaultState = defaultState.setShort().asSingle().with(waterlogged, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(
            type,
            north,
            east,
            south,
            west
        )
    }

    private fun NbtCompound.setTall() = this.setType(CoffeeTableType.TALL)
    private fun NbtCompound.setType(type: CoffeeTableType) =
        apply { putString("coffee_table_type", type.asString()) }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val nbt = ctx.stack.nbt
        val state = ctx.world.getBlockState(ctx.blockPos)
        return if (state.isOf(this) && nbt?.getString("coffee_table_type") != CoffeeTableType.TALL.asString()) state.setTall()
        else defaultState.setDirections(ctx.world, ctx.blockPos).run {
            nbt?.let {
                if (it.isTall) with(type, CoffeeTableType.TALL) else this
            } ?: this
        }
    }

    private fun BlockState.asSingle(): BlockState {
        return this.with(north, false)
            .with(east, false)
            .with(south, false)
            .with(west, false)
    }

    private fun BlockState.setShort(): BlockState = this.with(type, CoffeeTableType.SHORT)
    private fun BlockState.setTall(): BlockState = this.with(type, CoffeeTableType.TALL)
    private fun BlockState.setDirections(world: WorldAccess, pos: BlockPos): BlockState {
        return with(north, world.checkNorthOf(pos)).with(east, world.checkEastOf(pos))
            .with(south, world.checkSouthOf(pos)).with(west, world.checkWestOf(pos))
    }

    @Deprecated("Deprecated in Java")
    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        return !state.isTall && context.stack.item == asItem()
    }

    private fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        return getBlockState(pos.offset(direction))?.let { there: BlockState ->
            getBlockState(pos)?.let { here: BlockState ->
                if (there.block is CoffeeTableBlock && here.block is CoffeeTableBlock) here.get(type) == there.get(type)
                else if (there isOf Blocks.SCAFFOLDING) here.isTall
                else there isIn tag && here isIn tag
            } ?: false
        } ?: false
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
            ?.withIfExists(north, world.checkNorthOf(pos))?.withIfExists(east, world.checkEastOf(pos))
            ?.withIfExists(south, world.checkSouthOf(pos))?.withIfExists(west, world.checkWestOf(pos))
            ?: state
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

        var shape = VoxelShapes.empty()

        shape += (if (isTall) tallTopShape else shortTopShape)

        when {
            !hasNorthConnection -> {
                if (!hasEastConnection) {
                    shape += shortNorthEastLeg
                    if (isTall) shape += tallNorthEastLeg
                }
                if (!hasWestConnection) {
                    shape += shortNorthWestLeg
                    if (isTall) shape += tallNorthWestLeg
                }
            }
        }

        when {
            !hasSouthConnection -> {
                if (!hasEastConnection) {
                    shape += shortSouthEastLeg
                    if (isTall) shape += tallSouthEastLeg
                }
                if (!hasWestConnection) {
                    shape += shortSouthWestLeg
                    if (isTall) shape += tallSouthWestLeg
                }
            }
        }

        return shape
    }

    override fun getPickStack(world: BlockView, pos: BlockPos, state: BlockState): ItemStack {
        return super.getPickStack(world, pos, state).apply {
            orCreateNbt.setType(state[type])
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: BlockView?,
        tooltip: MutableList<Text>,
        options: TooltipContext
    ) {
        super.appendTooltip(stack, world, tooltip, options)
        if (stack.hasNbt()) {
            tooltip.add(
                Text.literal(stack.nbt?.getString("coffee_table_type"))
                    .formatted(Formatting.GRAY, Formatting.ITALIC)
            )
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
                    shortLegModel = "${this.woodType.name.lowercase()}_coffee_table_leg_short".toIdentifier()
                        .asBlockModelId(),
                    tallTopModel = ModModels.coffeeTableTopTall.upload(this, map, generator.modelCollector),
                    tallLegModel = "${this.woodType.name.lowercase()}_coffee_table_leg_tall".toIdentifier()
                        .asBlockModelId(),
                )
            )
            //ModModels.coffeeTableInventory.upload(this.getItemModelId(), map, generator.modelCollector)
            ModelDataGen.createCoffeeTableItemModel(this, generator)
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

        val isTall = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.TALL)
        val isShort = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.SHORT)

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

    override fun getLootTableBuilder(provider: FabricBlockLootTableProvider): LootTable.Builder {
        val tallStatePredicate = StatePredicate.Builder.create().exactMatch(type, CoffeeTableType.TALL)
        val whenBlockIsTall = BlockStatePropertyLootCondition.builder(this).properties(tallStatePredicate)
        val nbt = NbtCompound().setTall()
        return LootTable.builder().pool(
            LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(
                provider.applyExplosionDecay(
                    this, ItemEntry.builder(this).apply(
                        SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0f))
                            .conditionally(BlockLootTableGenerator.WITHOUT_SILK_TOUCH, whenBlockIsTall)
                    ).apply(
                        SetNbtLootFunction.builder(nbt)
                            .conditionally(BlockLootTableGenerator.WITH_SILK_TOUCH, whenBlockIsTall)
                    )
                )
            )
        )
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
        private val shortTopShape = createCuboidShape(0, 7, 0, 16, 9, 16)

        @JvmStatic
        private val tallTopShape = shortTopShape.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)

        // Short North Shapes
        @JvmStatic
        private val shortNorthEastLeg = createCuboidShape(13.75, 0, 0.25, 15.75, 7, 2.25)

        @JvmStatic
        private val shortNorthWestLeg = shortNorthEastLeg.rotatedRight

        // Short South Shapes
        @JvmStatic
        private val shortSouthEastLeg = shortNorthWestLeg.flipped

        @JvmStatic
        private val shortSouthWestLeg = shortNorthEastLeg.flipped

        // Tall North Shapes
        @JvmStatic
        private val tallNorthEastLeg = shortNorthEastLeg.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)

        @JvmStatic
        private val tallNorthWestLeg = tallNorthEastLeg.rotatedRight

        // Tall South Shapes
        @JvmStatic
        private val tallSouthEastLeg = tallNorthWestLeg.flipped

        @JvmStatic
        private val tallSouthWestLeg = tallNorthEastLeg.flipped
    }
}
