package com.mystery2099.wooden_accents_mod.data.generation.interfaces

import net.minecraft.data.server.recipe.RecipeJsonProvider
import java.util.function.Consumer

interface CustomRecipeProvider {
    infix fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>)
}