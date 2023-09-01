package com.mystery2099.wooden_accents_mod.datagen

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asPlanks
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.itemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.CoffeeTableBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.data.ModModels
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.WoodType
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.util.math.Direction
import java.util.function.Supplier

class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {

    val block = "block/"

    //Collections
    private val horizontalDirections = arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.run {
            WoodType.stream().forEach{
                ModModels.coffeeTableLegShort.upload("${it.name.lowercase()}_coffee_table_leg_short".toIdentifier().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.coffeeTableLegTall.upload("${it.name.lowercase()}_coffee_table_leg_tall".toIdentifier().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableCenterLeg.upload("${it.name.lowercase()}_table_single_leg".toIdentifier().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableCornerLeg.upload("${it.name.lowercase()}_table_corner_leg".toIdentifier().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableEndLeg.upload("${it.name.lowercase()}_table_end_leg".toIdentifier().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)
            }
            ModBlocks.blocks.forEach {
                when {
                    it is CustomBlockStateProvider ->
                        it.generateBlockStateModels(generator = blockStateModelGenerator)
                }
            }
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }

    companion object {
        fun createCoffeeTableItemModel(coffeeTableBlock: CoffeeTableBlock, generator: BlockStateModelGenerator) {
            val tallModel = ModModels.coffeeTableTallInventory.upload(coffeeTableBlock.itemModelId.withSuffixedPath("_tall"), TextureMap().apply {
                put(TextureKey.TOP, coffeeTableBlock.topBlock.textureId)
                put(ModModels.legs, coffeeTableBlock.baseBlock.textureId)
            }, generator.modelCollector)
            val jsonObject = ModModels.coffeeTableInventory.createJson(coffeeTableBlock.itemModelId, mapOf(
                TextureKey.TOP to coffeeTableBlock.topBlock.textureId,
                ModModels.legs to coffeeTableBlock.baseBlock.textureId
            ))
            val jsonArray = JsonArray()
            val jsonObject2 = JsonObject()
            val jsonObject3 = JsonObject()
            jsonObject3.addProperty("height", 1.0f)
            jsonObject2.add("predicate", jsonObject3)
            jsonObject2.addProperty("model", tallModel.toString())
            jsonArray.add(jsonObject2)
            jsonObject.add("overrides", jsonArray)
            generator.modelCollector.accept(coffeeTableBlock.itemModelId, Supplier {
                jsonObject
            })
        }
    }
}

