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
abstract class AbstractPillarBlock(val baseBlock: Block, private val pillarShape: Shape) :
    AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val itemGroup = ModItemGroups.structuralElements
    abstract val connectableBlockTag: TagKey<Block>

    private val outlineShapes = Array(4) { connections ->
        pillarShape.centerShape.appendShapes {
            pillarShape.topShape case (connections and UP_CONNECTION == 0)
            pillarShape.baseShape case (connections and DOWN_CONNECTION == 0)
        }
    }

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
    ): VoxelShape = outlineShapes[getConnectionIndex(state)]

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        super.getPlacementState(ctx).with {
            val world = ctx.world
            val pos = ctx.blockPos
            up to canConnect(world, pos, Direction.UP)
            down to canConnect(world, pos, Direction.DOWN)
        }

    private fun canConnect(world: WorldAccess, pos: BlockPos, direction: Direction): Boolean {
        val otherPos = pos.offset(direction)
        val otherState = world.getBlockState(otherPos)
        if (!(otherState isIn connectableBlockTag)) return false

        // An isolated thick pillar has full-cube collision, but remains visually connectable as a pillar.
        if (otherState.block is ThickPillarBlock) return true

        return otherState.isSideSolid(
            world,
            otherPos,
            direction.opposite,
            SideShapeType.CENTER
        ) && !otherState.isSideSolidFullSquare(world, otherPos, direction.opposite)
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
        val up: BooleanProperty = Properties.UP
        val down: BooleanProperty = Properties.DOWN

        private fun getConnectionIndex(state: BlockState): Int {
            var connections = 0
            if (state[up]) connections = connections or UP_CONNECTION
            if (state[down]) connections = connections or DOWN_CONNECTION
            return connections
        }

        private const val UP_CONNECTION = 1
        private const val DOWN_CONNECTION = 2
    }
}
