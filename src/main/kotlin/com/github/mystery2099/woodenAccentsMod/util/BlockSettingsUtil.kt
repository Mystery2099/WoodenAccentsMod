package com.github.mystery2099.woodenAccentsMod.util

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.sound.BlockSoundGroup

/**
 * This object provides various pre-configured block settings for different types of blocks.
 */
object BlockSettingsUtil {
    //Wood Blocks
    val oakSettings: FabricBlockSettings = FabricBlockSettings.of(Material.WOOD, MapColor.SPRUCE_BROWN).apply {
        strength(2.0f)
        sounds(BlockSoundGroup.WOOD)
    }
    val spruceSettings = oakSettings.copyWithColor(MapColor.BROWN)
    val birchSettings = oakSettings.copyWithColor(MapColor.OFF_WHITE)
    val jungleSettings = oakSettings.copy()
    val acaciaSettings = oakSettings.copyWithColor(MapColor.STONE_GRAY)
    val cherrySettings = oakSettings.copyWithColor(MapColor.TERRACOTTA_GRAY).apply {
        sounds(BlockSoundGroup.CHERRY_WOOD)
        requires(FeatureFlags.UPDATE_1_20)
    }
    val darkOakSettings = spruceSettings.copy()
    val mangroveSettings = oakSettings.copy()
    val bambooBlockSettings = oakSettings.copyWithColor(MapColor.DARK_GREEN).apply {
        sounds(BlockSoundGroup.BAMBOO)
        requires(FeatureFlags.UPDATE_1_20)
    }

    //Stripped Wood Blocks
    val strippedOakSettings = oakSettings.copyWithColor(MapColor.OAK_TAN)
    val strippedSpruceSettings = oakSettings.copyWithColor(MapColor.SPRUCE_BROWN)
    val strippedBirchSettings = oakSettings.copyWithColor(MapColor.PALE_YELLOW)
    val strippedJungleSettings = oakSettings.copyWithColor(MapColor.DIRT_BROWN)
    val strippedAcaciaSettings = oakSettings.copyWithColor(MapColor.ORANGE)
    val strippedCherrySettings = cherrySettings.copyWithColor(MapColor.TERRACOTTA_PINK)
    val strippedDarkOakSettings = oakSettings.copy()
    val strippedMangroveSettings = oakSettings.copyWithColor(MapColor.RED)
    val strippedBambooBlockSettings = bambooBlockSettings.copyWithColor(MapColor.YELLOW)

    //Stem Blocks
    val warpedSettings: FabricBlockSettings = FabricBlockSettings.of(Material.NETHER_WOOD, MapColor.DARK_AQUA).apply {
        strength(2.0f)
        sounds(BlockSoundGroup.NETHER_STEM)
    }
    val crimsonSettings = warpedSettings.copyWithColor(MapColor.DARK_CRIMSON)

    //Stripped Stem Blocks
    val strippedWarpedSettings = warpedSettings.copyWithColor(MapColor.TEAL)
    val strippedCrimsonSettings = crimsonSettings.copyWithColor(MapColor.DULL_PINK)


    /**
     * Creates a new instance of [FabricBlockSettings] with the specified [mapColor].
     *
     * @param mapColor The [MapColor] to set for the block.
     * @return A new instance of [FabricBlockSettings] with the specified [mapColor].
     */
    fun FabricBlockSettings.copyWithColor(mapColor: MapColor): FabricBlockSettings = this.copy().mapColor(mapColor)



    /**
     * Creates a deep copy of the current [AbstractBlock.Settings] object.
     *
     * @return A new [AbstractBlock.Settings] object that is a copy of the original.
     */
    fun AbstractBlock.Settings.copy(): AbstractBlock.Settings = FabricBlockSettings.copyOf(this)


    /**
     * Creates a new instance of [FabricBlockSettings] that is a copy of the current instance.
     * This method is used to clone a [FabricBlockSettings] object.
     *
     * @return A new instance of [FabricBlockSettings] that is a copy of the current instance.
     */
    fun FabricBlockSettings.copy(): FabricBlockSettings = FabricBlockSettings.copyOf(this)

}