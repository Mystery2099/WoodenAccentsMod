package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.block.textureId
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WallBlock
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.TagKey
import java.util.function.Consumer

class CustomWallBlock(val baseBlock: Block) : WallBlock(FabricBlockSettings.copyOf(baseBlock)), CustomItemGroupProvider,
    CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val tag: TagKey<Block> = ModBlockTags.woodenWalls
    override val itemGroup = ModItemGroups.building
    override fun offerRecipeTo(exporter: Consumer<FinishedRecipe>) {
        FabricRecipeProvider.wall(exporter, RecipeCategory.DECORATIONS, this, baseBlock)
    }

    override fun generateBlockStateModels(generator: BlockModelGenerators) {
        TextureMapping().put(TextureSlot.WALL, this.baseBlock.textureId).let { map ->
            generator.blockStateOutput.accept(
                BlockModelGenerators.createWall(
                    this,
                    ModelTemplates.WALL_POST.create(this, map, generator.modelOutput),
                    ModelTemplates.WALL_LOW_SIDE.create(this, map, generator.modelOutput),
                    ModelTemplates.WALL_TALL_SIDE.create(this, map, generator.modelOutput)
                )
            )
            generator.delegateItemModel(this, ModelTemplates.WALL_INVENTORY.create(this, map, generator.modelOutput))
        }
    }
}
