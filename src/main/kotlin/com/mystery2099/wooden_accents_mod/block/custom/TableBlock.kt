package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.group
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.CreativeTab
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class TableBlock(val baseBlock: Block, val topBlock: Block) : AbstractTableBlock(baseBlock) {
    //init { WoodenAccentsModItemGroups.livingRoomItems += this }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = state.run {
        val north = this[north]
        val east = this[east]
        val south = this[south]
        val west = this[west]
        mutableListOf<VoxelShape>().apply {
            add(topShape)
            if (!north && !east && !south && !west) add(legShape)
            //Ends
            if (!north && !east && south && !west) add(northEndLegShape)
            if (!north && !east && !south && west) add(eastEndLegShape)
            if (north && !east && !south && !west) add(southEndLegShape)
            if (!north && east && !south && !west) add(westEndLegShape)
            //Corners
            if (!north && !east && south && west) add(northEastLegShape)
            if (!north && east && south && !west) add(northWestLegShape)
            if (north && !east && !south && west) add(southEastLegShape)
            if (north && east && !south && !west) add(southWestLegShape)

        }.combined
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', topBlock)
            input('|', baseBlock)
            pattern("###")
            pattern(" | ")
            pattern(" | ")
            group(this@TableBlock, "tables")
            requires(topBlock)
            offerTo(exporter)
        }
    }

    override val itemGroup: CreativeTab
        get() = ModItemGroups.kitchenItemGroup

    override fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        return getBlockState(pos.offset(direction)).block is TableBlock
    }

    companion object {
        val topShape: VoxelShape = createCuboidShape(0.0, 13.0, 0.0, 16.0, 16.0, 16.0)
        val legShape: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 13.0, 10.0)
        val northEndLegShape: VoxelShape = createCuboidShape(6.0, 0.0, 1.0, 10.0, 13.0, 5.0)
        val eastEndLegShape: VoxelShape = northEndLegShape.rotateLeft()
        val southEndLegShape : VoxelShape = northEndLegShape.flip()
        val westEndLegShape: VoxelShape = northEndLegShape.rotateRight()
        val northEastLegShape: VoxelShape = createCuboidShape(11.0, 0.0, 1.0, 15.0, 13.0, 5.0)
        val northWestLegShape: VoxelShape = northEastLegShape.rotateRight()
        val southEastLegShape: VoxelShape = northWestLegShape.flip()
        val southWestLegShape: VoxelShape = northEastLegShape.flip()
    }
}