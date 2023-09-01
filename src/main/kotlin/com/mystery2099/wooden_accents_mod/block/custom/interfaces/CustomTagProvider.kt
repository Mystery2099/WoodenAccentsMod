package com.mystery2099.wooden_accents_mod.block.custom.interfaces

import net.minecraft.block.Block
import net.minecraft.registry.tag.TagKey

interface CustomTagProvider {
    val tag: TagKey<Block>
}