package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.woodenAccentsMod.block.woodType
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
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

/**
 * The ModernFenceGateBlock class represents a custom modern fence gate block that extends the FenceGateBlock class
 * and implements several custom interfaces for item group, recipe, tag, and block state generation.
 *
 * @param baseGate The base FenceGateBlock to copy settings from.
 * @param baseBlock The base Block to use in recipe and block state generation.
 */
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
        private val shape1 = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(3, 15, 7, 5, 16, 9),
            VoxelAssembly.createCuboidShape(11, 15, 7, 13, 16, 9),
            VoxelAssembly.createCuboidShape(0, 0, 7, 1, 15, 9),
            VoxelAssembly.createCuboidShape(15, 0, 7, 16, 15, 9),
            VoxelAssembly.createCuboidShape(1, 1, 7, 15, 15, 9)
        )
        private val shape2 = shape1.rotateLeft()
        private val wallShape1 = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(15, 0, 7, 16, 1, 9),
            VoxelAssembly.createCuboidShape(0, 0, 7, 1, 1, 9),
            VoxelAssembly.createCuboidShape(3, 15, 7, 5, 16, 9),
            VoxelAssembly.createCuboidShape(11, 15, 7, 13, 16, 9),
            VoxelAssembly.createCuboidShape(3, 14, 7, 13, 15, 9),
            VoxelAssembly.createCuboidShape(0, 1, 7, 16, 14, 9)
        )
        private val wallShape2 = wallShape1.rotateLeft()
    }
}