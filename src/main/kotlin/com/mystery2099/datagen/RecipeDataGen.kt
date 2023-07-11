package com.mystery2099.datagen

import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.*
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import java.util.function.Consumer

class RecipeDataGen(output: FabricDataOutput) : FabricRecipeProvider(output) {
    private lateinit var exporter: Consumer<RecipeJsonProvider>

    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        this.exporter = exporter
        ModBlocks.blocks.forEach{
            when(it){
                is ThinPillarBlock -> genThinPillarRecipe(it)
                is ThickPillarBlock -> genThickPillarRecipe(it)
                is TableBlock -> genTableRecipe(it)
                is CoffeeTableBlock -> genCoffeeTableRecipe(it)
            }
        }
    }

    /*---------------Pillars----------------*/
    private fun genPillarRecipe(block: AbstractPillarBlock, outputNum: Int, primaryInput: ItemConvertible, secondaryInput: ItemConvertible) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, outputNum)
            .input('|', secondaryInput)
            .input('#', primaryInput)
            .pattern("###")
            .pattern(" | ")
            .pattern("###")
            .group(if (block is ThickPillarBlock) "thick_pillars" else if (block is ThinPillarBlock) "thin_pillars" else "pillars")
            .criterion(hasItem(primaryInput), conditionsFromItem(primaryInput))
            .offerTo(exporter)
    }
    private fun genThinPillarRecipe(block: ThinPillarBlock) {
        genPillarRecipe(block, 8, block.baseBlock, Items.STICK)
    }
    private fun genThickPillarRecipe(block: ThickPillarBlock) {
        genPillarRecipe(block, 6, block.baseBlock, block.baseBlock)
    }
    /*---------------End Pillars----------------*/

    /*---------------Tables----------------*/
    private fun genTableRecipe(block: TableBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 4)
            .input('#', block.topBlock)
            .input('|', block.baseBlock)
            .pattern("###")
            .pattern(" | ")
            .pattern(" | ")
            .group(if (block.isStripped()) "stripped_tables" else "tables")
            .criterion(hasItem(block.topBlock), conditionsFromItem(block.topBlock))
            .offerTo(exporter)
    }
    /*---------------End Tables----------------*/

    /*---------------Coffee Tables----------------*/

    private fun genCoffeeTableRecipe(block: CoffeeTableBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 6)
            .input('_', block.topBlock)
            .input('|', block.baseBlock)
            .pattern("___")
            .pattern("| |")
            .group(if (block.isStripped()) "stripped_coffee_tables" else "coffee_tables")
            .criterion(hasItem(block.topBlock), conditionsFromItem(block.topBlock))
            .offerTo(exporter)
    }

    /*---------------End Coffee Tables----------------*/


    private fun Block.isStripped(): Boolean {
        return this.translationKey.contains("stripped")
    }
}