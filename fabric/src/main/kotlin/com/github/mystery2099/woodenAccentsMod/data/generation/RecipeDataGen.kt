package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.isPlank
import com.github.mystery2099.woodenAccentsMod.block.isStripped
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

class RecipeDataGen(output: FabricDataOutput,
                    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, registriesFuture) {

    override fun buildRecipes(recipeExporter: RecipeOutput) {
        ModBlocks.blocks.filterIsInstance<CustomRecipeProvider>().forEach {
            it.offerRecipeTo(recipeExporter)
        }
    }

    companion object {
        /** Adds the recipe-book unlock criterion for [requiredItem]. */
        fun ShapedRecipeBuilder.requires(requiredItem: ItemLike): ShapedRecipeBuilder {
            return unlockedBy(getHasName(requiredItem), has(requiredItem))
        }

        /** Adds the recipe-book unlock criterion for [requiredTag]. */
        fun ShapedRecipeBuilder.requires(requiredTag: TagKey<Item>): ShapedRecipeBuilder {
            return unlockedBy("has_${requiredTag.location()}", has(requiredTag))
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

}
