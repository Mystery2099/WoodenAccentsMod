package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer.Companion.with
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.BracketShelfBlockEntity
import com.github.mystery2099.woodenAccentsMod.block.itemModelId
import com.github.mystery2099.woodenAccentsMod.block.textureId
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.withYRotationOf
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.MultipartBlockStateSupplier
import net.minecraft.data.client.TextureMap
import net.minecraft.data.client.VariantSettings
import net.minecraft.data.client.When
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import net.minecraft.world.event.GameEvent
import java.util.function.Consumer

class BracketShelfBlock(val baseBlock: Block) : AbstractWaterloggableBlock(
    FabricBlockSettings.copyOf(baseBlock).nonOpaque().apply {
        if (baseBlock.requiredFeatures.contains(FeatureFlags.UPDATE_1_20)) {
            requires(FeatureFlags.UPDATE_1_20)
        }
    }
), BlockEntityProvider, CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {

    override val itemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.bracketShelves

    init {
        defaultState = defaultState.with {
            facing to Direction.NORTH
            left to false
            right to false
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(facing, left, right)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val placementFacing = if (ctx.side.axis.isHorizontal) ctx.side else ctx.horizontalPlayerFacing.opposite
        return super.getPlacementState(ctx).with(facing, placementFacing).withBracketVisibility(ctx.world, ctx.blockPos)
    }

    @Deprecated("Deprecated in Java")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState {
        val updated = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        // Pop off when the mount is removed, like vanilla ladders. Contents are
        // scattered in onStateReplaced.
        if (direction == updated[facing].opposite && !updated.canPlaceAt(world, pos)) {
            return Blocks.AIR.defaultState
        }
        if (direction?.axis?.isHorizontal == true) {
            world.scheduleBlockTick(pos, this, 1)
        }
        return updated
    }

    private fun BlockState.withBracketVisibility(world: WorldAccess, pos: BlockPos): BlockState {
        val leftDirection = this[facing].rotateYCounterclockwise()
        val rightDirection = this[facing].rotateYClockwise()
        var distanceFromLeft = 0
        var cursor = pos.offset(leftDirection)
        while (canConnect(world.getBlockState(cursor))) {
            distanceFromLeft++
            cursor = cursor.offset(leftDirection)
        }
        val hasRightNeighbor = canConnect(world.getBlockState(pos.offset(rightDirection)))
        return this.with {
            left to (distanceFromLeft > 0)
            right to (hasRightNeighbor && (distanceFromLeft + 1) % SHELVES_PER_SECTION != 0)
        }
    }

    private fun BlockState.canConnect(other: BlockState): Boolean =
        other.block === this.block && other[facing] == this[facing]

    @Deprecated("Deprecated in Java")
    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val leftDirection = state[facing].rotateYCounterclockwise()
        val rightDirection = state[facing].rotateYClockwise()
        var start = pos
        while (state.canConnect(world.getBlockState(start.offset(leftDirection)))) {
            start = start.offset(leftDirection)
        }

        var cursor = start
        while (state.canConnect(world.getBlockState(cursor))) {
            val currentState = world.getBlockState(cursor)
            val updatedState = currentState.withBracketVisibility(world, cursor)
            if (updatedState != currentState) {
                world.setBlockState(cursor, updatedState, Block.NOTIFY_LISTENERS)
            }
            cursor = cursor.offset(rightDirection)
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        BracketShelfBlockEntity(pos, state)

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        // Drain water first so an empty bucket can be used in the same spot it was filled with.
        if (state[waterlogged] && player.getStackInHand(hand).isOf(Items.BUCKET)) {
            player.setStackInHand(
                hand,
                ItemUsage.exchangeStack(player.getStackInHand(hand), player, ItemStack(Items.WATER_BUCKET))
            )
            world.setBlockState(pos, state.with(waterlogged, false))
            world.playSound(
                null,
                pos,
                SoundEvents.ITEM_BUCKET_FILL,
                SoundCategory.BLOCKS,
                1.0f,
                1.0f
            )
            world.emitGameEvent(player, GameEvent.FLUID_PICKUP, pos)
            return ActionResult.CONSUME
        }
        if (hand == Hand.OFF_HAND) return ActionResult.PASS

        val blockEntity = world.getBlockEntity(pos) as? BracketShelfBlockEntity ?: return ActionResult.PASS
        val stackInHand = player.getStackInHand(hand)

        if (world.isReceivingRedstonePower(pos)) {
            swapWithHotbar(world, pos, state[facing], player)
        } else {
            val slot = slotFromHit(hit, pos, state[facing])
            val shelfStack = blockEntity.getStack(slot)
            if (player.isSneaking && stackInHand.isEmpty) {
                blockEntity.setStack(slot, ItemStack.EMPTY)
                player.setStackInHand(hand, shelfStack)
            } else {
                blockEntity.setStack(slot, stackInHand.copy())
                player.setStackInHand(hand, shelfStack)
            }
            playInteractionSound(world, pos, shelfStack, stackInHand)
        }
        return ActionResult.CONSUME
    }

    /** Swaps the main-hand stack with the slot under the cursor; the shelf's
     * first slot is the one closest to [facing]'s counterclockwise side. */
    private fun slotFromHit(hit: BlockHitResult, pos: BlockPos, facing: Direction): Int {
        val leftDirection = facing.rotateYCounterclockwise()
        val hitPos = hit.pos
        val localX = hitPos.x - pos.x
        val localZ = hitPos.z - pos.z
        val alongLeft = if (leftDirection.axis == Direction.Axis.X) {
            if (leftDirection.direction == Direction.AxisDirection.POSITIVE) localX else 1.0 - localX
        } else {
            if (leftDirection.direction == Direction.AxisDirection.POSITIVE) localZ else 1.0 - localZ
        }
        // alongLeft is 0.0 at the counterclockwise edge, i.e. at the first slot.
        return ((1.0 - alongLeft) * 3.0).toInt().coerceIn(0, BracketShelfBlockEntity.SLOT_COUNT - 1)
    }

    /** A powered shelf swaps its slots (and its powered neighbours', up to three
     * shelves, all facing [facing]) with the rightmost hotbar slots. */
    private fun swapWithHotbar(world: World, clickedPos: BlockPos, facing: Direction, player: PlayerEntity) {
        val leftDirection = facing.rotateYCounterclockwise()
        val rightDirection = facing.rotateYClockwise()

        val leftEnd = generateSequence(clickedPos, { it.offset(leftDirection) })
            .takeWhile { isPoweredShelf(it, world, facing) }
            .take(3)
            .last()
        val group = generateSequence(leftEnd, { it.offset(rightDirection) })
            .takeWhile { isPoweredShelf(it, world, facing) }
            .take(3)
            .toList()

        val firstHotbarSlot = PlayerInventory.getHotbarSize() - group.size * BracketShelfBlockEntity.SLOT_COUNT
        group.forEachIndexed { groupIndex, shelfPos ->
            val shelf = world.getBlockEntity(shelfPos) as? BracketShelfBlockEntity ?: return@forEachIndexed
            repeat(BracketShelfBlockEntity.SLOT_COUNT) { slot ->
                val hotbarSlot = firstHotbarSlot + groupIndex * BracketShelfBlockEntity.SLOT_COUNT + slot
                val shelfStack = shelf.getStack(slot)
                shelf.setStack(slot, player.inventory.getStack(hotbarSlot))
                player.inventory.setStack(hotbarSlot, shelfStack)
            }
        }
        player.inventory.markDirty()
        world.playSound(
            null,
            clickedPos,
            SoundEvents.BLOCK_DISPENSER_DISPENSE,
            SoundCategory.BLOCKS,
            0.8f,
            1.0f
        )
    }

    private fun isPoweredShelf(pos: BlockPos, world: World, shelfFacing: Direction): Boolean {
        val state = world.getBlockState(pos)
        return state.block is BracketShelfBlock &&
            state[facing] == shelfFacing &&
            world.isReceivingRedstonePower(pos)
    }

    private fun playInteractionSound(world: World, pos: BlockPos, shelfStack: ItemStack, handStack: ItemStack) {
        val sound = when {
            shelfStack.isEmpty && handStack.isEmpty -> return
            shelfStack.isEmpty -> SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM
            handStack.isEmpty -> SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM
            else -> SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM
        }
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.5f, 0.9f + world.random.nextFloat() * 0.2f)
    }

    @Deprecated("Deprecated in Java")
    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean =
        world.getBlockState(pos.offset(state[facing].opposite)).isSideSolidFullSquare(
            world,
            pos.offset(state[facing].opposite),
            state[facing]
        )

    @Deprecated("Deprecated in Java")
    override fun hasComparatorOutput(state: BlockState?): Boolean = true

    /** Left, middle, and right slots contribute 1, 2, and 4 respectively. */
    @Deprecated("Deprecated in Java")
    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        val shelf = world.getBlockEntity(pos) as? BracketShelfBlockEntity ?: return 0
        var signal = 0
        repeat(BracketShelfBlockEntity.SLOT_COUNT) { slot ->
            if (!shelf.getStack(slot).isEmpty) signal = signal or (1 shl slot)
        }
        return signal
    }

    @Deprecated("Deprecated in Java")
    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if (!state.isOf(newState.block) && !world.isClient) {
            // Covers both player breaks and support-loss pop-offs.
            world.getBlockEntity(pos)?.let { if (it is BracketShelfBlockEntity) ItemScatterer.spawn(world, pos, it) }
            world.updateComparators(pos, state.block)
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        val direction = state[facing]
        var shape = shelfShapeByFacing.getValue(direction)
        if (!state[left]) shape = VoxelShapes.union(shape, leftBracketShapeByFacing.getValue(direction))
        if (!state[right]) shape = VoxelShapes.union(shape, rightBracketShapeByFacing.getValue(direction))
        return shape
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 2).apply {
            input('#', baseBlock)
            input('|', Items.STICK)
            pattern("###")
            pattern("| |")
            customGroup(this@BracketShelfBlock, "bracket_shelves")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val textures = TextureMap.all(baseBlock)
        val baseModel = ModModels.bracketShelfBase.upload(this, textures, generator.modelCollector)
        val leftBracketModel = ModModels.bracketShelfLeft.upload(this, textures, generator.modelCollector)
        val rightBracketModel = ModModels.bracketShelfRight.upload(this, textures, generator.modelCollector)
        ModModels.bracketShelfItem.upload(itemModelId, textures, generator.modelCollector)

        val supplier = MultipartBlockStateSupplier.create(this)
        val rotations = mapOf(
            Direction.NORTH to VariantSettings.Rotation.R0,
            Direction.EAST to VariantSettings.Rotation.R90,
            Direction.SOUTH to VariantSettings.Rotation.R180,
            Direction.WEST to VariantSettings.Rotation.R270
        )
        rotations.forEach { (direction, rotation) ->
            val facingCondition = When.create().set(facing, direction)
            supplier.with(facingCondition, baseModel.asBlockStateVariant().withYRotationOf(rotation))
            supplier.with(
                When.allOf(facingCondition, When.create().set(left, false)),
                leftBracketModel.asBlockStateVariant().withYRotationOf(rotation)
            )
            supplier.with(
                When.allOf(facingCondition, When.create().set(right, false)),
                rightBracketModel.asBlockStateVariant().withYRotationOf(rotation)
            )
        }
        generator.blockStateCollector.accept(supplier)
    }

    companion object {
        private const val SHELVES_PER_SECTION = 3

        val facing = Properties.HORIZONTAL_FACING
        val left: BooleanProperty = com.github.mystery2099.woodenAccentsMod.state.property.ModProperties.left
        val right: BooleanProperty = com.github.mystery2099.woodenAccentsMod.state.property.ModProperties.right

        private val northShelfShape = VoxelAssembly.createCuboidShape(0, 11, 6, 16, 15, 16)
        private val northLeftBracketShape = VoxelAssembly.createCuboidShape(2, 3, 7, 4, 11, 16)
        private val northRightBracketShape = VoxelAssembly.createCuboidShape(12, 3, 7, 14, 11, 16)

        private val shelfShapeByFacing = shapesByFacing(northShelfShape)
        private val leftBracketShapeByFacing = shapesByFacing(northLeftBracketShape)
        private val rightBracketShapeByFacing = shapesByFacing(northRightBracketShape)

        private fun shapesByFacing(northShape: VoxelShape) = mapOf(
            Direction.NORTH to northShape,
            Direction.EAST to northShape.rotateLeft(),
            Direction.SOUTH to northShape.flip(),
            Direction.WEST to northShape.rotateRight()
        )
    }
}
