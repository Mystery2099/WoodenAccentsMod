package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.planks
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.withBlockModelPath
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.textureId
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.WoodType
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.TextureMap

class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.run {
            // Block providers reference these shared leg models instead of uploading duplicates.
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

            ModBlocks.blocks.filterIsInstance<CustomBlockStateProvider>().forEach {
                it.generateBlockStateModels(generator = blockStateModelGenerator)
            }
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }
}
