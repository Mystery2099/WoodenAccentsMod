package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture


/**
 * The `BlockTagDataGen` class is responsible for generating block tags during data generation.
 *
 * @param output The [FabricDataOutput] to write the generated data to.
 * @param registriesFuture The [CompletableFuture] representing the lookup of the wrapper registries.
 * @see FabricTagProvider.BlockTagProvider
 */
class BlockTagDataGen(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    /**
     * The `tagBuilder` property is a private extension property of type `FabricTagBuilder`.
     * It is accessed through a getter function that returns the result of calling the `getOrCreateTagBuilder` function with the current `TagKey<Block>` instance.
     *
     * @return The `FabricTagBuilder` instance associated with the current `TagKey<Block>`.
     *
     * @see getOrCreateTagBuilder
     * @see TagKey
     * @see FabricTagProvider.FabricTagBuilder
     */
    private val TagKey<Block>.tagBuilder: FabricTagBuilder
        get() = getOrCreateTagBuilder(this)

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        BlockTags.AXE_MINEABLE.addTags(*ModBlockTags.blockToItemTagMap.keys.toTypedArray())

        ModBlocks.blocks.filterIsInstance<CustomTagProvider<Block>>().forEach {
            it.tag += it as Block
        }


        // Define relationships between custom block tags
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
        ModBlockTags.modernFenceConnectable.addTags(ModBlockTags.modernFenceGates, ModBlockTags.modernFences)
        ModBlockTags.kitchenCounters += ModBlockTags.kitchenCabinets

        // Update predefined block tags with custom block tags
        BlockTags.WALLS += ModBlockTags.woodenWalls
        BlockTags.FENCES += ModBlockTags.modernFences
        BlockTags.FENCE_GATES += ModBlockTags.modernFenceGates
        BlockTags.CLIMBABLE.addTags(ModBlockTags.plankLadders, ModBlockTags.connectingLadders)
        BlockTags.INSIDE_STEP_SOUND_BLOCKS += ModBlockTags.plankCarpets
    }


    /**
     * Adds the provided [tag] to the current [TagKey]<[Block]> tag builder.
     *
     * @param tag The tag to add.
     * @return The updated [FabricTagProvider.FabricTagBuilder].
     */
    private fun TagKey<Block>.add(tag: TagKey<Block>): FabricTagBuilder = tagBuilder.addTag(tag)
    /**
     * Adds the provided [block] to the current [TagKey]<[Block]> tag builder.
     *
     * @param block The [Block] to add.
     * @return The updated [FabricTagProvider.FabricTagBuilder].
     */
    private fun TagKey<Block>.add(block: Block): FabricTagBuilder = tagBuilder.add(block)

    /**
     * Forcefully adds the provided [tag] to the current [TagKey]<[Block]> tag builder.
     *
     * @param tag The tag to add.
     * @return The updated [FabricTagProvider.FabricTagBuilder].
     */
    private fun TagKey<Block>.forceAdd(tag: TagKey<Block>): FabricTagBuilder = tagBuilder.forceAddTag(tag)

    /**
     * Adds the provided [tag] to the current [TagKey]<[Block]> tag builder.
     * This method is an operator function for the `+=` operator.
     *
     * @param tag The tag to add.
     */
    private operator fun TagKey<Block>.plusAssign(tag: TagKey<Block>) {
        add(tag)
    }

    /**
     * Adds the provided [block] to the current [TagKey]<[Block]> tag builder.
     *
     * @param block The [Block] to add.
     */
    private operator fun TagKey<Block>.plusAssign(block: Block) {
        add(block)
    }

    private fun TagKey<Block>.addTags(vararg tags: TagKey<Block>) = tagBuilder.also { tags.forEach(it::addTag) }

    private fun TagKey<Block>.addBlocks(vararg blocks: Block) = tagBuilder.also { blocks.forEach(it::add) }
}
