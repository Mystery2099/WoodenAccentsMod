package com.mystery2099.wooden_accents_mod.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object ModDataGenerator : DataGeneratorEntrypoint {
	//Must be initiated before ItemTagDataGen!
	lateinit var blockTagGen: BlockTagDataGen
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
		}
	}
}