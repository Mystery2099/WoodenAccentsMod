package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsMod
import com.mystery2099.WoodenAccentsMod.unionWith
import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.block_entity.custom.KitchenCabinetBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stats
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

class KitchenCabinetBlock(val baseBlock : Block, val topBlock : Block) : BlockWithEntity(FabricBlockSettings.copyOf(baseBlock)) {
    companion object {
        val facing: DirectionProperty = Properties.HORIZONTAL_FACING
        val open: BooleanProperty = Properties.OPEN
        val kitchenCabinetBlockEntityTypeBuilder: FabricBlockEntityTypeBuilder<KitchenCabinetBlockEntity> =
            FabricBlockEntityTypeBuilder.create(::KitchenCabinetBlockEntity)

    }
    init {
        defaultState = stateManager.defaultState.with(facing, Direction.NORTH).with(open, false)
        WoodenAccentsModItemGroups.kitchenItems += this
        WoodenAccentsModItemGroups.storageBlocks += this
        kitchenCabinetBlockEntityTypeBuilder.addBlock(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState?,
        world: World,
        pos: BlockPos?,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is KitchenCabinetBlockEntity) {
            player.openHandledScreen(blockEntity)
            //player.incrementStat(Stats.OPEN_BARREL)
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
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is Inventory) {
            ItemScatterer.spawn(world, pos, blockEntity as Inventory)
            world.updateComparators(pos, this)
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    @Deprecated("Deprecated in Java")
    override fun scheduledTick(state: BlockState?, world: ServerWorld, pos: BlockPos?, random: Random?) {
        val blockEntity = world.getBlockEntity(pos)!!
        if (blockEntity is KitchenCabinetBlockEntity) blockEntity.tick()
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return KitchenCabinetBlockEntity(pos, state)
    }

    @Deprecated("Deprecated in Java")
    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        lateinit var blockEntity: BlockEntity
        if (itemStack.hasCustomName() && (world.getBlockEntity(pos).apply { blockEntity = this!! }) is KitchenCabinetBlockEntity) {
            (blockEntity as KitchenCabinetBlockEntity).customName = itemStack.name
        }
    }

    @Deprecated("Deprecated in Java")
    override fun hasComparatorOutput(state: BlockState?): Boolean {
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun getComparatorOutput(state: BlockState?, world: World, pos: BlockPos?): Int {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
    }

    @Deprecated("Deprecated in Java")
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(facing, rotation.rotate(state.get(facing)))
    }

    @Deprecated("Deprecated in Java")
    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState? {
        return state.rotate(mirror.getRotation(state.get(facing)))
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(facing, open)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(facing, ctx.horizontalPlayerFacing.opposite)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return when (state.get(facing)) {
            Direction.NORTH -> KitchenCounterBlock.NORTH_SHAPE
            Direction.EAST -> KitchenCounterBlock.EAST_SHAPE
            Direction.SOUTH -> KitchenCounterBlock.SOUTH_SHAPE
            Direction.WEST -> KitchenCounterBlock.WEST_SHAPE
            else -> VoxelShapes.fullCube()
        }.unionWith(KitchenCounterBlock.TOP_SHAPE)
    }
}