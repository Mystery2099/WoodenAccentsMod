package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.block.custom.KitchenCounterBlock
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.isPlank
import com.mystery2099.wooden_accents_mod.block.ModBlocks.isStripped
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.book.RecipeCategory
import java.util.function.Consumer

class RecipeDataGen(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        ModBlocks.blocks.forEach{
            when(it){
                is RecipeBlockData -> it.offerRecipeTo(exporter)
                is KitchenCounterBlock -> it.offerRecipeTo(exporter)
            }
        }
    }


    /*---------------Kitchen Counters----------------*/
    private infix fun KitchenCounterBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', baseBlock)
            input('_', topBlock)
            pattern("___")
            pattern("###")
            pattern("###")
            group(this@offerRecipeTo, "kitchen_counters")
            requires(baseBlock)
            offerTo(exporter)
        }
    }
    /*---------------End Kitchen Counters----------------*/




    companion object {
        infix fun ShapedRecipeJsonBuilder.requires(requiredItem: ItemConvertible): ShapedRecipeJsonBuilder {
            return criterion(hasItem(requiredItem), conditionsFromItem(requiredItem))
        }

        fun ShapedRecipeJsonBuilder.group(block: Block, name : String): ShapedRecipeJsonBuilder {
            return group(when {
                block.isStripped -> "stripped_$name"
                block.isPlank ->"plank_$name"
                else -> name
            })
        }
    }

}