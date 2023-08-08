package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.getItemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.Items
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class ThinPillarBlock(baseBlock: Block) : AbstractPillarBlock(baseBlock, shape) {
    override val tag: TagKey<Block>
        get() = ModBlockTags.thinPillars
    override infix fun WorldAccess.checkUp(pos: BlockPos): Boolean = this.getUpState(pos).run {
        isIn(ModBlockTags.thinPillars) || isIn(BlockTags.FENCES)
    }

    override infix fun WorldAccess.checkDown(pos: BlockPos): Boolean = this.getDownState(pos).run {
        isIn(ModBlockTags.thinPillars) || isIn(BlockTags.FENCES)
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        this.offerRecipe(exporter, 5, this.baseBlock, Items.STICK)
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val map = TextureMap.all(this.baseBlock)
        generator.blockStateCollector.accept(this.genBlockStateModelSupplier(
            centerModel = Identifier("${this.woodType.name.lowercase()}_fence_post").asBlockModelId(),
            bottomModel = ModModels.thinPillarBottom.upload(this, map, generator.modelCollector)
        ))
        ModModels.thinPillarInventory.upload(this.getItemModelId(), map, generator.modelCollector)
    }

    companion object {
        @JvmStatic
        val shape = Shape(
            createCuboidShape(4.0, 13.0, 4.0, 12.0, 16.0, 12.0),
            createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0),
            createCuboidShape(4.0, 0.0, 4.0, 12.0, 3.0, 12.0)
        )
    }
}