package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.withBlockModelPath
import com.github.mystery2099.woodenAccentsMod.block.itemModelId
import com.github.mystery2099.woodenAccentsMod.block.woodType
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import java.util.function.Consumer

class ThickPillarBlock(baseBlock: Block) : AbstractPillarBlock(baseBlock, shape) {
    override val connectableBlockTag: TagKey<Block> = ModBlockTags.thickPillarsConnectable
    override val tag: TagKey<Block> = ModBlockTags.thickPillars

    @Deprecated("Deprecated in Java")
    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = if (!state[AbstractPillarBlock.up] && !state[AbstractPillarBlock.down]) {
        VoxelShapes.fullCube()
    } else {
        getOutlineShape(state, world, pos, context)
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        this.offerRecipe(exporter = exporter, outputNum = 6, primaryInput = baseBlock, secondaryInput = baseBlock)
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val map = TextureMap.all(this.baseBlock)
        generator.blockStateCollector.accept(
            this.genBlockStateModelSupplier(
                centerModel = "${this.woodType.name.lowercase()}_plank_wall_post".toIdentifier().withBlockModelPath(),
                bottomModel = ModModels.thickPillarBottom.upload(this, map, generator.modelCollector)
            )
        )
        ModModels.thickPillarInventory.upload(this.itemModelId, map, generator.modelCollector)
    }

    companion object {
        val shape = Shape(
            topShape = VoxelAssembly.createCuboidShape(1, 10, 1, 15, 16, 15),
            centerShape = VoxelAssembly.createCuboidShape(4, 0, 4, 12, 16, 12),
            baseShape = VoxelAssembly.createCuboidShape(1, 0, 1, 15, 6, 15)
        )
    }
}
