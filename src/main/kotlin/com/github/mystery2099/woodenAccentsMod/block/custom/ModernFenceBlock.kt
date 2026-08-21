package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.combination.VoxelAssembly.plus
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.block.BlockStateUtil.isIn
import com.github.mystery2099.woodenAccentsMod.block.itemModelId
import com.github.mystery2099.woodenAccentsMod.block.textureId
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

class ModernFenceBlock(settings: Block, private val sideBlock: Block, private val postBlock: Block) :
    FenceBlock(FabricBlockSettings.copyOf(settings)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
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
    ): VoxelShape = outlineShapes[getConnectionIndex(state)]


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
            ModModels.modernFenceInventory.upload(itemModelId, map, generator.modelCollector)
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
        private val postShape: VoxelShape = VoxelAssembly.createCuboidShape(6, 0, 6, 10, 16, 10)
        private val northShape: VoxelShape = VoxelAssembly.createCuboidShape(7, 0, 0, 9, 15, 6)
        val directionToShapeMap = mapOf(
            Direction.NORTH to northShape,
            Direction.EAST to northShape.rotateLeft(),
            Direction.SOUTH to northShape.flip(),
            Direction.WEST to northShape.rotateRight()
        )

        private val outlineShapes = Array(16) { connections ->
            var shape = postShape
            if (connections and NORTH_CONNECTION != 0) shape += directionToShapeMap.getValue(Direction.NORTH)
            if (connections and EAST_CONNECTION != 0) shape += directionToShapeMap.getValue(Direction.EAST)
            if (connections and SOUTH_CONNECTION != 0) shape += directionToShapeMap.getValue(Direction.SOUTH)
            if (connections and WEST_CONNECTION != 0) shape += directionToShapeMap.getValue(Direction.WEST)
            shape
        }

        private fun getConnectionIndex(state: BlockState): Int {
            var connections = 0
            if (state[NORTH]) connections = connections or NORTH_CONNECTION
            if (state[EAST]) connections = connections or EAST_CONNECTION
            if (state[SOUTH]) connections = connections or SOUTH_CONNECTION
            if (state[WEST]) connections = connections or WEST_CONNECTION
            return connections
        }

        private const val NORTH_CONNECTION = 1
        private const val EAST_CONNECTION = 2
        private const val SOUTH_CONNECTION = 4
        private const val WEST_CONNECTION = 8
    }
}
