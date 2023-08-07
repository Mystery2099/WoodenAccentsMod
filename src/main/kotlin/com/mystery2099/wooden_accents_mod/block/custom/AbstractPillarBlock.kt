package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.plus
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemPlacementContext
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

abstract class AbstractPillarBlock(val baseBlock: Block, private val shape: Shape) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    GroupedBlock, RecipeBlockData, TaggedBlock {
    override val itemGroup get() = ModItemGroups.outsideBlockItemGroup
    override val tag: TagKey<Block>
        get() = ModBlockTags.pillars

    init { defaultState = defaultState.apply {
        with(up, false)
        with(down, false)
    } }

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
    )?.apply {
        with(up, world.checkUp(pos))
        with(down, world.checkDown(pos))
    } ?: Blocks.AIR.defaultState

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = shape.centerShape + arrayOf(
        if (!state[up]) shape.topShape else VoxelShapes.empty(),
        if (!state[down]) shape.baseShape else VoxelShapes.empty(),
        VoxelShapes.empty()
    )


    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = super.getPlacementState(ctx)?.apply {
        val world = ctx.world
        val pos = ctx.blockPos
        with(up, world.checkUp(pos))?.with(down, world.checkDown(pos))
    }!!
    //Up & Down
    open fun WorldAccess.getStateAtPos(blockPos: BlockPos): BlockState = getBlockState(blockPos)
    open fun WorldAccess.getUpState(pos: BlockPos): BlockState = getStateAtPos(pos.up())

    open fun WorldAccess.getDownState(pos: BlockPos): BlockState = getStateAtPos(pos.down())

    abstract infix fun WorldAccess.checkUp(pos: BlockPos): Boolean
    abstract infix fun WorldAccess.checkDown(pos: BlockPos): Boolean

    fun offerRecipe(exporter: Consumer<RecipeJsonProvider>, outputNum: Int, primaryInput: ItemConvertible, secondaryInput: ItemConvertible) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, outputNum).apply {
            input('|', secondaryInput)
            input('#', primaryInput)
            pattern("###")
            pattern(" | ")
            pattern("###")
            group(when (this@AbstractPillarBlock) {
                is ThickPillarBlock -> "thick_pillars"
                is ThinPillarBlock -> "thin_pillars"
                else -> "pillars"
            })
            requires(primaryInput)
            offerTo(exporter)
        }
    }
    @JvmRecord
    data class Shape(val topShape: VoxelShape, val centerShape: VoxelShape, val baseShape: VoxelShape)
    companion object {
        val up: BooleanProperty = Properties.UP!!
        val down: BooleanProperty = Properties.DOWN!!
    }
}
