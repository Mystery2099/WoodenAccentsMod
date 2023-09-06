package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.createCuboidShape
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedLeft
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FenceGateBlock
import net.minecraft.block.ShapeContext
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import java.util.function.Consumer

class ModernFenceGateBlock(baseGate: FenceGateBlock, val baseBlock: Block) : FenceGateBlock(FabricBlockSettings.copyOf(baseGate), baseGate.woodType),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val tag: TagKey<Block> = ModBlockTags.modernFenceGates
    override val itemGroup = ModItemGroups.structuralElements

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = when (state[FACING]) {
        Direction.NORTH, Direction.SOUTH -> if (state[IN_WALL]) wallShape1 else shape1
        Direction.EAST, Direction.WEST -> if (state[IN_WALL]) wallShape2 else shape2
        else -> super.getOutlineShape(state, world, pos, context)
    }


    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this).apply {
            input('#', baseBlock)
            input('|', Items.STICK)
            pattern("|#|")
            pattern("|#|")
            group("modern_fence_gates")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        TextureMap.all(baseBlock).let { map ->
            val model = ModModels.modernFenceGate.upload(this, map, generator.modelCollector)
            val openModel = ModModels.modernFenceGateOpen.upload(this, map, generator.modelCollector)
            val wallModel = ModModels.modernFenceGateWall.upload(this, map, generator.modelCollector)
            val openWallModel = ModModels.modernFenceGateWallOpen.upload(this, map, generator.modelCollector)
            generator.blockStateCollector.accept(BlockStateModelGenerator.createFenceGateBlockState(
                this,
                openModel,
                model,
                openWallModel,
                wallModel,
                false
            ))
        }
    }

    companion object {
        private val defaultShapes = VoxelShapeHelper.union(
            createCuboidShape(0, 0, 7.5, 1, 15, 8.5),
            createCuboidShape(15, 0, 7.5, 16, 15, 8.5),
            createCuboidShape(11, 1, 7.5, 13, 16, 8.5),
            createCuboidShape(3, 1, 7.5, 5, 16, 8.5),
            createCuboidShape(7, 1, 7.5, 9, 15, 8.5),
            createCuboidShape(0, 11, 7, 16, 14, 9),
            createCuboidShape(0, 2, 7, 16, 5, 9),
            createCuboidShape(0, 2, 7.5, 16, 14, 8.5)
        )
        private val shape1 = defaultShapes
        private val shape2 = defaultShapes.rotatedLeft
        private val wallShapes = VoxelShapeHelper.union(
            createCuboidShape(0, 0, 7, 1, 14, 9),
            createCuboidShape(15, 0, 7, 16, 14, 9),
            createCuboidShape(1, 11, 7, 15, 14, 9),
            createCuboidShape(1, 2, 7, 15, 5, 9),
            createCuboidShape(1, 5, 7.5, 15, 11, 8.5),
            createCuboidShape(11, 1, 7.5, 13, 16, 8.5),
            createCuboidShape(3, 1, 7.5, 5, 16, 8.5),
            createCuboidShape(7, 1, 7.5, 9, 15, 8.5)
        )
        private val wallShape1 = wallShapes
        private val wallShape2 = wallShapes.rotatedLeft
    }
}