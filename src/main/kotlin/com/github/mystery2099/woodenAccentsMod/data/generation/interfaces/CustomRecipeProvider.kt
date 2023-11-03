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
     * Offers custom recipes for data generation by providing them to a [RecipeJsonProvider].
     *
     * @param exporter A Consumer<RecipeJsonProvider> function responsible for accepting and processing the provided recipes.
     */
    infix fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>)
}
