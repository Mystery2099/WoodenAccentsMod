package com.mystery2099.datagen

import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.*
import com.mystery2099.data.ModBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.block.LadderBlock
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

class BlockTagDataGen( output : FabricDataOutput,  registriesFuture : CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        ModBlocks.blocks.forEach{
            BlockTags.AXE_MINEABLE.addBlock(it)
            when (it) {
                is ThinPillarBlock -> ModBlockTags.thinPillars.addBlock(it)
                is ThickPillarBlock -> ModBlockTags.thickPillars.addBlock(it)
                is CustomWallBlock -> ModBlockTags.woodenWalls.addBlock(it)
                is PlankLadderBlock -> ModBlockTags.plankLadders.addBlock(it)
                is TableBlock -> ModBlockTags.tables.addBlock(it)
                is ThinBookshelfBlock -> ModBlockTags.thinBookshelves.addBlock(it)
                is FloorCoveringBlock -> ModBlockTags.plankCarpets.addBlock(it)
                is CoffeeTableBlock -> ModBlockTags.coffeeTables.addBlock(it)
                is KitchenCounterBlock -> ModBlockTags.kitchenCounters.addBlock(it)
                is KitchenCabinetBlock -> ModBlockTags.kitchenCabinets.addBlock(it)
            }
        }
        ModBlockTags.pillars.addTags(ModBlockTags.thinPillars, ModBlockTags.thickPillars)
        BlockTags.WALLS.addTag(ModBlockTags.woodenWalls)
        BlockTags.CLIMBABLE.addTag(ModBlockTags.plankLadders)
        BlockTags.INSIDE_STEP_SOUND_BLOCKS.addTag(ModBlockTags.plankCarpets)
        ModBlockTags.kitchenCounters.addTag(ModBlockTags.kitchenCabinets)
    }

    private fun <T : Block> TagKey<Block>.addCollection(collection: Collection<T>): FabricTagBuilder {
        return getOrCreateTagBuilder(this).also { collection.forEach(it::add) }
    }

    private fun TagKey<Block>.addTags(vararg tags: TagKey<Block>): FabricTagBuilder {
        return getOrCreateTagBuilder(this).also { tags.forEach(it::addTag) }
    }
    private fun TagKey<Block>.addTag(tag: TagKey<Block>): FabricTagBuilder {
        return getOrCreateTagBuilder(this).addTag(tag)
    }

    private fun TagKey<Block>.addBlock(block: Block): FabricTagBuilder {
        return getOrCreateTagBuilder(this).add(block)
    }
    private fun TagKey<Block>.addBlocks(vararg blocks: Block): FabricTagBuilder {
        return getOrCreateTagBuilder(this).also { blocks.forEach(it::add) }
    }

}