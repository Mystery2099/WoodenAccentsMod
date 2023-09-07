package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.generation.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import net.minecraft.block.Block
import net.minecraft.block.LadderBlock
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.function.Consumer

abstract class AbstractCustomLadderBlock(settings: Settings) : LadderBlock(settings), CustomItemGroupProvider,
    CustomRecipeProvider,
    CustomTagProvider<Block>, CustomBlockStateProvider {
    override val tag: TagKey<Block> = BlockTags.CLIMBABLE
    override val itemGroup: CustomItemGroup = ModItemGroups.structuralElements

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