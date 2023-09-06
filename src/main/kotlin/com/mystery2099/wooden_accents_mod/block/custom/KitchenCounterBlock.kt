package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.customGroup
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import net.minecraft.block.Block
import net.minecraft.block.enums.StairShape
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.util.function.Consumer

class KitchenCounterBlock(baseBlock: Block, topBlock: Block) : AbstractKitchenCounterBlock(baseBlock, topBlock),
    CustomItemGroupProvider, CustomTagProvider<Block>, CustomRecipeProvider, CustomBlockStateProvider {
    override val tag: TagKey<Block> = ModBlockTags.kitchenCounters
    override val itemGroup: CustomItemGroup = ModItemGroups.decorations

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val block = this
        TextureMap().apply {
            put(TextureKey.TOP, block.topBlock.textureId)
            put(TextureKey.SIDE, block.baseBlock.textureId)
        }.let { map ->
            val normalModel = ModModels.kitchenCounter.upload(block, map, generator.modelCollector)

            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                    .coordinate(
                        variantMap(
                            blockModel = normalModel,
                            innerLeftModel = ModModels.kitchenCounterInnerLeftCorner.upload(
                                block,
                                map,
                                generator.modelCollector
                            ),
                            outerLeftModel = ModModels.kitchenCounterOuterLeftCorner.upload(
                                block,
                                map,
                                generator.modelCollector
                            )
                        )
                    )
            )
            generator.registerParentedItemModel(block, normalModel)
        }
    }

    private fun variantMap(
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
                StairShape.INNER_RIGHT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R90),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(VariantSettings.Rotation.R90),
            ),
            Direction.EAST to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(VariantSettings.Rotation.R90),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R90),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(VariantSettings.Rotation.R90),
                StairShape.INNER_RIGHT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R180),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(VariantSettings.Rotation.R180),
            ),
            Direction.SOUTH to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(VariantSettings.Rotation.R180),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R180),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(VariantSettings.Rotation.R180),
                StairShape.INNER_RIGHT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R270),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(VariantSettings.Rotation.R270),
            ),
            Direction.WEST to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(VariantSettings.Rotation.R270),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R270),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(VariantSettings.Rotation.R270),
                StairShape.INNER_RIGHT to northInnerLeft,
                StairShape.OUTER_RIGHT to northOuterLeft,
            )
        ).forEach { i -> i.value.forEach { j -> register(i.key, j.key, j.value) } }
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4).apply {
            input('#', baseBlock)
            input('_', topBlock)
            pattern("___")
            pattern("###")
            pattern("###")
            customGroup(this@KitchenCounterBlock, "kitchen_counters")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

}