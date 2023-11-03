package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.combination.VoxelAssembly.appendShapes
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.asBlockModelId
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.itemModelId
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.textureId
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.woodType
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.CoffeeTableTypes
import com.github.mystery2099.woodenAccentsMod.block.defaultItemStack
import com.github.mystery2099.woodenAccentsMod.data.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.data.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.conditionally
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.*
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.state.property.ModProperties
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.isIn
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.isOf
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.withProperties
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.putModel
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.withYRotationOf
import com.github.mystery2099.woodenAccentsMod.util.WhenUtil
import com.github.mystery2099.woodenAccentsMod.util.WhenUtil.and
import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier


/**
 * Coffee table block
 *
 * @property baseBlock The base block for the coffee table.
 * @property topBlock The block used for the top(texture) of the coffee table.
 * @constructor Creates a [CoffeeTableBlock] using the block [settings] of another [Block] as the base.
 */
class CoffeeTableBlock(val baseBlock: Block, private val topBlock: Block) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider,
    CustomBlockLootTableProvider {


    override val tag: TagKey<Block> = ModBlockTags.coffeeTables
    override val itemGroup = ModItemGroups.decorations

    /*
     * Getters for checking and setting specific properties of [BlockState]s.
     */
    private val BlockState.isTall: Boolean
        get() = getOrEmpty(type) == Optional.of(CoffeeTableTypes.TALL)
    override val variantItemGroupStack: ItemStack
        get() = this.defaultItemStack.apply {
            orCreateNbt.setType(CoffeeTableTypes.TALL)
        }
    private val NbtCompound.isTall: Boolean
        get() = this.getString(CoffeeTableTypes.TAG) == CoffeeTableTypes.TALL.asString()

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

    private fun NbtCompound.setTall() = this.setType(CoffeeTableTypes.TALL)
    private fun NbtCompound.setType(type: CoffeeTableTypes) =
        apply { putString(CoffeeTableTypes.TAG, type.asString()) }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val nbt = ctx.stack.nbt
        val state = ctx.world.getBlockState(ctx.blockPos)
        return if (state isOf this && nbt?.getString(CoffeeTableTypes.TAG) != CoffeeTableTypes.TALL.asString()) state.setTall()
        else defaultState.setDirections(ctx.world, ctx.blockPos).run {
            nbt?.let {
                if (it.isTall) with(type, CoffeeTableTypes.TALL) else this
            } ?: this
        }
    }

    private fun BlockState.asSingle(): BlockState {
        return this.withProperties {
            north setTo false
            east setTo false
            south setTo false
            west setTo false
        }
    }

    /**
     * Sets the [CoffeeTableBlock]'s type to [CoffeeTableTypes.SHORT].
     *
     * @return The updated [BlockState].
     */
    private fun BlockState.setShort(): BlockState = this.with(type, CoffeeTableTypes.SHORT)
    /**
     * Sets the [CoffeeTableBlock]'s type to [CoffeeTableTypes.TALL].
     *
     * @return The updated [BlockState].
     */
    private fun BlockState.setTall(): BlockState = this.with(type, CoffeeTableTypes.TALL)

    /**
     * Sets the connection directions for the [CoffeeTableBlock] based on the surrounding blocks.
     *
     * @param world The [WorldAccess] where the block is located.
     * @param pos The position of the [Block].
     * @return The updated [BlockState].
     */
    private fun BlockState.setDirections(world: WorldAccess, pos: BlockPos): BlockState {
        return this.withProperties {
            north setTo world.checkNorthOf(pos)
            east setTo world.checkEastOf(pos)
            south setTo world.checkSouthOf(pos)
            west setTo world.checkWestOf(pos)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        return !state.isTall && context.stack.item == asItem()
    }

    private fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        return getBlockState(pos.offset(direction))?.let { otherState: BlockState ->
            getBlockState(pos)?.let { thisState: BlockState ->
                val states = arrayOf(thisState, otherState)
                if (states.all { it.block is CoffeeTableBlock }) thisState.get(type) == otherState.get(type)
                else if (otherState isOf  Blocks.SCAFFOLDING) thisState.isTall
                else states.all { it isIn tag }
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
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        .withProperties {
            north setTo world.checkNorthOf(pos)
            east setTo world.checkEastOf(pos)
            south setTo world.checkSouthOf(pos)
            west setTo world.checkWestOf(pos)
        }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val isTall: Boolean = state[type] == CoffeeTableTypes.TALL
        val hasNorthConnection = state[north]
        val hasSouthConnection = state[south]
        val hasEastConnection = state[east]
        val hasWestConnection = state[west]

        val shouldAppendNorthEast = !hasNorthConnection && !hasEastConnection
        val shouldAppendNorthWest = !hasNorthConnection && !hasWestConnection

        val shouldAppendSouthEast = !hasSouthConnection && !hasEastConnection
        val shouldAppendSouthWest = !hasSouthConnection && !hasWestConnection

        return (if (isTall) tallTopShape else shortTopShape).appendShapes {
            //Short legs
            shortNorthEastLeg case shouldAppendNorthEast
            shortNorthWestLeg case shouldAppendNorthWest
            shortSouthEastLeg case shouldAppendSouthEast
            shortSouthWestLeg case shouldAppendSouthWest
            //Tall legs
            tallNorthEastLeg case (shouldAppendNorthEast && isTall)
            tallNorthWestLeg case (shouldAppendNorthWest && isTall)
            tallSouthEastLeg case (shouldAppendSouthEast && isTall)
            tallSouthWestLeg case (shouldAppendSouthWest && isTall)

        }
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
                Text.literal(stack.nbt?.getString(CoffeeTableTypes.TAG))
                    .formatted(Formatting.GRAY, Formatting.ITALIC)
            )
        }
    }

    /**
     * Offers a shaped recipe for the [CoffeeTableBlock] to the recipe exporter.
     *
     * @param exporter The recipe exporter to offer the recipe to.
     */
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

    /**
     * Generates block state models and item models for the [CoffeeTableBlock].
     *
     * @param generator The block state model generator.
     */
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
            generateItemModel(this, generator)
        }
    }

    /**
     * A [MultipartBlockStateSupplier] for the block state model of the [CoffeeTableBlock].
     *
     * @param shortTopModel The model identifier for the short top.
     * @param shortLegModel The model identifier for the short leg.
     * @param tallTopModel The model identifier for the tall top.
     * @param tallLegModel The model identifier for the tall leg.
     * @return A [MultipartBlockStateSupplier] for the [CoffeeTableBlock].
     */
    private fun blockStateModelSupplier(
        shortTopModel: Identifier,
        shortLegModel: Identifier,
        tallTopModel: Identifier,
        tallLegModel: Identifier
    ): MultipartBlockStateSupplier = MultipartBlockStateSupplier.create(this).apply {
        val shortNorthEastVariant = shortLegModel.asBlockStateVariant()
        val tallNorthEastVariant = tallLegModel.asBlockStateVariant()

        val isTall = When.create().set(ModProperties.coffeeTableType, CoffeeTableTypes.TALL)
        val isShort = When.create().set(ModProperties.coffeeTableType, CoffeeTableTypes.SHORT)

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


    private fun generateItemModel(coffeeTableBlock: CoffeeTableBlock, generator: BlockStateModelGenerator) {
        val textureMap = mapOf(
            TextureKey.TOP to coffeeTableBlock.topBlock.textureId,
            ModModels.legs to coffeeTableBlock.baseBlock.textureId
        )
        val tallModel = ModModels.coffeeTableTallInventory.upload(
            coffeeTableBlock.itemModelId.withSuffixedPath("_tall"),
            TextureMap().apply {
                textureMap.forEach {
                    put(it.key, it.value)
                }
            },
            generator.modelCollector
        )
        val jsonObject = ModModels.coffeeTableInventory.createJson(coffeeTableBlock.itemModelId, textureMap)
        val jsonArray = JsonArray()
        val jsonObject2 = JsonObject()
        val jsonObject3 = JsonObject()
        jsonObject3.addProperty("height", 1.0f)
        jsonObject2.add("predicate", jsonObject3)
        jsonObject2.addProperty("model", tallModel.toString())
        jsonArray.add(jsonObject2)
        jsonObject.add("overrides", jsonArray)
        generator.modelCollector.accept(coffeeTableBlock.itemModelId, Supplier {
            jsonObject
        })
    }
    override fun getLootTableBuilder(provider: FabricBlockLootTableProvider): LootTable.Builder {
        val tallStatePredicate = StatePredicate.Builder.create().exactMatch(type, CoffeeTableTypes.TALL)
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
        private val shortTopShape = VoxelAssembly.createCuboidShape(0, 7, 0, 16, 9, 16)

        private val tallTopShape = shortTopShape.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)

        // Short North Shapes
        private val shortNorthEastLeg = VoxelAssembly.createCuboidShape(13.75, 0, 0.25, 15.75, 7, 2.25)

        private val shortNorthWestLeg = shortNorthEastLeg.rotateRight()

        // Short South Shapes
        private val shortSouthEastLeg = shortNorthWestLeg.flip()

        private val shortSouthWestLeg = shortNorthEastLeg.flip()

        // Tall North Shapes
        private val tallNorthEastLeg = shortNorthEastLeg.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)

        private val tallNorthWestLeg = tallNorthEastLeg.rotateRight()

        // Tall South Shapes
        private val tallSouthEastLeg = tallNorthWestLeg.flip()

        private val tallSouthWestLeg = tallNorthEastLeg.flip()
    }
}
