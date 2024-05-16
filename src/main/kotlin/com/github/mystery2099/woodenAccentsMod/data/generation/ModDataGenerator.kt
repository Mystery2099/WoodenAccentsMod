package com.github.mystery2099.woodenAccentsMod.data.generation

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

/**
 * The object used to generate assets and data for the Wooden Accents Mod
 */
object ModDataGenerator : DataGeneratorEntrypoint {

    lateinit var blockTagGen: BlockTagDataGen // Must be initiated before ItemTagDataGen!
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        fabricDataGenerator.createPack().apply {
            //Assets
            addProvider(::EnglishLangDataGen)
            addProvider(::ModelDataGen)

            //Data
            addProvider(::BlockLootTableDataGen)
            addProvider(::RecipeDataGen)
            blockTagGen = addProvider(::BlockTagDataGen)
            addProvider(::ItemTagDataGen)
            addProvider(::BiomeTagDataGen)
        }.addProvider(::AdvancementDataGen)
    }
}