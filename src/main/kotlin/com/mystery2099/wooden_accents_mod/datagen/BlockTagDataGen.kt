package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.block.custom.KitchenCounterBlock
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.*
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

class BlockTagDataGen( output : FabricDataOutput,  registriesFuture : CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        ModBlocks.blocks.forEach{
            BlockTags.AXE_MINEABLE += it
            when (it) {
                is ThinPillarBlock -> ModBlockTags.thinPillars += it
                is ThickPillarBlock -> ModBlockTags.thickPillars +=it
                is CustomWallBlock -> ModBlockTags.woodenWalls += it
                is ModernFenceBlock -> ModBlockTags.modernFences += it
                is ModernFenceGateBlock -> ModBlockTags.modernFenceGates += it
                is PlankLadderBlock -> ModBlockTags.plankLadders += it
                is TableBlock -> ModBlockTags.tables += it
                is ThinBookshelfBlock -> ModBlockTags.thinBookshelves += it
                is FloorCoveringBlock -> ModBlockTags.plankCarpets += it
                is CoffeeTableBlock -> ModBlockTags.coffeeTables += it
                is KitchenCounterBlock -> ModBlockTags.kitchenCounters += it
                is KitchenCabinetBlock -> ModBlockTags.kitchenCabinets += it
            }
        }
        ModBlockTags.pillars.add(ModBlockTags.thinPillars, ModBlockTags.thickPillars)
        BlockTags.WALLS += ModBlockTags.woodenWalls
        BlockTags.FENCES += ModBlockTags.modernFences
        BlockTags.FENCE_GATES += ModBlockTags.modernFenceGates
        BlockTags.CLIMBABLE += ModBlockTags.plankLadders
        BlockTags.INSIDE_STEP_SOUND_BLOCKS += ModBlockTags.plankCarpets
        ModBlockTags.kitchenCounters += ModBlockTags.kitchenCabinets
    }

    private fun <T : Block> TagKey<Block>.add(collection: Collection<T>): FabricTagBuilder {
        return getOrCreateTagBuilder(this).also { collection.forEach(it::add) }
    }

    private fun TagKey<Block>.add(vararg tags: TagKey<Block>): FabricTagBuilder {
        return getOrCreateTagBuilder(this).also { tags.forEach(it::addTag) }
    }
    private fun TagKey<Block>.add(tag: TagKey<Block>): FabricTagBuilder = getOrCreateTagBuilder(this).addTag(tag)

    private fun TagKey<Block>.add(block: Block): FabricTagBuilder = getOrCreateTagBuilder(this).add(block)
    private fun TagKey<Block>.add(vararg blocks: Block): FabricTagBuilder {
        return getOrCreateTagBuilder(this).also { blocks.forEach(it::add) }
    }
    private operator fun TagKey<Block>.plusAssign(tag: TagKey<Block>) { add(tag)}
    private operator fun TagKey<Block>.plusAssign(block: Block) { add(block) }
    private operator fun TagKey<Block>.plusAssign(tags: Array<TagKey<Block>>) { tags.map{ add(it) } }

}