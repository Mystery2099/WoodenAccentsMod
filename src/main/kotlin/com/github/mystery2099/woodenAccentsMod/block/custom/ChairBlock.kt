package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.combination.VoxelAssembly.and
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.data.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.data.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.entity.ModEntities
import com.github.mystery2099.woodenAccentsMod.entity.custom.SeatEntity
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.withProperties
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.client.TexturedModel
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import java.util.function.Consumer


class ChairBlock(settings: Settings, val baseBlock: Block) : HorizontalFacingBlock(settings), Waterloggable,
    CustomBlockStateProvider, CustomItemGroupProvider,
    CustomRecipeProvider, CustomTagProvider<Block> {

    override val itemGroup: CustomItemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.chairs

    init {
        this.defaultState = defaultState.withProperties {
            waterlogged setTo false
            FACING setTo Direction.NORTH
        }
    }

    constructor(baseBlock: Block) : this(FabricBlockSettings.copyOf(baseBlock), baseBlock)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, waterlogged)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.withProperties {
            waterlogged setTo ctx.world.getFluidState(ctx.blockPos).isOf(Fluids.WATER)
            FACING setTo ctx.horizontalPlayerFacing.opposite
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult = if (world.isClient) {
        ActionResult.SUCCESS // do nothing on the client side
    } else {
        val seat = SeatEntity(
            ModEntities.seatEntity,
            world
        ) // create a new seat entity
        seat.updatePosition(
            pos.x + 0.5,
            pos.y + 0.3,
            pos.z + 0.5
        )
        world.spawnEntity(seat)
        player.startRiding(seat)
        player.headYaw = state[FACING].asRotation()
        ActionResult.CONSUME
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = when (state[FACING]) {
        Direction.NORTH -> northShape
        Direction.EAST -> eastShape
        Direction.SOUTH -> southShape
        Direction.WEST -> westShape
        else -> VoxelShapes.fullCube()
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "state.also { if (it[waterlogged]) world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world)) }",
        "com.mystery2099.wooden_accents_mod.block.custom.ChairBlock.Companion.waterlogged",
        "net.minecraft.fluid.Fluids",
        "net.minecraft.fluid.Fluids"
    )
    )
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState = state.also {
        if (it[waterlogged]) world.scheduleFluidTick(
            pos,
            Fluids.WATER,
            Fluids.WATER.getTickRate(world)
        )
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "if (state[waterlogged]) Fluids.WATER.getStill(false) else super.getFluidState(state)",
        "com.mystery2099.wooden_accents_mod.block.custom.ChairBlock.Companion.waterlogged",
        "net.minecraft.fluid.Fluids",
        "net.minecraft.block.HorizontalFacingBlock"
    )
    )
    override fun getFluidState(state: BlockState): FluidState {
        return if (state[waterlogged]) Fluids.WATER.getStill(false)
        else Fluids.EMPTY.defaultState
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val texturedModel = TexturedModel.makeFactory({ TextureMap.all(baseBlock) }, ModModels.basicChair)
        generator.registerNorthDefaultHorizontalRotated(this, texturedModel)
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 3).apply {
            input('#', baseBlock)
            input('|', Items.STICK)
            pattern("#  ")
            pattern("###")
            pattern("| |")
            customGroup(this@ChairBlock, "chairs")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

    companion object {
        val waterlogged: BooleanProperty = Properties.WATERLOGGED
        private val northTopShape = VoxelAssembly.createCuboidShape(2, 10, 12, 14, 20, 14)
        private val bottomShape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(2, 8, 2, 14, 10, 14),
            VoxelAssembly.createCuboidShape(2, 0, 2, 4, 8, 4),
            VoxelAssembly.createCuboidShape(12, 0, 2, 14, 8, 4),
            VoxelAssembly.createCuboidShape(12, 0, 12, 14, 8, 14),
            VoxelAssembly.createCuboidShape(2, 0, 12, 4, 8, 14)
        )

        private val northShape = northTopShape and bottomShape
        private val eastShape = northTopShape.rotateLeft() and bottomShape
        private val southShape = northTopShape.flip() and bottomShape
        private val westShape = northTopShape.rotateRight() and bottomShape
    }
}