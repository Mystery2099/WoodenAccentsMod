package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import net.minecraft.block.Block
import net.minecraft.block.LadderBlock
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.function.Consumer
/**
 * An abstract class representing a custom ladder block.
 *
 * @param settings The settings for the ladder block.
 */
abstract class AbstractCustomLadderBlock(settings: Settings) : LadderBlock(settings), CustomItemGroupProvider,
    CustomRecipeProvider,
    CustomTagProvider<Block>, CustomBlockStateProvider {
    override val tag: TagKey<Block> = BlockTags.CLIMBABLE
    override val itemGroup: CustomItemGroup = ModItemGroups.structuralElements

    /**
     * Offer a recipe for creating this ladder block.
     *
     * @param exporter The consumer for exporting the recipe.
     * @param input The input item convertible used in the recipe.
     * @param outputNum The number of ladder blocks to be produced in the recipe.
     * @param group The recipe group to which this recipe belongs.
     */
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