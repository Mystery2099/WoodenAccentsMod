package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.isIn
import com.mystery2099.wooden_accents_mod.data.generation.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.uvLock
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withXRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.plus
import com.mystery2099.wooden_accents_mod.util.WhenUtil
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.SideShapeType
import net.minecraft.data.client.MultipartBlockStateSupplier
import net.minecraft.data.client.VariantSettings
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
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

abstract class AbstractPillarBlock(val baseBlock: Block, private val shape: Shape) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val itemGroup = ModItemGroups.structuralElements
    abstract val connectableBlockTag: TagKey<Block>
    override val tag: TagKey<Block> = ModBlockTags.pillars

    init {
        defaultState = defaultState.run {
            with(up, false).with(down, false)
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(up, down)
    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState = super.getStateForNeighborUpdate(
        state = state,
        direction = direction,
        neighborState = neighborState,
        world = world,
        pos = pos!!,
        neighborPos = neighborPos
    )?.run {
        with(up, canConnect(world, pos, Direction.UP)).with(down, canConnect(world, pos, Direction.DOWN))
    } ?: defaultState

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        var newShape = shape.centerShape
        if (!state[up]) newShape += shape.topShape
        if (!state[down]) newShape += shape.baseShape
        return newShape
    }


    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = super.getPlacementState(ctx)?.run {
        val world = ctx.world
        val pos = ctx.blockPos
        with(up, canConnect(world, pos, Direction.UP)).with(down, canConnect(world, pos, Direction.DOWN))
    } ?: defaultState

    private fun canConnect(world: WorldAccess, pos: BlockPos, direction: Direction): Boolean {
        val otherState = world.getBlockState(pos.offset(direction))
        return otherState isIn connectableBlockTag && otherState.isSideSolid(
            world,
            pos.offset(direction),
            direction.opposite,
            SideShapeType.CENTER
        ) && !otherState.isSideSolidFullSquare(world, pos, direction.opposite)
    }

    fun offerRecipe(
        exporter: Consumer<RecipeJsonProvider>,
        outputNum: Int,
        primaryInput: ItemConvertible,
        secondaryInput: ItemConvertible
    ) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, outputNum).apply {
            input('|', secondaryInput)
            input('#', primaryInput)
            pattern("###")
            pattern(" | ")
            pattern("###")
            group(
                when (this@AbstractPillarBlock) {
                    is ThickPillarBlock -> "thick_pillars"
                    is ThinPillarBlock -> "thin_pillars"
                    else -> "pillars"
                }
            )
            requires(primaryInput)
            offerTo(exporter)
        }
    }

    fun genBlockStateModelSupplier(
        centerModel: Identifier,
        bottomModel: Identifier
    ): MultipartBlockStateSupplier = MultipartBlockStateSupplier.create(this).apply {
        with(centerModel.asBlockStateVariant())
        with(WhenUtil.notUp, bottomModel.asBlockStateVariant().withXRotationOf(VariantSettings.Rotation.R180).uvLock())
        with(WhenUtil.notDown, bottomModel.asBlockStateVariant())
    }

    @JvmRecord
    data class Shape(val topShape: VoxelShape, val centerShape: VoxelShape, val baseShape: VoxelShape)
    companion object {
        val up: BooleanProperty = Properties.UP!!
        val down: BooleanProperty = Properties.DOWN!!
    }
}
