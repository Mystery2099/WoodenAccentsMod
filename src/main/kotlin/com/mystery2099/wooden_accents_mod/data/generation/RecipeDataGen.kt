package com.mystery2099.wooden_accents_mod.data.generation

import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.isPlank
import com.mystery2099.wooden_accents_mod.block.ModBlocks.isStripped
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomRecipeProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import java.util.function.Consumer

class RecipeDataGen(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        ModBlocks.blocks.forEach {
            when {
                it is CustomRecipeProvider -> it.offerRecipeTo(exporter)
            }
        }
    }

    companion object {
        infix fun ShapedRecipeJsonBuilder.requires(requiredItem: ItemConvertible): ShapedRecipeJsonBuilder {
            return criterion(hasItem(requiredItem), conditionsFromItem(requiredItem))
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