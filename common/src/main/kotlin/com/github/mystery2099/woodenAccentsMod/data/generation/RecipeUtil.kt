package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.isPlank
import com.github.mystery2099.woodenAccentsMod.block.isStripped
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeUtil

/** Recipe-builder conveniences used by blocks that own their recipe definitions. */
object RecipeUtil {
    /** Adds the recipe-book unlock criterion for [requiredItem]. */
    fun ShapedRecipeBuilder.requires(requiredItem: ItemLike): ShapedRecipeBuilder {
        return unlockedBy(RecipeProvider.getHasName(requiredItem), RecipeProvider.has(requiredItem))
    }

    /** Adds the recipe-book unlock criterion for [requiredTag]. */
    fun ShapedRecipeBuilder.requires(requiredTag: TagKey<Item>): ShapedRecipeBuilder {
        return unlockedBy("has_${requiredTag.location()}", RecipeProvider.has(requiredTag))
    }

    fun ShapedRecipeBuilder.customGroup(block: Block, name: String): ShapedRecipeBuilder {
        return group(
            when {
                block.isStripped -> "stripped_$name"
                block.isPlank -> "plank_$name"
                else -> name
            }
        )
    }
}
