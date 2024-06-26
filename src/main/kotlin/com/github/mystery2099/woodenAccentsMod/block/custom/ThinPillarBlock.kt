package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.withBlockModelPath
import com.github.mystery2099.woodenAccentsMod.block.itemModelId
import com.github.mystery2099.woodenAccentsMod.block.woodType
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.Items
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.function.Consumer


/**
 * ThinPillarBlock represents a pillar block that is derived from a base block.
 *
 * @param baseBlock The base block on which this pillar is based.
 */
class ThinPillarBlock(baseBlock: Block) : AbstractPillarBlock(baseBlock, shape) {
    override val connectableBlockTag: TagKey<Block> = ModBlockTags.thinPillarsConnectable
    override val tag: TagKey<Block> = ModBlockTags.thinPillars

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        this.offerRecipe(
            exporter = exporter,
            outputNum = 5,
            primaryInput = this.baseBlock,
            secondaryInput = Items.STICK
        )
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val map = TextureMap.all(this.baseBlock)
        generator.blockStateCollector.accept(
            this.genBlockStateModelSupplier(
                centerModel = Identifier("${this.woodType.name.lowercase()}_fence_post").withBlockModelPath(),
                bottomModel = ModModels.thinPillarBottom.upload(this, map, generator.modelCollector)
            )
        )
        ModModels.thinPillarInventory.upload(this.itemModelId, map, generator.modelCollector)
    }

    companion object {
        val shape = Shape(
            topShape = VoxelAssembly.createCuboidShape(4, 13, 4, 12, 16, 12),
            centerShape = VoxelAssembly.createCuboidShape(6, 0, 6, 10, 16, 10),
            baseShape = VoxelAssembly.createCuboidShape(4, 0, 4, 12, 3, 12)
        )
    }
}