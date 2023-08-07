package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import net.minecraft.block.Block
import net.minecraft.block.LadderBlock
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.function.Consumer

abstract class AbstractLadderBlock(settings: Settings) : LadderBlock(settings), GroupedBlock, RecipeBlockData, TaggedBlock {
    override val tag: TagKey<Block>
        get() = BlockTags.CLIMBABLE
    fun offerRecipe(exporter: Consumer<RecipeJsonProvider>, input: ItemConvertible, outputNum: Int, group: String) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, outputNum).apply {
            input('#', input)
            pattern("# #")
            pattern("###")
            pattern("# #")
            group(group)
            requires(input)
            offerTo(exporter)
        }

    }
}