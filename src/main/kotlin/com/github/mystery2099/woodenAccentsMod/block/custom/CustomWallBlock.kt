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
import net.minecraft.block.Block
import net.minecraft.block.WallBlock
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import java.util.function.Consumer

/**
 * Represents a custom wall block that extends the [WallBlock] class.
 *
 * This class provides functionality for custom item group, custom recipes, custom tags, and custom block state models.
 *
 * @param baseBlock The base block used to create the custom wall block.
 */
class CustomWallBlock(val baseBlock: Block) : WallBlock(FabricBlockSettings.copyOf(baseBlock)), CustomItemGroupProvider,
    CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val tag: TagKey<Block> = ModBlockTags.woodenWalls
    override val itemGroup = ModItemGroups.structuralElements
    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        FabricRecipeProvider.offerWallRecipe(exporter, RecipeCategory.DECORATIONS, this, baseBlock)
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        TextureMap().put(TextureKey.WALL, this.baseBlock.textureId).let { map ->
            generator.blockStateCollector.accept(
                BlockStateModelGenerator.createWallBlockState(
                    this,
                    Models.TEMPLATE_WALL_POST.upload(this, map, generator.modelCollector),
                    Models.TEMPLATE_WALL_SIDE.upload(this, map, generator.modelCollector),
                    Models.TEMPLATE_WALL_SIDE_TALL.upload(this, map, generator.modelCollector)
                )
            )
            generator.registerParentedItemModel(this, Models.WALL_INVENTORY.upload(this, map, generator.modelCollector))
        }
    }
}