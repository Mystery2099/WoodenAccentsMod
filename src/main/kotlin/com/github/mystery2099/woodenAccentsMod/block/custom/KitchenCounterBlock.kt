package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.block.textureId
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.withYRotationOf
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
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


/**
 * KitchenCounterBlock represents a block in a kitchen counter.
 *
 * @property baseBlock The base block of the kitchen counter.
 * @property topBlock The top block of the kitchen counter.
 */
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