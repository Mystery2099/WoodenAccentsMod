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

/**
 * Singleton object representing the Wooden Accents Mod.
 * Implements the ModInitializer interface for mod initialization.
 */
object WoodenAccentsMod : ModInitializer {

    /**
     * This constant represents the ID of the Wooden Accents Mod.
     * It is an internal constant and has a value of "wooden_accents_mod".
     */
    internal const val MOD_ID = "wooden_accents_mod"

    /**
     * The logger for the software.
     */
    internal val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Initializing $MOD_ID")
        ModBlocks.register()
        ModBlockEntities.register()
        ModEntities.register()
        ModItemGroups.register()
    }

    /**
     * Converts a string to an [Identifier] object.
     *
     * @param namespace The namespace prefix for the identifier. Defaults to [MOD_ID].
     * @return The converted [Identifier] object.
     */
    fun String.toIdentifier(namespace: String = MOD_ID): Identifier = Identifier(namespace, this)


    /**
     * @return A new [Identifier] with the block model path prefix.
     */
    fun Identifier.withBlockModelPath(): Identifier = this.withPrefixedPath("block/")


    /**
     * Returns the corresponding block for the given WoodType.
     *
     * @return The corresponding block for the WoodType.
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