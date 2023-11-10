package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.isPlank
import com.github.mystery2099.woodenAccentsMod.block.isStripped
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey
import java.util.function.Consumer

/**
 * [RecipeDataGen] is responsible for generating custom recipes for the Wooden Accents Mod.
 * It extends [FabricRecipeProvider], which is used for generating recipe data.
 *
 * @param output The data output to which the generated recipes will be written.
 */
class RecipeDataGen(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        ModBlocks.blocks.filterIsInstance<CustomRecipeProvider>().forEach {
            it.offerRecipeTo(exporter)
        }
    }

    companion object {
        /**
         * Specify a required [Item] for the recipe.
         *
         * @param requiredItem The required [Item] for the recipe.
         * @return The [ShapedRecipeJsonBuilder] with the specified requirement.
         */
        fun ShapedRecipeJsonBuilder.requires(requiredItem: ItemConvertible): ShapedRecipeJsonBuilder {
            return criterion(hasItem(requiredItem), conditionsFromItem(requiredItem))
        }

        /**
         * Specify a required item tag for the recipe.
         *
         * @param requiredTag The required item tag for the recipe.
         * @return The [ShapedRecipeJsonBuilder] with the specified requirement.
         */
        fun ShapedRecipeJsonBuilder.requires(requiredTag: TagKey<Item>): ShapedRecipeJsonBuilder {
            return criterion("has_${requiredTag.id}", conditionsFromTag(requiredTag))
        }

        /**
         * Set a custom recipe group for the recipe based on the provided block.
         *
         * @param block The [Block] for which the custom recipe group is set.
         * @param name The name of the recipe group.
         * @return The [ShapedRecipeJsonBuilder] with the custom recipe group.
         */
        fun ShapedRecipeJsonBuilder.customGroup(block: Block, name: String): ShapedRecipeJsonBuilder {
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