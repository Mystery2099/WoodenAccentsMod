package com.github.mystery2099.woodenAccentsMod

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/** Loader-agnostic core shared by every mod loader. */
object WoodenAccentsMod {
    internal const val MOD_ID = "wooden_accents_mod"

    internal val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    fun String.toIdentifier(namespace: String = MOD_ID): ResourceLocation = ResourceLocation(namespace, this)

    fun ResourceLocation.withBlockModelPath(): ResourceLocation = this.withPrefix("block/")

    /**
     * Maps vanilla wood types to their plank blocks. Unknown or modded types fall back to oak because
     * Minecraft does not expose this relationship through a registry.
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
