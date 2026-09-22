package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.woodenAccentsMod.block.woodType
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeUtil.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroup
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.TagKey
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.world.level.BlockGetter
import java.util.function.Consumer
import net.minecraft.world.level.block.state.BlockBehaviour

class ModernFenceGateBlock(baseGate: FenceGateBlock, val baseBlock: Block) : FenceGateBlock(BlockBehaviour.Properties.copy(baseGate), baseGate.woodType),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val tag: TagKey<Block> = ModBlockTags.modernFenceGates
    override val itemGroup = ModItemGroup.BUILDING

    @Deprecated("Deprecated in Java")
    override fun getShape(
        state: BlockState,
        world: BlockGetter?,
        pos: BlockPos?,
        context: CollisionContext?
    ): VoxelShape = when (state.getValue(FACING)) {
        Direction.NORTH, Direction.SOUTH -> if (state.getValue(IN_WALL)) wallShape1 else shape1
        Direction.EAST, Direction.WEST -> if (state.getValue(IN_WALL)) wallShape2 else shape2
        else -> super.getShape(state, world, pos, context)
    }


    override fun offerRecipeTo(exporter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, this).apply {
            define('#', baseBlock)
            define('|', Items.STICK)
            pattern("|#|")
            pattern("|#|")
            group("modern_fence_gates")
            requires(baseBlock)
            save(exporter)
        }
    }

    override fun generateBlockStateModels(generator: BlockModelGenerators) {
        TextureMapping.cube(baseBlock).let { map ->
            val model = ModModels.modernFenceGate.create(this, map, generator.modelOutput)
            val openModel = ModModels.modernFenceGateOpen.create(this, map, generator.modelOutput)
            val wallModel = ModModels.modernFenceGateWall.create(this, map, generator.modelOutput)
            val openWallModel = ModModels.modernFenceGateWallOpen.create(this, map, generator.modelOutput)
            generator.blockStateOutput.accept(BlockModelGenerators.createFenceGate(
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
