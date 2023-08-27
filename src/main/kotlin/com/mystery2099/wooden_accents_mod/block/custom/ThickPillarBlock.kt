package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks.itemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import java.util.function.Consumer

class ThickPillarBlock(baseBlock: Block) : AbstractPillarBlock(baseBlock, shape) {
    override val connectableBlockTag: TagKey<Block> = ModBlockTags.thickPillarsConnectable
    override val tag: TagKey<Block> = ModBlockTags.thickPillars

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        this.offerRecipe(exporter = exporter, outputNum = 6, primaryInput = baseBlock, secondaryInput = baseBlock)
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val map = TextureMap.all(this.baseBlock)
        generator.blockStateCollector.accept(this.genBlockStateModelSupplier(
            centerModel = "${this.woodType.name.lowercase()}_plank_wall_post".toIdentifier().asBlockModelId(),
            bottomModel = ModModels.thickPillarBottom.upload(this, map, generator.modelCollector)
        ))
        ModModels.thickPillarInventory.upload(this.itemModelId, map, generator.modelCollector)
    }

    companion object {
        val shape = Shape(
            topShape = VoxelShapeHelper.createCuboidShape(1, 10, 1, 15, 16, 15),
            centerShape = VoxelShapeHelper.createCuboidShape(4, 0, 4, 12, 16, 12),
            baseShape = VoxelShapeHelper.createCuboidShape(1, 0, 1, 15, 6, 15)
        )
    }
}