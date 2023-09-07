package com.mystery2099.wooden_accents_mod.data.generation.interfaces

import net.minecraft.data.client.BlockStateModelGenerator

interface CustomBlockStateProvider {
    fun generateBlockStateModels(generator: BlockStateModelGenerator)
}