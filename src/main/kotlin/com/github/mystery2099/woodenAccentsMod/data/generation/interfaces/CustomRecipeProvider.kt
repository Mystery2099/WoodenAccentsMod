package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.data.server.recipe.RecipeJsonProvider
import java.util.function.Consumer

interface CustomRecipeProvider {
    infix fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>)
}