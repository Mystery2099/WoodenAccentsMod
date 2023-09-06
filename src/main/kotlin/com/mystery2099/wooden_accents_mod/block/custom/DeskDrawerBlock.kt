package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import java.util.function.Consumer

class DeskDrawerBlock(settings: Settings, val baseBlock: Block, val topBlock: Block) :
    WaterloggableBlockWithEntity(settings), CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>,
    CustomBlockStateProvider {

    override val itemGroup: CustomItemGroup
        get() = ModItemGroups.decorations
    override val tag: TagKey<Block>
        get() = ModBlockTags.deskDrawers

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        TODO("Not yet implemented")
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {

    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {

    }
}