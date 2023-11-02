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
 * Block Tag Data Generation
 *
 * This class is responsible for generating block tags for the Wooden Accents Mod.
 *
 * @param output The data output for block tags.
 * @param registriesFuture A [CompletableFuture] for registry lookup.
 */
class BlockTagDataGen(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {

    /**
     * Gets or creates the [FabricTagProvider.FabricTagBuilder] for the associated [TagKey]<[Block]>.
     *
     * @see getOrCreateTagBuilder
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
     * @return The updated tag builder.
     * @see TagKey.plusAssign
     * @see TagKey.forceAdd
     */
    private fun TagKey<Block>.add(tag: TagKey<Block>): FabricTagBuilder = tagBuilder.addTag(tag)

    /**
     * Adds the provided [block] to the current [TagKey]<[Block]> tag builder.
     *
     * @param block The [Block] to add.
     * @return The updated [FabricTagProvider.FabricTagBuilder].
     * @see TagKey.plusAssign
     */
    private fun TagKey<Block>.add(block: Block): FabricTagBuilder = tagBuilder.add(block)

    /**
     * Forces the addition of the provided [tag] to the current [TagKey]<[Block]> tag builder.
     *
     * @param tag The tag to add.
     * @return The updated [FabricTagProvider.FabricTagBuilder].
     */
    private fun TagKey<Block>.forceAdd(tag: TagKey<Block>): FabricTagBuilder = tagBuilder.forceAddTag(tag)

    /**
     * Adds the provided [tag] to the current [TagKey]<[Block]> tag builder.
     *
     * @param tag The tag to add.
     * @see TagKey.add
     * @see TagKey.forceAdd
     */
    private operator fun TagKey<Block>.plusAssign(tag: TagKey<Block>) {
        add(tag)
    }

    /**
     * Adds the provided [block] to the current [TagKey]<[Block]> tag builder.
     *
     * @param block The [Block] to add.
     * @see TagKey.add
     */
    private operator fun TagKey<Block>.plusAssign(block: Block) {
        add(block)
    }

    /**
     * Adds the provided tags to the current TagKey<Block> tag builder.
     *
     * @param tags The array of tags to add.
     * @return The updated tag builder.
     */
    private fun TagKey<Block>.addTags(vararg tags: TagKey<Block>) = tagBuilder.also { tags.forEach(it::addTag) }

    /**
     * Adds the provided block to the current TagKey tag builder.
     * @param blocks the [Array] of [Block]s to add
     * @return The updated [FabricTagProvider.FabricTagBuilder].
     * */
    private fun TagKey<Block>.addBlocks(vararg blocks: Block) = tagBuilder.also { blocks.forEach(it::add) }
}
