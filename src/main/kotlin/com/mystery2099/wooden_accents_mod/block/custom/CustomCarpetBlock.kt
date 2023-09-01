package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.modelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.customGroup
import com.mystery2099.wooden_accents_mod.datagen.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.CarpetBlock
import net.minecraft.block.Material
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import java.util.function.Consumer

class CustomCarpetBlock(val baseBlock: Block) : CarpetBlock(
    FabricBlockSettings.of(Material.CARPET).strength(0.1f).apply {
        mapColor(baseBlock.defaultMapColor)
        sounds(baseBlock.getSoundGroup(baseBlock.defaultState))
        if (baseBlock.requiredFeatures.contains(FeatureFlags.UPDATE_1_20)) {
            requires(FeatureFlags.UPDATE_1_20)
        }
    }
), CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider, CustomBlockStateProvider {

    override val itemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.plankCarpets
    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 3).apply {
            input('#', baseBlock)
            input('_', Items.PAPER)
            pattern("##")
            pattern("_ ")
            customGroup(this@CustomCarpetBlock, "carpets")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
            generator.registerSingleton(this, TextureMap().put(TextureKey.WOOL, this.baseBlock.textureId), Models.CARPET)
            generator.registerParentedItemModel(this, this.modelId)
    }
}