package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.block.custom.KitchenCounterBlock
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asPlanks
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.BlockStateGeneratorDataBlock
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.WoodType
import net.minecraft.block.enums.StairShape
import net.minecraft.data.client.*
import net.minecraft.data.client.VariantSettings.Rotation
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
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
            ModBlocks.blocks.forEach { this.genBlockStateModel(it) }
        }
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }

    private fun BlockStateModelGenerator.genBlockStateModel(block: Block) {
        when (block) {
            is BlockStateGeneratorDataBlock -> block.generateBlockStateModels(this)
            is KitchenCounterBlock -> block.generateBlockStateModels(this)
        }
    }

    /*------------ Kitchen Counters -----------*/
    private fun KitchenCounterBlock.generateBlockStateModels(generator: BlockStateModelGenerator) {
        val block = this
        TextureMap().apply {
            put(TextureKey.TOP, block.topBlock.textureId)
            put(TextureKey.SIDE, block.baseBlock.textureId)
        }.let {map ->
            val normalModel = ModModels.kitchenCounter.upload(block, map, generator.modelCollector)

            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                .coordinate(
                    kitchenCounterVariantMap(
                        blockModel = normalModel,
                        innerLeftModel = ModModels.kitchenCounterInnerLeftCorner.upload(block, map, generator.modelCollector),
                        outerLeftModel = ModModels.kitchenCounterOuterLeftCorner.upload(block, map, generator.modelCollector)
                    )
                )
            )
            generator.registerParentedItemModel(block, normalModel)
        }
    }

    private fun kitchenCounterVariantMap(
        blockModel: Identifier,
        innerLeftModel: Identifier,
        outerLeftModel: Identifier
    ) = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.STAIR_SHAPE).apply {
        val northBlock = blockModel.asBlockStateVariant()
        val northInnerLeft = innerLeftModel.asBlockStateVariant()
        val northOuterLeft = outerLeftModel.asBlockStateVariant()

        mapOf(
            Direction.NORTH to mapOf(
                StairShape.STRAIGHT to northBlock,
                StairShape.INNER_LEFT to northInnerLeft,
                StairShape.OUTER_LEFT to northOuterLeft,
                StairShape.INNER_RIGHT to  northInnerLeft.withYRotationOf(Rotation.R90),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(Rotation.R90),
            ),
            Direction.EAST to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(Rotation.R90),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(Rotation.R90),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(Rotation.R90),
                StairShape.INNER_RIGHT to  northInnerLeft.withYRotationOf(Rotation.R180),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(Rotation.R180),
            ),
            Direction.SOUTH to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(Rotation.R180),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(Rotation.R180),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(Rotation.R180),
                StairShape.INNER_RIGHT to  northInnerLeft.withYRotationOf(Rotation.R270),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(Rotation.R270),
            ),
            Direction.WEST to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(Rotation.R270),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(Rotation.R270),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(Rotation.R270),
                StairShape.INNER_RIGHT to  northInnerLeft,
                StairShape.OUTER_RIGHT to northOuterLeft,
            )
        ).forEach { i -> i.value.forEach { j -> register(i.key, j.key, j.value) } }
    }

    /*------------ End Kitchen Counters -----------*/
}

