package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.data.client.BlockStateModelGenerator

/** Lets a block generate the models and block states it owns. */
interface CustomBlockStateProvider {
    fun generateBlockStateModels(generator: BlockStateModelGenerator)
}
