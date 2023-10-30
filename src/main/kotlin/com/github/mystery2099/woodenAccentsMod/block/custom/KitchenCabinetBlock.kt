package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly.and
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.textureId
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.KitchenCabinetBlockEntity
import com.github.mystery2099.woodenAccentsMod.data.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.data.ModItemTags
import com.github.mystery2099.woodenAccentsMod.data.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.isOf
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.withProperties
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.asBlockStateVariant
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
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
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.function.Consumer

/**
 * Kitchen cabinet block
 *
 * @property baseBlock
 * @property topBlock
 * @constructor Create Kitchen cabinet block from the block settings of another block
 */
class KitchenCabinetBlock(val baseBlock: Block, private val topBlock: Block) :
    BlockWithEntity(FabricBlockSettings.copyOf(baseBlock)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {

    override val tag: TagKey<Block> = ModBlockTags.kitchenCabinets
    override val itemGroup = ModItemGroups.decorations

    init {
        defaultState = stateManager.defaultState.withProperties {
            facing setTo Direction.NORTH
            open setTo false
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
        if (state isOf newState.block) return
        with(world.getBlockEntity(pos)) {
            if (this is Inventory) {
                ItemScatterer.spawn(world, pos, this)
                world.updateComparators(pos, this@KitchenCabinetBlock)
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "(world.getBlockEntity(pos) as? KitchenCabinetBlockEntity)?.also { it.tick() }",
        "com.mystery2099.wooden_accents_mod.block_entity.custom.KitchenCabinetBlockEntity"
    )
    )
    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        (world.getBlockEntity(pos) as? KitchenCabinetBlockEntity)?.also {
            it.tick()
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = KitchenCabinetBlockEntity(pos, state)

    @Deprecated("Deprecated in Java", ReplaceWith("BlockRenderType.MODEL", "net.minecraft.block.BlockRenderType"))
    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL
    override fun onPlaced(
        world: World,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        world.getBlockEntity(pos)?.let {
            if (itemStack.hasCustomName() && it is KitchenCabinetBlockEntity) {
                it.customName = itemStack.name
            }
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("true"))
    override fun hasComparatorOutput(state: BlockState?): Boolean = true

    @Deprecated("Deprecated in Java", ReplaceWith(
        "ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))",
        "net.minecraft.screen.ScreenHandler"
    )
    )
    override fun getComparatorOutput(state: BlockState?, world: World, pos: BlockPos?): Int {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "state.withProperties { facing setTo rotation.rotate(state[facing]) }",
        "com.mystery2099.wooden_accents_mod.util.BlockStateUtil.withProperties",
        "com.mystery2099.wooden_accents_mod.block.custom.KitchenCabinetBlock.Companion.facing",
        "com.mystery2099.wooden_accents_mod.block.custom.KitchenCabinetBlock.Companion.facing"
    )
    )
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState = state.withProperties {
        facing setTo rotation.rotate(state[facing])
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "state.apply { rotate(mirror.getRotation(get(facing))) }",
        "com.mystery2099.wooden_accents_mod.block.custom.KitchenCabinetBlock.Companion.facing"
    )
    )
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
    ): VoxelShape = directionVoxelShapeMap[state[facing]]?.and(AbstractKitchenCounterBlock.TOP_SHAPE)
        ?: VoxelShapes.fullCube()

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', baseBlock)
            input('_', topBlock)
            input('O', ModItemTags.chests)
            pattern("___")
            pattern("#O#")
            pattern("###")
            customGroup(this@KitchenCabinetBlock, "kitchen_cabinets")
            requires(ModBlockTags.getItemTagFrom(ModBlockTags.kitchenCounters))
            offerTo(exporter)
        }
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

    companion object {
        val facing: DirectionProperty = Properties.HORIZONTAL_FACING
        val open: BooleanProperty = Properties.OPEN
        val directionVoxelShapeMap = mapOf(
            Direction.NORTH to AbstractKitchenCounterBlock.NORTH_SHAPE,
            Direction.EAST to AbstractKitchenCounterBlock.NORTH_SHAPE.rotateLeft(),
            Direction.SOUTH to AbstractKitchenCounterBlock.NORTH_SHAPE.flip(),
            Direction.WEST to AbstractKitchenCounterBlock.NORTH_SHAPE.rotateRight()
        )
    }
}