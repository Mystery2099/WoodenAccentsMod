package com.mystery2099.datagen

import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.*
import com.mystery2099.data.ModBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

class BlockTagDataGen( output : FabricDataOutput,  registriesFuture : CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        BlockTags.AXE_MINEABLE.addCollection(ModBlocks.blocks)
        //Pillars
        ModBlockTags.thinPillars.addCollection(ThinPillarBlock.instances)
        ModBlockTags.thickPillars.addCollection(ThickPillarBlock.instances)
        ModBlockTags.pillars.addTags(ModBlockTags.thinPillars, ModBlockTags.thickPillars)

        //Tables
        ModBlockTags.tables.addCollection(TableBlock.instances)

        //Coffee Tables
        ModBlockTags.coffeeTables.addCollection(CoffeeTableBlock.instances)

        //Kitchen Counters
        ModBlockTags.kitchenCounters.addCollection(KitchenCounterBlock.instances)
    }

    private fun <T : Block> TagKey<Block>.addCollection(collection: Collection<T>): FabricTagBuilder {
        return getOrCreateTagBuilder(this).also { collection.forEach(it::add) }
    }

    private fun TagKey<Block>.addTags(vararg tags: TagKey<Block>): FabricTagBuilder {
        return getOrCreateTagBuilder(this).also { tags.forEach(it::addTag) }
    }

}