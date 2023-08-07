package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableType
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class CoffeeTableBlock(val baseBlock: Block, val topBlock: Block) : AbstractTableBlock(baseBlock),
    GroupedBlock {
    init {
        defaultState = defaultState.with(type, CoffeeTableType.SHORT)
        //WoodenAccentsModItemGroups.livingRoomItems += this
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(type)
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        val isTall: Boolean = state[type] == CoffeeTableType.TALL
        val hasNorthConnection = state[north]
        val hasSouthConnection = state[south]
        val hasEastConnection = state[east]
        val hasWestConnection = state[west]

        return mutableListOf<VoxelShape>().apply {
            add(if (isTall) tallTopShape else shortTopShape)
            if (!hasNorthConnection) {
                if (!hasEastConnection) {
                    add(shortNorthEastLeg)
                    if (isTall) add(tallNorthEastLeg)
                }
                if (!hasWestConnection) {
                    add(shortNorthWestLeg)
                    if (isTall) add(tallNorthWestLeg)
                }
            }
            if (!hasSouthConnection) {
                if (!hasEastConnection) {
                    add(shortSouthEastLeg)
                    if (isTall) add(tallSouthEastLeg)
                }
                if (!hasWestConnection) {
                    add(shortSouthWestLeg)
                    if (isTall) add(tallSouthWestLeg)
                }
            }
        }.combined
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val state = ctx.world.getBlockState(ctx.blockPos)
        val isSneaking = ctx.player?.isSneaking
        return if (state.isOf(this) && (isSneaking == null || !isSneaking) ) state.with(type, CoffeeTableType.TALL) else super.getPlacementState(ctx)?.withIfExists(
            type, CoffeeTableType.SHORT)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "state.get(type) == CoffeeTableType.SHORT && context.stack.item == asItem()",
        "com.mystery2099.block.custom.CoffeeTableBlock.Companion.type",
        "com.mystery2099.block.custom.enums.CoffeeTableType"
    )
    )
    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        return state[type] == CoffeeTableType.SHORT && context.stack.item == asItem()
    }

    override fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        val here = getBlockState(pos)
        val there = getBlockState(pos.offset(direction))
        return there.block is CoffeeTableBlock && here[type] == there[type]
    }

    companion object {
        @JvmStatic
        val type = ModProperties.coffeeTableType

        private const val SHAPE_VERTICAL_OFFSET = 7.0 / 16

        // Top shapes
        @JvmStatic
        private val shortTopShape = createCuboidShape(0.0, 7.0, 0.0, 16.0, 9.0, 16.0)
        @JvmStatic
        private val tallTopShape = shortTopShape.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)

        // Short North Shapes
        @JvmStatic
        private val shortNorthEastLeg = createCuboidShape(13.75, 0.0, 0.25, 15.75, 7.0, 2.25)
        @JvmStatic
        private val shortNorthWestLeg = shortNorthEastLeg.rotateRight()

        // Short South Shapes
        @JvmStatic
        private val shortSouthEastLeg = shortNorthWestLeg.flip()
        @JvmStatic
        private val shortSouthWestLeg = shortNorthEastLeg.flip()

        // Tall North Shapes
        @JvmStatic
        private val tallNorthEastLeg = shortNorthEastLeg.offset(0.0, SHAPE_VERTICAL_OFFSET, 0.0)
        @JvmStatic
        private val tallNorthWestLeg = tallNorthEastLeg.rotateRight()

        // Tall South Shapes
        @JvmStatic
        private val tallSouthEastLeg = tallNorthWestLeg.flip()
        @JvmStatic
        private val tallSouthWestLeg = tallNorthEastLeg.flip()
    }

    override val itemGroup get() = ModItemGroups.livingRoomItemGroup
}
