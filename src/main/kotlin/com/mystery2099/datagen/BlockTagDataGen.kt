package com.mystery2099.datagen

import com.mystery2099.block.custom.ThinPillarBlock
import com.mystery2099.data.ModBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.HashSet
import java.util.concurrent.CompletableFuture

class BlockTagDataGen( output : FabricDataOutput,  registriesFuture : CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        getOrCreateTagBuilder(ModBlockTags.TEST_TAG).add(Blocks.ACACIA_BUTTON)
        BlockTags.AXE_MINEABLE.addCollection(axeMineable)
    }

    companion object {
        @JvmStatic
        val axeMineable = HashSet<Block>()
    }

    private fun <T : Block> TagKey<Block>.addCollection(collection: Collection<T>) {
        collection.forEach(getOrCreateTagBuilder(this)::add)
    }

}