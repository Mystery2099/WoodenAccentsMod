package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.getItemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

class ThickPillarBlock(baseBlock: Block) : AbstractPillarBlock(baseBlock, shape) {
    override val tag: TagKey<Block>
        get() = ModBlockTags.thickPillars

    override infix fun WorldAccess.checkUp(pos: BlockPos): Boolean = getUpState(pos).run {
        isIn(ModBlockTags.pillars) || isIn(BlockTags.WALLS)
    }

    override infix fun WorldAccess.checkDown(pos: BlockPos): Boolean  = getDownState(pos).run {
        isIn(ModBlockTags.pillars) || isIn(BlockTags.WALLS)
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        this.offerRecipe(exporter, 6, baseBlock, baseBlock)
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val map = TextureMap.all(this.baseBlock)
        generator.blockStateCollector.accept(this.genBlockStateModelSupplier(
            centerModel = "${this.woodType.name.lowercase()}_wall_post".asWamId().asBlockModelId(),
            bottomModel = ModModels.thickPillarBottom.upload(this, map, generator.modelCollector)
        ))
        ModModels.thickPillarInventory.upload(this.getItemModelId(), map, generator.modelCollector)
    }

    companion object {
        @JvmStatic
        val shape = Shape(
            createCuboidShape(1.0, 10.0, 1.0, 15.0, 16.0, 15.0),
            createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0),
            createCuboidShape(1.0, 0.0, 1.0, 15.0, 6.0, 15.0)
        )
    }
}