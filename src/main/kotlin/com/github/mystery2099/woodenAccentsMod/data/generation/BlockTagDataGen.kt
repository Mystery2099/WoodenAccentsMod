package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.data.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

/**
 * Block tag data gen
 *
 * @constructor
 *
 * @param output
 * @param registriesFuture
 */
class BlockTagDataGen(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {

    private val TagKey<Block>.tagBuilder: FabricTagBuilder
        get() = getOrCreateTagBuilder(this)

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        BlockTags.AXE_MINEABLE.addTags(*ModBlockTags.blockToItemTagMap.keys.toTypedArray())
        ModBlocks.blocks.filterIsInstance<CustomTagProvider<Block>>().forEach {
            it.tag += it as Block
        }
        ModBlockTags.pillars.addTags(ModBlockTags.thinPillars, ModBlockTags.thickPillars)
        ModBlockTags.thinPillarsConnectable.apply {
            addTags(
                ModBlockTags.thinPillars,
                BlockTags.FENCES,
                ModBlockTags.supportBeams,
                ModBlockTags.tables,
            )
            addBlocks(Blocks.END_ROD, Blocks.HOPPER, Blocks.LIGHTNING_ROD)
        }
        ModBlockTags.thickPillarsConnectable.addTags(
            ModBlockTags.thickPillars,
            ModBlockTags.thinPillars,
            BlockTags.WALLS
        )

        BlockTags.WALLS += ModBlockTags.woodenWalls

        BlockTags.FENCES += ModBlockTags.modernFences
        BlockTags.FENCE_GATES += ModBlockTags.modernFenceGates

        ModBlockTags.modernFenceConnectable.addTags(ModBlockTags.modernFenceGates, ModBlockTags.modernFences)

        BlockTags.CLIMBABLE += ModBlockTags.plankLadders
        BlockTags.CLIMBABLE += ModBlockTags.connectingLadders

        BlockTags.INSIDE_STEP_SOUND_BLOCKS += ModBlockTags.plankCarpets

        ModBlockTags.kitchenCounters += ModBlockTags.kitchenCabinets
    }



    private fun TagKey<Block>.add(tag: TagKey<Block>): FabricTagBuilder = tagBuilder.addTag(tag)
    private fun TagKey<Block>.add(block: Block): FabricTagBuilder = tagBuilder.add(block)
    private fun TagKey<Block>.forceAdd(tag: TagKey<Block>): FabricTagBuilder = tagBuilder.forceAddTag(tag)

    private fun TagKey<Block>.addTags(vararg tags: TagKey<Block>) = tagBuilder.also { tags.forEach(it::addTag) }
    private fun TagKey<Block>.addBlocks(vararg blocks: Block) = tagBuilder.also { blocks.forEach(it::add) }

    private operator fun TagKey<Block>.plusAssign(tag: TagKey<Block>) {
        add(tag)
    }

    private operator fun TagKey<Block>.plusAssign(block: Block) {
        add(block)
    }
}
