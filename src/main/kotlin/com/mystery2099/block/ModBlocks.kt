package com.mystery2099.block

import com.mystery2099.WoodenAccentsMod
import com.mystery2099.WoodenAccentsMod.toId
import com.mystery2099.block.custom.ThinPillarBlock
import com.mystery2099.datagen.BlockTagDataGen
import com.mystery2099.datagen.EnglishLangDataGen
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlocks {

    @JvmStatic
    val thinOakPillar = ThinPillarBlock(Blocks.OAK_PLANKS).register("thin_oak_pillar")
    @JvmStatic
    val thinSprucePillar = ThinPillarBlock(Blocks.SPRUCE_PLANKS).register("thin_spruce_pillar")
    @JvmStatic
    val thinBirchPillar = ThinPillarBlock(Blocks.BIRCH_PLANKS).register("thin_birch_pillar")
    @JvmStatic
    val thinJunglePillar = ThinPillarBlock(Blocks.JUNGLE_PLANKS).register("thin_jungle_pillar")
    @JvmStatic
    val thinAcaciaPillar = ThinPillarBlock(Blocks.ACACIA_PLANKS).register("thin_acacia_pillar")
    @JvmStatic
    val thinDarkOakPillar = ThinPillarBlock(Blocks.DARK_OAK_PLANKS).register("thin_dark_oak_pillar")
    @JvmStatic
    val thinMangrovePillar = ThinPillarBlock(Blocks.MANGROVE_PLANKS).register("thin_mangrove_pillar")
    @JvmStatic
    val thinCherryPillar = ThinPillarBlock(Blocks.CHERRY_PLANKS).register("thin_cherry_pillar")
    @JvmStatic
    val thinBambooPillar = ThinPillarBlock(Blocks.BAMBOO_PLANKS).register("thin_bamboo_pillar")
    @JvmStatic
    val thinBambooMosaicPillar = ThinPillarBlock(Blocks.BAMBOO_MOSAIC).register("thin_bamboo_mosaic_pillar")
    @JvmStatic
    val thinCrimsonPillar = ThinPillarBlock(Blocks.CRIMSON_PLANKS).register("thin_crimson_pillar")
    @JvmStatic
    val thinWarpedPillar = ThinPillarBlock(Blocks.WARPED_PLANKS).register("thin_warped_pillar")

    private fun Block.register(id: String): Block {
        return this.register(id.toId())
    }
    private fun Block.register(identifier: Identifier): Block {
        BlockTagDataGen.axeMineable.add(this)
        EnglishLangDataGen.basicNamedBlocks.add(this)
        Registry.register(Registries.ITEM, identifier, BlockItem(this, FabricItemSettings()))
        return Registry.register(Registries.BLOCK, identifier, this)
    }

    fun register() {
        WoodenAccentsMod.logger.info("Registering ModBlocks for mod: ${WoodenAccentsMod.modid}")
    }

}