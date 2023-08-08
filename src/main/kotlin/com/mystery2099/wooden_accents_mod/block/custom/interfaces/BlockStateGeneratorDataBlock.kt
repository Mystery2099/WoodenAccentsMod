package com.mystery2099.wooden_accents_mod.block.custom.interfaces

import net.minecraft.data.client.BlockStateModelGenerator

interface BlockStateGeneratorDataBlock {
    fun generateBlockStateModels(generator: BlockStateModelGenerator)
}