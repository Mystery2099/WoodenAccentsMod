package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.CreativeTab
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import java.util.function.Consumer

class ThinBookshelfBlock(val baseBlock: Block) :
    ChiseledBookshelfBlock(FabricBlockSettings.copyOf(baseBlock).requires(FeatureFlags.UPDATE_1_20)),
    GroupedBlock, RecipeBlockData, TaggedBlock {
    override val tag: TagKey<Block>
        get() = ModBlockTags.thinBookshelves
    override val itemGroup: CreativeTab
        get() = ModItemGroups.livingRoomItemGroup

    companion object {
        private val northShape = Block.createCuboidShape(0.0, 0.0, 11.0, 16.0, 16.0, 16.0)!!
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = when (state.get(HorizontalFacingBlock.FACING)) {
        Direction.NORTH -> northShape
        Direction.EAST -> northShape.rotateLeft()
        Direction.SOUTH -> northShape.flip()
        Direction.WEST -> northShape.rotateRight()
        else -> super.getOutlineShape(state, world, pos, context)
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 2).apply {
            input('#', baseBlock)
            input('_', Ingredient.fromTag(ItemTags.WOODEN_SLABS))
            pattern("##")
            pattern("__")
            pattern("##")
            group("thin_bookshelves")
            requires(baseBlock)
            offerTo(exporter)
        }
    }


}