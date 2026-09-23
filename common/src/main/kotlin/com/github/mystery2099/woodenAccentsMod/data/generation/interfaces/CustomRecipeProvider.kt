package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.data.recipes.RecipeOutput

/** Lets a registered block keep its recipe definition with the block itself. */
interface CustomRecipeProvider {
    infix fun offerRecipeTo(recipeExporter: RecipeOutput)
}
