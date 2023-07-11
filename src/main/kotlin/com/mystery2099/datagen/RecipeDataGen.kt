package com.mystery2099.datagen

import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.AbstractPillarBlock
import com.mystery2099.block.custom.ThickPillarBlock
import com.mystery2099.block.custom.ThinPillarBlock
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
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
}