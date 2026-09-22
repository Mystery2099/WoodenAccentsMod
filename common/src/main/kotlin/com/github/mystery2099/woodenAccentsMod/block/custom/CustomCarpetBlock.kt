package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.block.modelId
import com.github.mystery2099.woodenAccentsMod.block.textureId
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeUtil.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeUtil.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroup
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CarpetBlock
import net.minecraft.world.level.material.PushReaction
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.TagKey
import java.util.function.Consumer
import net.minecraft.world.level.block.state.BlockBehaviour

class CustomCarpetBlock(val baseBlock: Block) : CarpetBlock(
    BlockBehaviour.Properties.of().strength(0.1f).pushReaction(PushReaction.DESTROY).apply {
        mapColor(baseBlock.defaultMapColor())
        sound(baseBlock.getSoundType(baseBlock.defaultBlockState()))
        if (baseBlock.defaultBlockState().ignitedByLava()) ignitedByLava()
    }
), CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {

    override val itemGroup = ModItemGroup.BUILDING
    override val tag: TagKey<Block> = ModBlockTags.plankCarpets
    override fun offerRecipeTo(exporter: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, this, 3).apply {
            define('#', baseBlock)
            define('_', Items.PAPER)
            pattern("##")
            pattern("_ ")
            customGroup(this@CustomCarpetBlock, "carpets")
            requires(baseBlock)
            save(exporter)
        }
    }

    override fun generateBlockStateModels(generator: BlockModelGenerators) {
            generator.createTrivialBlock(this, TextureMapping().put(TextureSlot.WOOL, this.baseBlock.textureId), ModelTemplates.CARPET)
            generator.delegateItemModel(this, this.modelId)
    }
}
