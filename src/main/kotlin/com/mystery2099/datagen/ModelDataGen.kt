package com.mystery2099.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator

class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        TODO("Not yet implemented")
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        TODO("Not yet implemented")
    }
}