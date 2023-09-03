package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.enums.DeskShape
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.util.function.Consumer

class DeskBlock(settings: FabricBlockSettings, baseBlock: Block, topBlock: Block) :
    AbstractDeskBlock(settings, baseBlock, topBlock) {
    override val itemGroup: CustomItemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.desks
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val block = this
        TextureMap().apply {
            put(TextureKey.TOP, block.topBlock.textureId)
            put(TextureKey.SIDE, block.baseBlock.textureId)
        }.let { map ->
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                    .coordinate(
                        variantMap(
                            blockModel = ModModels.desk.upload(block, map, generator.modelCollector),
                            innerLeftModel = ModModels.deskLeftCorner.upload(block, map, generator.modelCollector)
                        )
                    )
            )
        }
    }

    private fun variantMap(
        blockModel: Identifier,
        innerLeftModel: Identifier
    ) = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, ModProperties.deskShape).apply {
        val northBlock = blockModel.asBlockStateVariant()
        val northInnerLeft = innerLeftModel.asBlockStateVariant()

        mapOf(
            Direction.NORTH to mapOf(
                DeskShape.STRAIGHT to northBlock,
                DeskShape.LEFT to northInnerLeft,
                DeskShape.RIGHT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R90),
            ),
            Direction.EAST to mapOf(
                DeskShape.STRAIGHT to northBlock.withYRotationOf(VariantSettings.Rotation.R90),
                DeskShape.LEFT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R90),
                DeskShape.RIGHT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R180),
            ),
            Direction.SOUTH to mapOf(
                DeskShape.STRAIGHT to northBlock.withYRotationOf(VariantSettings.Rotation.R180),
                DeskShape.LEFT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R180),
                DeskShape.RIGHT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R270),
            ),
            Direction.WEST to mapOf(
                DeskShape.STRAIGHT to northBlock.withYRotationOf(VariantSettings.Rotation.R270),
                DeskShape.LEFT to northInnerLeft.withYRotationOf(VariantSettings.Rotation.R270),
                DeskShape.RIGHT to northInnerLeft,
            )
        ).forEach { i -> i.value.forEach { j -> register(i.key, j.key, j.value) } }
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {

    }


}