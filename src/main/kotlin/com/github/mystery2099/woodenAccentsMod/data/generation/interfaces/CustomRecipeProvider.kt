package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.RecipeJsonProvider
import java.util.function.Consumer

/**
 * The [CustomRecipeProvider] interface is used for providing custom recipes during data generation.
 *
 * When [ModBlocks.blocks] is looped through in [RecipeDataGen], any [Block] which implements this interface will have it's recipe generated.
 *
 */
interface CustomRecipeProvider {
    /**
     * Offers a recipe to the specified exporter.
     *
     * @param exporter the consumer that provides a RecipeJsonProvider to export the recipe
     */
    infix fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>)
}
