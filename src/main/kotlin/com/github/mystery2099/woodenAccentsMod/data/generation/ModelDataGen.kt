package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.asBlockModelId
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.asPlanks
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.textureId
import com.github.mystery2099.woodenAccentsMod.data.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.WoodType
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.util.math.Direction

/**
 * Model data gen
 *
 * @constructor
 *
 * @param output
 */
class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {

    val block = "block/"

    //Collections
    private val horizontalDirections = arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.run {
            WoodType.stream().forEach {
                ModModels.coffeeTableLegShort.upload(
                    "${it.name.lowercase()}_coffee_table_leg_short".toIdentifier().asBlockModelId(), TextureMap().put(
                        ModModels.legs, it.asPlanks().textureId
                    ), modelCollector
                )

                ModModels.coffeeTableLegTall.upload(
                    "${it.name.lowercase()}_coffee_table_leg_tall".toIdentifier().asBlockModelId(), TextureMap().put(
                        ModModels.legs, it.asPlanks().textureId
                    ), modelCollector
                )

                ModModels.tableCenterLeg.upload(
                    "${it.name.lowercase()}_table_single_leg".toIdentifier().asBlockModelId(), TextureMap().put(
                        ModModels.legs, it.asPlanks().textureId
                    ), modelCollector
                )

                ModModels.tableCornerLeg.upload(
                    "${it.name.lowercase()}_table_corner_leg".toIdentifier().asBlockModelId(), TextureMap().put(
                        ModModels.legs, it.asPlanks().textureId
                    ), modelCollector
                )

                ModModels.tableEndLeg.upload(
                    "${it.name.lowercase()}_table_end_leg".toIdentifier().asBlockModelId(), TextureMap().put(
                        ModModels.legs, it.asPlanks().textureId
                    ), modelCollector
                )
            }
            com.github.mystery2099.woodenAccentsMod.block.ModBlocks.blocks.filterIsInstance<CustomBlockStateProvider>().forEach {
                it.generateBlockStateModels(generator = blockStateModelGenerator)
            }
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }
}

