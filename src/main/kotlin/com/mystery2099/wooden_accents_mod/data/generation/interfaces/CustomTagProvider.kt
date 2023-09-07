package com.mystery2099.wooden_accents_mod.data.generation.interfaces

import net.minecraft.registry.tag.TagKey

interface CustomTagProvider<T> {
    val tag: TagKey<T>
}