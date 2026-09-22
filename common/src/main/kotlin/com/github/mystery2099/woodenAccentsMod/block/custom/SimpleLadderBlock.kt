package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.id
import com.github.mystery2099.woodenAccentsMod.block.itemModelId
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.state.BlockBehaviour

class SimpleLadderBlock(val baseBlock: Block) :
    AbstractCustomLadderBlock(BlockBehaviour.Properties.of().apply {
        mapColor(baseBlock.defaultMapColor())
        destroyTime(Blocks.LADDER.defaultDestroyTime())
        explosionResistance(Blocks.LADDER.explosionResistance)
        sound(baseBlock.getSoundType(baseBlock.defaultBlockState()))
        instrument(baseBlock.defaultBlockState().instrument())
        if (baseBlock.defaultBlockState().ignitedByLava()) ignitedByLava()
    }) {
    override val tag: TagKey<Block> = ModBlockTags.simpleLadders

    override fun offerRecipeTo(recipeExporter: RecipeOutput) {
        offerRecipe(recipeExporter, baseBlock, 8, "simple_ladders")
    }

    override fun generateBlockStateModels(generator: BlockModelGenerators) {
        val ladderTexture = (id.path.removeSuffix("_simple_ladder") + "_ladder").toIdentifier().withPrefix("block/")
        ModModels.simpleLadder.create(this, TextureMapping.singleSlot(TextureSlot.ALL, ladderTexture), generator.modelOutput)
        ModelTemplates.FLAT_ITEM.create(itemModelId, TextureMapping.layer0(ladderTexture), generator.modelOutput)
        generator.createNonTemplateHorizontalBlock(this)
    }
}
