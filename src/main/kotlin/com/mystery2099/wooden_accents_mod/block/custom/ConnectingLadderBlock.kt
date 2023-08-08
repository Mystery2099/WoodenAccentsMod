package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.enums.ConnectingLadderShape
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.isIn
import com.mystery2099.wooden_accents_mod.item_group.CreativeTab
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.unifyElements
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class ConnectingLadderBlock(val baseBlock: Block) : AbstractCustomLadderBlock(FabricBlockSettings.of(baseBlock.defaultState.material, baseBlock.defaultMapColor).apply {
    hardness(Blocks.LADDER.hardness)
    resistance(Blocks.LADDER.blastResistance)
    sounds(baseBlock.getSoundGroup(baseBlock.defaultState))
}) {
    override val tag: TagKey<Block>
        get() = ModBlockTags.connectingLadders
    override val itemGroup: CreativeTab
        get() = ModItemGroups.outsideBlockItemGroup

    init {
        defaultState = defaultState.withShape(left = false, right = false)
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
        val connectsLeft: Boolean = world.getBlockState(pos.offset(state[FACING].rotateYClockwise())).isConnectingLadder()
        val connectsRight: Boolean = world.getBlockState(pos.offset(state[FACING].rotateYCounterclockwise())).isConnectingLadder()
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
            .withShape(connectsLeft, connectsRight)
    }
    private fun BlockState.isConnectingLadder(): Boolean = this isIn tag
    private fun BlockState.withShape(left: Boolean, right: Boolean): BlockState = this.withIfExists(shape, run {
        when {
            left && right -> ConnectingLadderShape.CENTER
            left -> ConnectingLadderShape.RIGHT
            right -> ConnectingLadderShape.LEFT
            else -> ConnectingLadderShape.SINGLE
        }
    })

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(shape)
    }
    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = shapeMap[state[shape]]?.get(state[FACING]) ?: super.getOutlineShape(state, world, pos, context)

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        super.offerRecipe(exporter, baseBlock, 8, "connecting_ladder")
    }
    companion object {
        val shape = ModProperties.connectingLadderShape

        private val singleShapeArray = arrayOf(
            Block.createCuboidShape(2.0, 0.0, 15.0, 4.0, 16.0, 16.0),
            Block.createCuboidShape(12.0, 0.0, 15.0, 14.0, 16.0, 16.0),
            Block.createCuboidShape(2.0, 1.0, 14.5, 14.0, 15.0, 15.0)
        )

        private val leftShapeArray = arrayOf(
            Block.createCuboidShape(12.0, 0.0, 15.0, 14.0, 16.0, 16.0),
            Block.createCuboidShape(0.0, 1.0, 14.5, 14.0, 15.0, 15.0)
        )

        private val rightShapeArray = arrayOf(
            Block.createCuboidShape(2.0, 0.0, 15.0, 4.0, 16.0, 16.0),
            Block.createCuboidShape(2.0, 1.0, 14.5, 16.0, 15.0, 15.0)
        )

        private val singleShapeMap = mapOf(
            Direction.NORTH to singleShapeArray.unifyElements(),
            Direction.EAST to singleShapeArray.rotateLeft(),
            Direction.SOUTH to singleShapeArray.flip(),
            Direction.WEST to singleShapeArray.rotateRight()
        )
        private val centerShapeMap = mutableMapOf(
            Direction.NORTH to Block.createCuboidShape(0.0, 1.0, 14.5, 16.0, 15.0, 15.0)
        ).also {
            it[Direction.EAST] = it[Direction.NORTH]?.rotateLeft()
            it[Direction.SOUTH] = it[Direction.NORTH]?.flip()
            it[Direction.WEST] = it[Direction.NORTH]?.rotateRight()
        }.toMap()

        private val leftShapeMap = mapOf(
            Direction.NORTH to leftShapeArray.unifyElements(),
            Direction.EAST to leftShapeArray.rotateLeft(),
            Direction.SOUTH to leftShapeArray.flip(),
            Direction.WEST to leftShapeArray.rotateRight()
        )

        private val rightShapeMap = mapOf(
            Direction.NORTH to rightShapeArray.unifyElements(),
            Direction.EAST to rightShapeArray.rotateLeft(),
            Direction.SOUTH to rightShapeArray.flip(),
            Direction.WEST to rightShapeArray.rotateRight()
        )
        private val shapeMap = mapOf(
            ConnectingLadderShape.SINGLE to singleShapeMap,
            ConnectingLadderShape.CENTER to centerShapeMap,
            ConnectingLadderShape.LEFT to leftShapeMap,
            ConnectingLadderShape.RIGHT to rightShapeMap
        )

    }
}