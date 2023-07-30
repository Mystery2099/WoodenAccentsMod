package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.block_entity.custom.KitchenCabinetBlockEntity
import com.mystery2099.util.VoxelShapeHelper.rotate
import com.mystery2099.util.VoxelShapeHelper.union
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
        defaultState = stateManager.defaultState.apply {
            with(facing, Direction.NORTH)
            with(open, false)
        }
        WoodenAccentsModItemGroups.run {
            kitchenItems += this@KitchenCabinetBlock
            storageBlocks += this@KitchenCabinetBlock
        }
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
        world.getBlockEntity(pos)!!.let { if (it is KitchenCabinetBlockEntity) it.tick() }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = KitchenCabinetBlockEntity(pos, state)

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
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = when (state.get(facing)) {
        Direction.NORTH -> KitchenCounterBlock.NORTH_SHAPE
        Direction.EAST -> KitchenCounterBlock.NORTH_SHAPE.rotate(Direction.SOUTH)
        Direction.SOUTH -> KitchenCounterBlock.NORTH_SHAPE.rotate(Direction.WEST)
        Direction.WEST -> KitchenCounterBlock.NORTH_SHAPE.rotate(Direction.NORTH)
        else -> VoxelShapes.fullCube()
    }.union(KitchenCounterBlock.TOP_SHAPE)
}