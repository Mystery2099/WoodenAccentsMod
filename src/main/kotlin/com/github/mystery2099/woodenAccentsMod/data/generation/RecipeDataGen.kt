package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.isPlank
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.isStripped
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
 * Recipe data gen
 *
 * @constructor
 *
 * @param output
 */
class RecipeDataGen(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        //TODO Change to use filterIsInstance
        ModBlocks.blocks.filterIsInstance<CustomRecipeProvider>().forEach {
            it.offerRecipeTo(exporter)
        }
    }

    companion object {
        fun ShapedRecipeJsonBuilder.requires(requiredItem: ItemConvertible): ShapedRecipeJsonBuilder {
            return criterion(hasItem(requiredItem), conditionsFromItem(requiredItem))
        }
        fun ShapedRecipeJsonBuilder.requires(requiredTag: TagKey<Item>): ShapedRecipeJsonBuilder {
            return criterion("has_${requiredTag.id}", conditionsFromTag(requiredTag))
        }

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