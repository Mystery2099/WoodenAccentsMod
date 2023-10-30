package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.data.client.BlockStateModelGenerator

interface CustomBlockStateProvider {
    fun generateBlockStateModels(generator: BlockStateModelGenerator)
}