package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.getItemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.BlockStateGeneratorDataBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModBlockTags.isIn
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.CompositeVoxelShape
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import java.util.function.Consumer

class ModernFenceBlock(settings: Block, val sideBlock: Block, val postBlock: Block) : FenceBlock(FabricBlockSettings.copyOf(settings)),
    GroupedBlock, RecipeBlockData, TaggedBlock, BlockStateGeneratorDataBlock {
    override val tag: TagKey<Block> = ModBlockTags.modernFences
    override val itemGroup = ModItemGroups.structuralElements

    override fun canConnect(state: BlockState, neighborIsFullSquare: Boolean, dir: Direction): Boolean {
        return !cannotConnect(state) && neighborIsFullSquare || state.run {
            (this isIn BlockTags.FENCES && this isIn tag == this@ModernFenceBlock.defaultState.isIn(
                tag
            )
            ) || this.block is FenceGateBlock && FenceGateBlock.canWallConnect(this, dir)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ) = postShape.apply {
        northShape shallBeAddedIf state[NORTH]
        eastShape shallBeAddedIf state[EAST]
        southShape shallBeAddedIf state[SOUTH]
        westShape shallBeAddedIf state[WEST]
    }.get()



    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this, 3).apply {
            input('#', postBlock)
            input('|', sideBlock)
            pattern("#|#")
            pattern("#|#")
            group("modern_fences")
            requires(postBlock)
            offerTo(exporter)
        }
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        TextureMap().apply {
            put(TextureKey.SIDE, sideBlock.textureId)
            put(TextureKey.END, postBlock.textureId)
            put(TextureKey.UP, TextureMap.getSubId(postBlock, "_top"))
        }.let { map ->
            ModModels.modernFenceInventory.upload(getItemModelId(), map, generator.modelCollector)
            generator.blockStateCollector.accept(
                BlockStateModelGenerator.createFenceBlockState(
                    this,
                    ModModels.modernFencePost.upload(this, map, generator.modelCollector),
                    ModModels.modernFenceSide.upload(this, map, generator.modelCollector)
                )
            )
        }
    }
    companion object {
        val postShape: CompositeVoxelShape = CompositeVoxelShape.of(CompositeVoxelShape.createCuboidShape(6, 0, 6, 10, 16, 10))
        private val northShape: CompositeVoxelShape = CompositeVoxelShape(
            CompositeVoxelShape.createCuboidShape(7, 11, 0, 9, 14, 6),
            CompositeVoxelShape.createCuboidShape(7, 2, 0, 9, 5, 6),
            CompositeVoxelShape.createCuboidShape(7.5, 5, 1, 8.5, 11, 2),
            CompositeVoxelShape.createCuboidShape(7.5, 0, 2, 8.5, 15, 5),
            CompositeVoxelShape.createCuboidShape(7.5, 0, 0, 8.5, 15, 1)
        )
        val eastShape = northShape.rotatedLeft()
        val southShape = northShape.flipped()
        val westShape = northShape.rotatedRight()
    }
}


