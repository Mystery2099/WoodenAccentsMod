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
    const val MOD_ID = "wooden_accents_mod"
    val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Initializing $MOD_ID")
        ModBlocks.register()
        ModBlockEntities.register()
        ModEntities.register()
        ModItemGroups.register()
    }

    /**
     * Converts a path to an [Identifier] using the mod's namespace.
     *
     * @param path The [String] path to convert to an [Identifier].
     * @return The [Identifier] with the mod's namespace.
     * @see String.toIdentifier
     * @see Identifier
     */
    fun modId(path: String): Identifier = path.toIdentifier()

    /**
     * Converts a [String] to an [Identifier] using the mod's namespace.
     *
     * @param namespace The namespace for the [Identifier].
     * @return The [Identifier] with the mod's namespace.
     * @see modId
     * @see Identifier
     */
    fun String.toIdentifier(namespace: String = MOD_ID): Identifier = Identifier(namespace, this)

    /**
     * Converts an [Identifier] to a block model [Identifier] with the "block/" prefix.
     *
     * @return The block model [Identifier] with the "block/" prefix.
     */
    fun Identifier.asBlockModelId(): Identifier = this.withPrefixedPath("block/")


    /**
     * Converts a [WoodType] to its corresponding planks [Block].
     *
     * @return The planks [Block] for the given [WoodType].
     */
    fun WoodType.asPlanks(): Block = when (this) {
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