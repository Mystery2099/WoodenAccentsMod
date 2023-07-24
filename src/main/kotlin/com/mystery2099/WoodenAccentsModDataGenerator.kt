package com.mystery2099

import com.mystery2099.datagen.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object WoodenAccentsModDataGenerator : DataGeneratorEntrypoint {
	//Must be initiated before ItemTagDataGen!
	lateinit var blockTagGen: BlockTagDataGen
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		with(fabricDataGenerator.createPack()) {
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