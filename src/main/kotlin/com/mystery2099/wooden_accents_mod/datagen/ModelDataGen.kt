package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asPlanks
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.BlockStateGeneratorDataBlock
import com.mystery2099.wooden_accents_mod.data.ModModels
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.WoodType
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.util.math.Direction

class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {

    val block = "block/"

    //Collections
    private val horizontalDirections = arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.run {
            WoodType.stream().forEach{
                ModModels.coffeeTableLegShort.upload("${it.name.lowercase()}_coffee_table_leg_short".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.coffeeTableLegTall.upload("${it.name.lowercase()}_coffee_table_leg_tall".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableCenterLeg.upload("${it.name.lowercase()}_table_single_leg".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableCornerLeg.upload("${it.name.lowercase()}_table_corner_leg".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableEndLeg.upload("${it.name.lowercase()}_table_end_leg".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)
            }
            ModBlocks.blocks.forEach {
                when {
                    it is BlockStateGeneratorDataBlock ->
                        it.generateBlockStateModels(generator = blockStateModelGenerator)
                }
            }
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }
}

