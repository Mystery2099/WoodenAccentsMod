package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.entity.ModEntities
import com.mystery2099.wooden_accents_mod.entity.custom.SeatEntity
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flipped
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedRight
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.client.TexturedModel
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.function.Consumer


class ChairBlock(settings: Settings, val baseBlock: Block) : HorizontalFacingBlock(settings), Waterloggable,
    CustomBlockStateProvider, CustomItemGroupProvider,
    CustomRecipeProvider, CustomTagProvider<Block> {

    override val itemGroup: CustomItemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.chairs

    init {
        this.defaultState = defaultState.with(FACING, Direction.NORTH)
    }

    constructor(baseBlock: Block) : this(FabricBlockSettings.copyOf(baseBlock), baseBlock)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return defaultState.with(FACING, ctx.horizontalPlayerFacing.opposite)

    }

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState?,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        return if (world.isClient) {
            ActionResult.SUCCESS
        } else {
            val seat = SeatEntity(
                ModEntities.seatEntity,
                world
            ) // create a new seat entity
            seat.updatePosition(
                pos.x + 0.5,
                pos.y + 0.25,
                pos.z + 0.5
            )
            world.spawnEntity(seat)
            player.startRiding(seat)
            ActionResult.CONSUME
        }
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
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val texturedModel = TexturedModel.makeFactory({ TextureMap.all(baseBlock) }, ModModels.basicChair)
        generator.registerNorthDefaultHorizontalRotated(this, texturedModel)
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {

    }
    companion object {
        private val northShape = VoxelShapeHelper.union(
            VoxelShapeHelper.createCuboidShape(2, 0, 2, 4, 8, 4),
            VoxelShapeHelper.createCuboidShape(12, 0, 2, 14, 8, 4),
            VoxelShapeHelper.createCuboidShape(12, 0, 12, 14, 8, 14),
            VoxelShapeHelper.createCuboidShape(2, 0, 12, 4, 8, 14),
            VoxelShapeHelper.createCuboidShape(2, 10, 12, 14, 16, 14),
            VoxelShapeHelper.createCuboidShape(2, 16, 12, 14, 20, 14),
            VoxelShapeHelper.createCuboidShape(2, 8, 2, 14, 10, 14)
        )
        val eastShape = northShape.rotatedLeft
        val southShape = northShape.flipped
        val westShape = northShape.rotatedRight
    }
}