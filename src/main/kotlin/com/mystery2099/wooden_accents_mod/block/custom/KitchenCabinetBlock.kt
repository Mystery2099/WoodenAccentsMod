package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.BlockStateGeneratorDataBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.block_entity.custom.KitchenCabinetBlockEntity
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModItemTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.customGroup
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.unifiedWith
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.client.VariantsBlockStateSupplier
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.function.Consumer

class KitchenCabinetBlock(val baseBlock: Block, val topBlock: Block) :
    BlockWithEntity(FabricBlockSettings.copyOf(baseBlock)),
    GroupedBlock, RecipeBlockData, TaggedBlock, BlockStateGeneratorDataBlock {
    override val tag: TagKey<Block> = ModBlockTags.kitchenCabinets
    override val itemGroup = ModItemGroups.decorations

    init {
        defaultState = stateManager.defaultState.with(facing, Direction.NORTH).with(open, false)
        kitchenCabinetBlockEntityTypeBuilder.addBlock(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is KitchenCabinetBlockEntity) {
            player.openHandledScreen(blockEntity)
            PiglinBrain.onGuardedBlockInteracted(player, true)
        }
        return ActionResult.CONSUME
    }

    @Deprecated("Deprecated in Java")
    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        newState: BlockState,
        moved: Boolean
    ) {
        if (state.isOf(newState.block)) return
        with(world.getBlockEntity(pos)) {
            if (this is Inventory) {
                ItemScatterer.spawn(world, pos, this)
                world.updateComparators(pos, this@KitchenCabinetBlock)
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    @Deprecated("Deprecated in Java")
    override fun scheduledTick(state: BlockState?, world: ServerWorld, pos: BlockPos?, random: Random?) {
        world.getBlockEntity(pos)?.let { if (it is KitchenCabinetBlockEntity) it.tick() }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        KitchenCabinetBlockEntity(pos, state)

    @Deprecated("Deprecated in Java")
    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL

    override fun onPlaced(
        world: World,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        with(world.getBlockEntity(pos)) {
            if (itemStack.hasCustomName() && this is KitchenCabinetBlockEntity) {
                this.customName = itemStack.name
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun hasComparatorOutput(state: BlockState?): Boolean = true

    @Deprecated("Deprecated in Java")
    override fun getComparatorOutput(state: BlockState?, world: World, pos: BlockPos?): Int {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
    }

    @Deprecated("Deprecated in Java")
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState = state.apply {
        with(facing, rotation.rotate(get(facing)))
    }

    @Deprecated("Deprecated in Java")
    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState = state.apply {
        rotate(mirror.getRotation(get(facing)))
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(facing, open)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(facing, ctx.horizontalPlayerFacing.opposite)

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = directionVoxelShapeMap[state[facing]]?.unifiedWith(AbstractKitchenCounterBlock.TOP_SHAPE)
        ?: super.getOutlineShape(state, world, pos, context)

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', baseBlock)
            input('_', topBlock)
            input('O', ModItemTags.chests)
            pattern("___")
            pattern("#O#")
            pattern("###")
            customGroup(this@KitchenCabinetBlock, "kitchen_cabinets")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

    companion object {
        val facing: DirectionProperty = Properties.HORIZONTAL_FACING
        val open: BooleanProperty = Properties.OPEN
        val kitchenCabinetBlockEntityTypeBuilder: FabricBlockEntityTypeBuilder<KitchenCabinetBlockEntity> =
            FabricBlockEntityTypeBuilder.create(::KitchenCabinetBlockEntity)
        val directionVoxelShapeMap = mapOf(
            Direction.NORTH to AbstractKitchenCounterBlock.NORTH_SHAPE,
            Direction.EAST to AbstractKitchenCounterBlock.NORTH_SHAPE.rotateLeft(),
            Direction.SOUTH to AbstractKitchenCounterBlock.NORTH_SHAPE.flip(),
            Direction.WEST to AbstractKitchenCounterBlock.NORTH_SHAPE.rotateRight()
        )
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val map = TextureMap().apply {
            put(TextureKey.TOP, topBlock.textureId)
            put(TextureKey.SIDE, baseBlock.textureId)
        }
        val model = ModModels.kitchenCabinet.upload(this, map, generator.modelCollector)
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(this, model.asBlockStateVariant())
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
        )
        generator.registerParentedItemModel(this, model)
    }
}