package com.github.mystery2099.woodenAccentsMod

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import com.github.mystery2099.woodenAccentsMod.entity.ModEntities
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object WoodenAccentsMod : ModInitializer {

    /** The mod's unique identifier. **/
    internal const val MOD_ID = "wooden_accents_mod"

    /** The logger for the mod. **/
    internal val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Initializing $MOD_ID")
        ModBlocks.register()
        ModBlockEntities.register()
        ModEntities.register()
        ModItemGroups.register()
    }

    /**
     * Converts a [String] to an [Identifier] using the mod's namespace.
     *
     * @param namespace The namespace for the [Identifier].
     * @return The [Identifier] with the mod's namespace.
     * @see Identifier
     */
    fun String.toIdentifier(namespace: String = MOD_ID): Identifier = Identifier(namespace, this)

    /**
     * Converts an [Identifier] to a block model [Identifier] with the "block/" prefix.
     *
     * @return The block model [Identifier] with the "block/" prefix.
     */
    fun Identifier.withBlockModelPath(): Identifier = this.withPrefixedPath("block/")


    /**
     * Extension property for [WoodType] to get the corresponding plank [block][Block].
     *
     * @return The plank [block][Block] associated with the [wood type][WoodType].
     */
    val WoodType.planks: Block
        get() = when (this) {
            WoodType.OAK -> Blocks.OAK_PLANKS
            WoodType.SPRUCE -> Blocks.SPRUCE_PLANKS
            WoodType.BIRCH -> Blocks.BIRCH_PLANKS
            WoodType.ACACIA -> Blocks.ACACIA_PLANKS
            WoodType.CHERRY -> Blocks.CHERRY_PLANKS
            WoodType.JUNGLE -> Blocks.JUNGLE_PLANKS
            WoodType.DARK_OAK -> Blocks.DARK_OAK_PLANKS
            WoodType.CRIMSON -> Blocks.CRIMSON_PLANKS
            WoodType.WARPED -> Blocks.WARPED_PLANKS
            WoodType.MANGROVE -> Blocks.MANGROVE_PLANKS
            WoodType.BAMBOO -> Blocks.BAMBOO_PLANKS
            else -> Blocks.OAK_PLANKS
        }
}