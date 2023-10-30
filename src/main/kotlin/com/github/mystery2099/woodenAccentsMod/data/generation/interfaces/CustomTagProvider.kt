package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.registry.tag.TagKey

interface CustomTagProvider<T> {
    val tag: TagKey<T>
}