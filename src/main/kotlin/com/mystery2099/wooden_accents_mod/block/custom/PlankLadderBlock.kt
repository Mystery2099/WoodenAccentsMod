package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.combined
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import java.util.function.Consumer

class PlankLadderBlock(val baseBlock: Block) : AbstractCustomLadderBlock(FabricBlockSettings.copyOf(Blocks.LADDER)),
    GroupedBlock {

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
        private val northShapes = arrayOf(
            createCuboidShape(2.0, 1.0, 15.0, 14.0, 4.0, 16.0),
            createCuboidShape(2.0, 12.0, 15.0, 14.0, 15.0, 16.0),
            createCuboidShape(2.0, 6.0, 15.0, 14.0, 10.0, 16.0)
        )
        private val northShape = northShapes.combined
        private val eastShape = northShapes.rotateLeft()
        private val southShape = northShapes.flip()
        private val westShape = northShapes.rotateRight()
    }
}