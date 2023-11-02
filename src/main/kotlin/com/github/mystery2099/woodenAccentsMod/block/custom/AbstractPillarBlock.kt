package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly.appendShapes
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.isIn
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.withProperties
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.uvLock
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.withXRotationOf
import com.github.mystery2099.woodenAccentsMod.util.WhenUtil
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

/**
 * Abstract pillar block.
 *
 * This abstract class represents a pillar block that can be used as a base for various pillar-like structures.
 * It extends [AbstractWaterloggableBlock] and provides additional functionality for custom pillar blocks.
 *
 * @property baseBlock The base block on which this pillar is based.
 * @property shape The shape of the pillar, containing top, center, and base shapes.
 * @constructor Creates an instance of `AbstractPillarBlock` from the block settings of another block.
 * @param baseBlock The base block to be used as a template for this pillar block.
 * @param shape The shape of the pillar, including top, center, and base shapes.
 */
abstract class AbstractPillarBlock(val baseBlock: Block, private val shape: Shape) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val itemGroup = ModItemGroups.structuralElements
    abstract val connectableBlockTag: TagKey<Block>

    init {
        defaultState = defaultState.withProperties {
            up setTo false
            down setTo false
        }
    }

    /**
     * Appends custom block state properties for this pillar block.
     *
     * @param builder The block state builder for this block.
     */
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
    ).withProperties {
        up setTo canConnect(world, pos, Direction.UP)
        down setTo canConnect(world, pos, Direction.DOWN)
    }

    /**
     * Returns the outline shape of this pillar block based on its state.
     *
     * @param state The block state of the pillar.
     * @param world The world where the block is located.
     * @param pos The position of the block.
     * @param context The shape context.
     * @return The voxel shape representing the outline of the pillar.
     */
    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = shape.centerShape.appendShapes {
        shape.topShape case !state[up]
        shape.baseShape case !state[down]
    }

    /**
     * Determines the block state when placing this pillar block.
     *
     * @param ctx The placement context.
     * @return The block state of the pillar after placement.
     */
    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        super.getPlacementState(ctx).withProperties {
            val world = ctx.world
            val pos = ctx.blockPos
            up setTo canConnect(world, pos, Direction.UP)
            down setTo canConnect(world, pos, Direction.DOWN)
        }

    /**
     * Checks if this pillar can connect to another block in the specified direction.
     *
     * @param world The world in which the block is placed.
     * @param pos The position of the block.
     * @param direction The direction to check for connection.
     * @return `true` if the pillar can connect to the block in the specified direction, `false` otherwise.
     */
    private fun canConnect(world: WorldAccess, pos: BlockPos, direction: Direction): Boolean {
        val otherState = world.getBlockState(pos.offset(direction))
        return otherState isIn connectableBlockTag && otherState.isSideSolid(
            world,
            pos.offset(direction),
            direction.opposite,
            SideShapeType.CENTER
        ) && !otherState.isSideSolidFullSquare(world, pos, direction.opposite)
    }

    /**
     * Offer a crafting recipe for this pillar block.
     *
     * @param exporter The consumer for exporting the recipe.
     * @param outputNum The number of pillar blocks to be produced in the recipe.
     * @param primaryInput The primary input item convertible for the recipe.
     * @param secondaryInput The secondary input item convertible for the recipe.
     */
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

    /**
     * Generates a block state model supplier for this pillar block.
     *
     * @param centerModel The identifier for the center model.
     * @param bottomModel The identifier for the bottom model.
     * @return A `MultipartBlockStateSupplier` for the pillar block.
     */
    fun genBlockStateModelSupplier(
        centerModel: Identifier,
        bottomModel: Identifier
    ): MultipartBlockStateSupplier = MultipartBlockStateSupplier.create(this).apply {
        with(centerModel.asBlockStateVariant())
        with(WhenUtil.notUp, bottomModel.asBlockStateVariant().withXRotationOf(VariantSettings.Rotation.R180).uvLock())
        with(WhenUtil.notDown, bottomModel.asBlockStateVariant())
    }

    /**
     * Represents the shape of the pillar, containing top, center, and base shapes.
     *
     * @property topShape The voxel shape for the top part of the pillar.
     * @property centerShape The voxel shape for the center part of the pillar.
     * @property baseShape The voxel shape for the base part of the pillar.
     */
    @JvmRecord
    data class Shape(val topShape: VoxelShape, val centerShape: VoxelShape, val baseShape: VoxelShape)
    companion object {
        val up: BooleanProperty = Properties.UP
        val down: BooleanProperty = Properties.DOWN
    }
}
