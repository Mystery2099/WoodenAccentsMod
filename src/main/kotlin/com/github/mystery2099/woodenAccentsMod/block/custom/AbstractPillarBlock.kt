package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly.appendShapes
import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.with
import com.github.mystery2099.woodenAccentsMod.block.BlockStateUtil.isIn
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.uvLock
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.withXRotationOf
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
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
 * AbstractPillarBlock is an abstract class representing a pillar block in the game.
 *
 * @param baseBlock The base block for the pillar.
 * @param shape The shape of the pillar, containing the top, center, and base shapes.
 */
abstract class AbstractPillarBlock(val baseBlock: Block, private val shape: Shape) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val itemGroup = ModItemGroups.structuralElements
    abstract val connectableBlockTag: TagKey<Block>

    init {
        defaultState = defaultState.with {
            up to false
            down to false
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
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState = super.getStateForNeighborUpdate(
        state = state,
        direction = direction,
        neighborState = neighborState,
        world = world,
        pos = pos,
        neighborPos = neighborPos
    ).with {
        up to canConnect(world, pos, Direction.UP)
        down to canConnect(world, pos, Direction.DOWN)
    }

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

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        super.getPlacementState(ctx).with {
            val world = ctx.world
            val pos = ctx.blockPos
            up to canConnect(world, pos, Direction.UP)
            down to canConnect(world, pos, Direction.DOWN)
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
