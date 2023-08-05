package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.block.custom.KitchenCounterBlock
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.id
import com.mystery2099.wooden_accents_mod.block.custom.*
import com.mystery2099.wooden_accents_mod.data.ModItemTags
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
    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        ModBlocks.blocks.forEach{
            when(it){
                is ThinPillarBlock -> it.offerRecipeTo(exporter)
                is ThickPillarBlock -> it.offerRecipeTo(exporter)
                is PlankLadderBlock -> it.offerRecipeTo(exporter)
                is CustomWallBlock -> offerWallRecipe(exporter, RecipeCategory.DECORATIONS, it, it.baseBlock)
                is TableBlock -> it.offerRecipeTo(exporter)
                is CoffeeTableBlock -> it.offerRecipeTo(exporter)
                is ThinBookshelfBlock -> it.offerRecipeTo(exporter)
                is FloorCoveringBlock -> it.offerRecipeTo(exporter)
                is KitchenCounterBlock -> it.offerRecipeTo(exporter)
                is KitchenCabinetBlock -> it.offerRecipeTo(exporter)
            }
        }
    }

    /*---------------Pillars----------------*/
    private fun AbstractPillarBlock.offerRecipe(exporter: Consumer<RecipeJsonProvider>, outputNum: Int, primaryInput: ItemConvertible, secondaryInput: ItemConvertible) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, outputNum).apply {
            input('|', secondaryInput)
            input('#', primaryInput)
            pattern("###")
            pattern(" | ")
            pattern("###")
            group(when (this@offerRecipe) {
                is ThickPillarBlock -> "thick_pillars"
                is ThinPillarBlock -> "thin_pillars"
                else -> "pillars"
            })
            requires(primaryInput)
            offerTo(exporter)
        }

    }
    private infix fun ThinPillarBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        offerRecipe(exporter, 5, this.baseBlock, Items.STICK)
    }
    private infix fun ThickPillarBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        offerRecipe(exporter, 6, baseBlock, baseBlock)
    }
    /*---------------End Pillars----------------*/

    /*---------------Ladders----------------*/
    private fun LadderBlock.offerRecipe(exporter: Consumer<RecipeJsonProvider>, input: ItemConvertible, outputNum: Int, group: String) {
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
    private infix fun PlankLadderBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        offerRecipe(exporter, baseBlock, 8, "plank_ladders")
    }

    /*---------------End Ladders----------------*/

    /*---------------Tables----------------*/
    private infix fun TableBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', topBlock)
            input('|', baseBlock)
            pattern("###")
            pattern(" | ")
            pattern(" | ")
            group(this@offerRecipeTo, "tables")
            requires(topBlock)
            offerTo(exporter)
        }

    }
    /*---------------End Tables----------------*/

    /*---------------Coffee Tables----------------*/
    private infix fun CoffeeTableBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 6).apply {
            input('_', topBlock)
            input('|', baseBlock)
            pattern("___")
            pattern("| |")
            group(this@offerRecipeTo, "coffee_tables")
            requires(topBlock)
            offerTo(exporter)
        }

    }
    /*---------------End Coffee Tables----------------*/

    /*---------------Thin Bookshelves----------------*/
    private infix fun ThinBookshelfBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 2).apply {
            input('#', baseBlock)
            input('_', Ingredient.fromTag(ItemTags.WOODEN_SLABS))
            pattern("##")
            pattern("__")
            pattern("##")
            group("thin_bookshelves")
            requires(baseBlock)
            offerTo(exporter)
        }
    }
    /*---------------End Thin Bookshelves----------------*/

    /*---------------Floor Covering Blocks----------------*/
    private infix fun FloorCoveringBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 3).apply {
            input('#', baseBlock)
            input('_', Items.PAPER)
            pattern("##")
            pattern("_ ")
            group(this@offerRecipeTo, "carpets")
            requires(baseBlock)
            offerTo(exporter)
        }
    }
    /*---------------End Floor Covering Blocks----------------*/

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
    private infix fun KitchenCabinetBlock.offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', baseBlock)
            input('_', topBlock)
            input('O', ModItemTags.chests)
            pattern("___")
            pattern("#O#")
            pattern("###")
            group(this@offerRecipeTo, "kitchen_cabinets")
            requires(baseBlock)
            offerTo(exporter)
        }
    }
    /*---------------End Kitchen Counters----------------*/


    private fun Block.isStripped() = id.path.contains("stripped")
    private fun Block.isPlank() = id.path.contains("plank")
    private fun ShapedRecipeJsonBuilder.group(block: Block, name : String): ShapedRecipeJsonBuilder {
        return group(when {
            block.isStripped() -> "stripped_$name"
            block.isPlank() ->"plank_$name"
            else -> name
        })
    }

    private infix fun ShapedRecipeJsonBuilder.requires(requiredItem: ItemConvertible): ShapedRecipeJsonBuilder {
        return criterion(hasItem(requiredItem), conditionsFromItem(requiredItem))
    }

}