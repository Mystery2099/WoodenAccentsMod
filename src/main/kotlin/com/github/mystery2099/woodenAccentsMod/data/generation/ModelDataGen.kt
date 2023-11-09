package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.planks
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.withBlockModelPath
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.textureId
import com.github.mystery2099.woodenAccentsMod.data.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.WoodType
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.TextureMap

/**
 * [ModelDataGen] is responsible for generating custom model data for the Wooden Accents Mod.
 * It extends [FabricModelProvider], which is used for generating model data.
 *
 * @param output The data output to which the generated models will be written.
 */
class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {


    /**
     * Generate block state models for the mod.
     *
     * @param blockStateModelGenerator The block state model generator to create block state models.
     */
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.run {
            // Iterate through wood types and generate models for coffee tables and table legs.
            WoodType.stream().forEach {
                ModModels.coffeeTableLegShort.upload(
                    "${it.name.lowercase()}_coffee_table_leg_short".toIdentifier().withBlockModelPath(), TextureMap.of(
                        ModModels.legs, it.planks.textureId
                    ), modelCollector
                )

                ModModels.coffeeTableLegTall.upload(
                    "${it.name.lowercase()}_coffee_table_leg_tall".toIdentifier().withBlockModelPath(), TextureMap.of(
                        ModModels.legs, it.planks.textureId
                    ), modelCollector
                )

                ModModels.tableCenterLeg.upload(
                    "${it.name.lowercase()}_table_single_leg".toIdentifier().withBlockModelPath(), TextureMap.of(
                        ModModels.legs, it.planks.textureId
                    ), modelCollector
                )

                ModModels.tableCornerLeg.upload(
                    "${it.name.lowercase()}_table_corner_leg".toIdentifier().withBlockModelPath(), TextureMap.of(
                        ModModels.legs, it.planks.textureId
                    ), modelCollector
                )

                ModModels.tableEndLeg.upload(
                    "${it.name.lowercase()}_table_end_leg".toIdentifier().withBlockModelPath(), TextureMap.of(
                        ModModels.legs, it.planks.textureId
                    ), modelCollector
                )
            }

            // Iterate through custom block state providers in ModBlocks and generate block state models.
            ModBlocks.blocks.filterIsInstance<CustomBlockStateProvider>().forEach {
                it.generateBlockStateModels(generator = blockStateModelGenerator)
            }
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }
}

