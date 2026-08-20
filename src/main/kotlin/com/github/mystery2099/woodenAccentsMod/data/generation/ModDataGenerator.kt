package com.github.mystery2099.woodenAccentsMod.data.generation

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object ModDataGenerator : DataGeneratorEntrypoint {

    // Item tags copy their matching block tags, so this provider must be registered first.
    lateinit var blockTagGen: BlockTagDataGen
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        fabricDataGenerator.createPack().apply {
            addProvider(::EnglishLangDataGen)
            addProvider(::ModelDataGen)

            addProvider(::BlockLootTableDataGen)
            addProvider(::RecipeDataGen)
            blockTagGen = addProvider(::BlockTagDataGen)
            addProvider(::ItemTagDataGen)
            addProvider(::BiomeTagDataGen)
        }.addProvider(::AdvancementDataGen)
    }
}
