package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import java.util.function.Consumer

class DeskBlock(settings: FabricBlockSettings, baseBlock: Block, topBlock: Block) :
    AbstractDeskBlock(settings, baseBlock, topBlock) {
    override val itemGroup: CustomItemGroup = ModItemGroups.decorations
    override val tag: TagKey<Block> = ModBlockTags.desks
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {

    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {

    }


}