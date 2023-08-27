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
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FenceBlock
import net.minecraft.block.ShapeContext
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import java.util.function.Consumer

class ModernFenceBlock(settings: Block, val sideBlock: Block, val postBlock: Block) :
    FenceBlock(FabricBlockSettings.copyOf(settings)),
    GroupedBlock, RecipeBlockData, TaggedBlock, BlockStateGeneratorDataBlock {
    override val tag: TagKey<Block> = ModBlockTags.modernFences
    override val itemGroup = ModItemGroups.structuralElements

    override fun canConnect(state: BlockState, neighborIsFullSquare: Boolean, dir: Direction): Boolean {
        return !cannotConnect(state) && neighborIsFullSquare || state isIn ModBlockTags.modernFenceConnectable
    }

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ) = CompositeVoxelShape(postShape).apply {
        if (state[NORTH]) this += directionToShapeMap[Direction.NORTH]
        if (state[EAST]) this += directionToShapeMap[Direction.EAST]
        if (state[SOUTH]) this += directionToShapeMap[Direction.SOUTH]
        if (state[WEST]) this += directionToShapeMap[Direction.WEST]
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
        private val postShape: VoxelShape = VoxelShapeHelper.createCuboidShape(6, 0, 6, 10, 16, 10)
        private val northShape: CompositeVoxelShape = CompositeVoxelShape(
            VoxelShapeHelper.createCuboidShape(7, 11, 0, 9, 14, 6),
            VoxelShapeHelper.createCuboidShape(7, 2, 0, 9, 5, 6),
            VoxelShapeHelper.createCuboidShape(7.5, 5, 1, 8.5, 11, 2),
            VoxelShapeHelper.createCuboidShape(7.5, 0, 2, 8.5, 15, 5),
            VoxelShapeHelper.createCuboidShape(7.5, 0, 0, 8.5, 15, 1)
        )
        val directionToShapeMap = mapOf(
            Direction.NORTH to northShape.get(),
            Direction.EAST to northShape.rotatedLeft().get(),
            Direction.SOUTH to northShape.flipped().get(),
            Direction.WEST to northShape.rotatedRight().get()
        )
    }
}


