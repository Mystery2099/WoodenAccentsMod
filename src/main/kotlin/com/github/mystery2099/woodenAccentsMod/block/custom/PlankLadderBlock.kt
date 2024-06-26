package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import java.util.function.Consumer

/**
 * PlankLadderBlock class represents a custom ladder block made of planks.
 *
 * This class extends AbstractCustomLadderBlock class and provides additional functionality for custom ladder blocks made of planks.
 *
 * @property baseBlock The base block used to create the plank ladder block.
 * @constructor Creates an instance of PlankLadderBlock with the specified base block.
 */
class PlankLadderBlock(val baseBlock: Block) :
    AbstractCustomLadderBlock(FabricBlockSettings.of(baseBlock.defaultState.material, baseBlock.defaultMapColor).apply {
        hardness(Blocks.LADDER.hardness)
        resistance(Blocks.LADDER.blastResistance)
        sounds(baseBlock.getSoundGroup(baseBlock.defaultState))

        if (baseBlock.requiredFeatures.contains(FeatureFlags.UPDATE_1_20)) {
            requires(FeatureFlags.UPDATE_1_20)
        }
    }), CustomItemGroupProvider {
    override val tag: TagKey<Block> = ModBlockTags.plankLadders

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState, world: BlockView?, pos: BlockPos?, context: ShapeContext?
    ): VoxelShape = when (state[FACING]) {
        Direction.NORTH -> northShape
        Direction.EAST -> eastShape
        Direction.SOUTH -> southShape
        Direction.WEST -> westShape
        else -> super.getOutlineShape(state, world, pos, context)
    }


    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        offerRecipe(exporter, baseBlock, 8, "plank_ladders")
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        ModModels.plankLadder.upload(this, TextureMap.all(baseBlock), generator.modelCollector)
        generator.registerNorthDefaultHorizontalRotation(this)
    }

    companion object {
        private val northShape = VoxelAssembly.createCuboidShape(2, 1, 15, 14, 15, 16)
        private val eastShape = northShape.rotateLeft()
        private val southShape = northShape.flip()
        private val westShape = northShape.rotateRight()
    }
}