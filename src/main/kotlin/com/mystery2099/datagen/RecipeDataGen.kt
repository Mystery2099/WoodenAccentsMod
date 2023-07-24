package com.mystery2099.datagen

import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.*
import com.mystery2099.data.ModItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.block.Block
import net.minecraft.block.LadderBlock
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.ItemTags
import java.util.function.Consumer

class RecipeDataGen(output: FabricDataOutput) : FabricRecipeProvider(output) {
    private lateinit var exporter: Consumer<RecipeJsonProvider>

    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        this.exporter = exporter
        ModBlocks.blocks.forEach{
            when(it){
                is ThinPillarBlock -> genThinPillarRecipe(it)
                is ThickPillarBlock -> genThickPillarRecipe(it)
                is PlankLadderBlock -> genPlankLadderRecipe(it)
                is CustomWallBlock -> offerWallRecipe(exporter, RecipeCategory.DECORATIONS, it, it.baseBlock)
                is TableBlock -> genTableRecipe(it)
                is CoffeeTableBlock -> genCoffeeTableRecipe(it)
                is ThinBookshelfBlock -> genThinBookshelfRecipe(it)
                is FloorCoveringBlock -> genFloorCoveringRecipe(it)
                is KitchenCounterBlock -> genKitchenCounterRecipe(it)
                is KitchenCabinetBlock -> genKitchenCabinetRecipe(it)
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
            .criterion(primaryInput)
            .offerTo(exporter)
    }
    private fun genThinPillarRecipe(block: ThinPillarBlock) {
        genPillarRecipe(block, 5, block.baseBlock, Items.STICK)
    }
    private fun genThickPillarRecipe(block: ThickPillarBlock) {
        genPillarRecipe(block, 6, block.baseBlock, block.baseBlock)
    }
    /*---------------End Pillars----------------*/

    /*---------------Ladders----------------*/
    private fun genLadderRecipe(output: LadderBlock, input: ItemConvertible, outputNum: Int, group: String) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, output, outputNum)
            .input('#', input)
            .pattern("# #")
            .pattern("###")
            .pattern("# #")
            .group(group)
            .criterion(input)
            .offerTo(exporter)
    }
    private fun genPlankLadderRecipe(output: PlankLadderBlock) {
        genLadderRecipe(output, output.baseBlock, 8, "plank_ladders")
    }

    /*---------------End Ladders----------------*/

    /*---------------Tables----------------*/
    private fun genTableRecipe(block: TableBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 4)
            .input('#', block.topBlock)
            .input('|', block.baseBlock)
            .pattern("###")
            .pattern(" | ")
            .pattern(" | ")
            .group(block, "tables")
            .criterion(block.topBlock)
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
            .group(block, "coffee_tables")
            .criterion(block.topBlock)
            .offerTo(exporter)
    }
    /*---------------End Coffee Tables----------------*/

    /*---------------Thin Bookshelves----------------*/
    private fun genThinBookshelfRecipe(block: ThinBookshelfBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 2)
            .input('#', block.baseBlock)
            .input('_', Ingredient.fromTag(ItemTags.WOODEN_SLABS))
            .pattern("##")
            .pattern("__")
            .pattern("##")
            .group("thin_bookshelves")
            .criterion(block.baseBlock)
            .offerTo(exporter)
    }
    /*---------------End Thin Bookshelves----------------*/

    /*---------------Floor Covering Blocks----------------*/
    private fun genFloorCoveringRecipe(block: FloorCoveringBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 3).run {
            input('#', block.baseBlock)
            input('_', Items.PAPER)
            pattern("##")
            pattern("_ ")
            group(block, "carpets")
            criterion(block.baseBlock)
            offerTo(exporter)
        }
    }
    /*---------------End Floor Covering Blocks----------------*/

    /*---------------Kitchen Counters----------------*/
    private fun genKitchenCounterRecipe(block: KitchenCounterBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 4)
            .input('#', block.baseBlock)
            .input('_', block.topBlock)
            .pattern("___")
            .pattern("###")
            .pattern("###")
            .group(block, "kitchen_counters")
            .criterion(block.baseBlock)
            .offerTo(exporter)
    }
    private fun genKitchenCabinetRecipe(block: KitchenCabinetBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 4)
            .input('#', block.baseBlock)
            .input('_', block.topBlock)
            .input('O', ModItemTags.chests)
            .pattern("___")
            .pattern("#O#")
            .pattern("###")
            .group(block, "kitchen_cabinets")
            .criterion(block.baseBlock)
            .offerTo(exporter)
    }
    /*---------------End Kitchen Counters----------------*/


    private fun Block.isStripped(): Boolean {
        return this.translationKey.contains("stripped")
    }
    private fun Block.isPlank(): Boolean {
        return this.translationKey.contains("plank")
    }
    private fun ShapedRecipeJsonBuilder.group(block: Block, name : String): ShapedRecipeJsonBuilder {
        return this.group(if (block.isStripped()) "stripped_$name" else if (block.isPlank()) "plank_$name" else name)
    }

    private fun ShapedRecipeJsonBuilder.criterion(requiredItem: ItemConvertible): ShapedRecipeJsonBuilder {
        return this.criterion(hasItem(requiredItem), conditionsFromItem(requiredItem))
    }

}