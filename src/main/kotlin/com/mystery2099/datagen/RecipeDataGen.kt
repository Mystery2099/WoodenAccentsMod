package com.mystery2099.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import java.util.function.Consumer

class RecipeDataGen(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        TODO("Not yet implemented")
    }
}