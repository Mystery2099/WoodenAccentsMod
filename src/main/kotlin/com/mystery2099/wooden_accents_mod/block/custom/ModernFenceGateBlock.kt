package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FenceGateBlock
import net.minecraft.block.ShapeContext
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import java.util.function.Consumer

class ModernFenceGateBlock(baseGate: FenceGateBlock, val baseBlock: Block) : FenceGateBlock(FabricBlockSettings.copyOf(baseGate), baseGate.woodType),
    GroupedBlock, RecipeBlockData {
    private inline val defaultShapes
        get() = arrayOf(
            Block.createCuboidShape(0.0, 0.0, 7.5, 1.0, 15.0, 8.5),
            Block.createCuboidShape(15.0, 0.0, 7.5, 16.0, 15.0, 8.5),
            Block.createCuboidShape(11.0, 1.0, 7.5, 13.0, 16.0, 8.5),
            Block.createCuboidShape(3.0, 1.0, 7.5, 5.0, 16.0, 8.5),
            Block.createCuboidShape(7.0, 1.0, 7.5, 9.0, 15.0, 8.5),
            Block.createCuboidShape(0.0, 11.0, 7.0, 16.0, 14.0, 9.0),
            Block.createCuboidShape(0.0, 2.0, 7.0, 16.0, 5.0, 9.0),
            Block.createCuboidShape(0.0, 2.0, 7.5, 16.0, 14.0, 8.5)
        )
    private val shape1 = defaultShapes.combined
    private val shape2 = defaultShapes.rotateLeft()
    private inline val wallShapes
        get() = arrayOf(
            Block.createCuboidShape(0.0, 0.0, 7.0, 1.0, 14.0, 9.0),
            Block.createCuboidShape(15.0, 0.0, 7.0, 16.0, 14.0, 9.0),
            Block.createCuboidShape(1.0, 11.0, 7.0, 15.0, 14.0, 9.0),
            Block.createCuboidShape(1.0, 2.0, 7.0, 15.0, 5.0, 9.0),
            Block.createCuboidShape(1.0, 5.0, 7.5, 15.0, 11.0, 8.5),
            Block.createCuboidShape(11.0, 1.0, 7.5, 13.0, 16.0, 8.5),
            Block.createCuboidShape(3.0, 1.0, 7.5, 5.0, 16.0, 8.5),
            Block.createCuboidShape(7.0, 1.0, 7.5, 9.0, 15.0, 8.5)
        )
    private val wallShape1 = wallShapes.combined
    private val wallShape2 = wallShapes.rotateLeft()

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ) = when (state[FACING]) {
        Direction.NORTH, Direction.SOUTH -> if (state[IN_WALL]) wallShape1 else shape1
        Direction.EAST, Direction.WEST -> if (state[IN_WALL]) wallShape2 else shape2
        else -> super.getOutlineShape(state, world, pos, context)
    }
    override val itemGroup
        get() = ModItemGroups.outsideBlockItemGroup

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this).apply {
            input('#', baseBlock)
            input('|', Items.STICK)
            pattern("|#|")
            pattern("|#|")
            group("modern_fence_gates")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

}