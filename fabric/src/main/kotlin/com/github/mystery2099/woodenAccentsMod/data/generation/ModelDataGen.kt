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
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.TextureMapping

class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {

    override fun generateBlockStateModels(blockStateModelGenerator: BlockModelGenerators) {
        blockStateModelGenerator.run {
            // Block providers reference these shared leg models instead of uploading duplicates.
            WoodType.values().forEach {
                ModModels.coffeeTableLegShort.create(
                    "${it.name.lowercase()}_coffee_table_leg_short".toIdentifier().withBlockModelPath(), TextureMapping.singleSlot(
                        ModModels.legs, it.planks.textureId
                    ), modelOutput
                )

                ModModels.coffeeTableLegTall.create(
                    "${it.name.lowercase()}_coffee_table_leg_tall".toIdentifier().withBlockModelPath(), TextureMapping.singleSlot(
                        ModModels.legs, it.planks.textureId
                    ), modelOutput
                )

                ModModels.tableCenterLeg.create(
                    "${it.name.lowercase()}_table_single_leg".toIdentifier().withBlockModelPath(), TextureMapping.singleSlot(
                        ModModels.legs, it.planks.textureId
                    ), modelOutput
                )

                ModModels.tableCornerLeg.create(
                    "${it.name.lowercase()}_table_corner_leg".toIdentifier().withBlockModelPath(), TextureMapping.singleSlot(
                        ModModels.legs, it.planks.textureId
                    ), modelOutput
                )

                ModModels.tableEndLeg.create(
                    "${it.name.lowercase()}_table_end_leg".toIdentifier().withBlockModelPath(), TextureMapping.singleSlot(
                        ModModels.legs, it.planks.textureId
                    ), modelOutput
                )
            }

            ModBlocks.blocks.filterIsInstance<CustomBlockStateProvider>().forEach {
                it.generateBlockStateModels(generator = blockStateModelGenerator)
            }
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerators) {

    }
}
