package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.addIf
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.getItemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableType
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.group
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.putModel
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import com.mystery2099.wooden_accents_mod.util.WhenUtil
import com.mystery2099.wooden_accents_mod.util.WhenUtil.and
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
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class CoffeeTableBlock(val baseBlock: Block, val topBlock: Block) : AbstractTableBlock(baseBlock),
    GroupedBlock {
    override val tag: TagKey<Block>
        get() = ModBlockTags.coffeeTables
    override val itemGroup get() = ModItemGroups.livingRoomItemGroup

    init {
        defaultState = defaultState.with(type, CoffeeTableType.SHORT)
        //WoodenAccentsModItemGroups.livingRoomItems += this
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(type)
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

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 6).apply {
            input('_', topBlock)
            input('|', baseBlock)
            pattern("___")
            pattern("| |")
            group(this@CoffeeTableBlock, "coffee_tables")
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
                    shortLegModel = "${this.woodType.name.lowercase()}_coffee_table_leg_short".asWamId().asBlockModelId(),
                    tallTopModel = ModModels.coffeeTableTopTall.upload(this, map, generator.modelCollector),
                    tallLegModel = "${this.woodType.name.lowercase()}_coffee_table_leg_tall".asWamId().asBlockModelId(),
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

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val state = ctx.world.getBlockState(ctx.blockPos)
        val isSneaking = ctx.player?.isSneaking
        return if (state.isOf(this) && (isSneaking == null || !isSneaking) ) state.with(type, CoffeeTableType.TALL) else super.getPlacementState(ctx)?.withIfExists(
            type, CoffeeTableType.SHORT)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "state.get(type) == CoffeeTableType.SHORT && context.stack.item == asItem()",
        "com.mystery2099.block.custom.CoffeeTableBlock.Companion.type",
        "com.mystery2099.block.custom.enums.CoffeeTableType"
    )
    )
    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        return state[type] == CoffeeTableType.SHORT && context.stack.item == asItem()
    }

    override fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        val here = getBlockState(pos)
        val there = getBlockState(pos.offset(direction))
        return there.block is CoffeeTableBlock && here[type] == there[type]
    }

    companion object {
        @JvmStatic
        val type = ModProperties.coffeeTableType

        private const val SHAPE_VERTICAL_OFFSET = 7.0 / 16

        // Top shapes
        @JvmStatic
        private val shortTopShape = createCuboidShape(0.0, 7.0, 0.0, 16.0, 9.0, 16.0)
        @JvmStatic
        private val tallTopShape = shortTopShape.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)

        // Short North Shapes
        @JvmStatic
        private val shortNorthEastLeg = createCuboidShape(13.75, 0.0, 0.25, 15.75, 7.0, 2.25)
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
