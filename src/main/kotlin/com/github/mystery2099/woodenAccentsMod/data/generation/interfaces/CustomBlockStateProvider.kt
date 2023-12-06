package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.data.client.BlockStateModelGenerator

/**
 * An interface for generating block state models.
 */
interface CustomBlockStateProvider {
    /**
     * Generates block state models using the given BlockStateModelGenerator.
     *
     * @param generator The BlockStateModelGenerator used to generate block state models.
     */
    fun generateBlockStateModels(generator: BlockStateModelGenerator)
}