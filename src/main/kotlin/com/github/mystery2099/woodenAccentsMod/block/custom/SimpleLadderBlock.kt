package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.id
import com.github.mystery2099.woodenAccentsMod.block.itemModelId
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import java.util.function.Consumer

class SimpleLadderBlock(val baseBlock: Block) :
    AbstractCustomLadderBlock(FabricBlockSettings.of(baseBlock.defaultState.material, baseBlock.defaultMapColor).apply {
        hardness(Blocks.LADDER.hardness)
        resistance(Blocks.LADDER.blastResistance)
        sounds(baseBlock.getSoundGroup(baseBlock.defaultState))

        if (baseBlock.requiredFeatures.contains(FeatureFlags.UPDATE_1_20)) {
            requires(FeatureFlags.UPDATE_1_20)
        }
    }) {
    override val tag: TagKey<Block> = ModBlockTags.simpleLadders

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        offerRecipe(exporter, baseBlock, 8, "simple_ladders")
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val ladderTexture = (id.path.removeSuffix("_simple_ladder") + "_ladder").toIdentifier().withPrefixedPath("block/")
        ModModels.simpleLadder.upload(this, TextureMap.of(TextureKey.ALL, ladderTexture), generator.modelCollector)
        Models.GENERATED.upload(itemModelId, TextureMap.layer0(ladderTexture), generator.modelCollector)
        generator.registerNorthDefaultHorizontalRotation(this)
    }
}
