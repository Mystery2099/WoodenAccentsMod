package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.world.level.block.Block
import net.minecraft.data.recipes.FinishedRecipe
import java.util.function.Consumer

/** Lets a registered block keep its recipe definition with the block itself. */
interface CustomRecipeProvider {
    infix fun offerRecipeTo(exporter: Consumer<FinishedRecipe>)
}
