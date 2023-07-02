package com.mystery2099

import com.mystery2099.datagen.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object WoodenAccentsModDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		with(fabricDataGenerator.createPack()) {
			//Assets
			addProvider(::EnglishLangDataGen)
			addProvider(::ModelDataGen)

			//Data
			addProvider(::BlockLootTableDataGen)
			addProvider(::RecipeDataGen)
			WoodenAccentsUtil.setBlockTagGen(addProvider(::BlockTagDataGen))
			addProvider(::ItemTagDataGen)
			addProvider(::BiomeTagDataGen)
		}
	}
}