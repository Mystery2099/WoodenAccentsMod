package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.tags.TagKey

/** Adds a registered object to [tag] during data generation. */
interface CustomTagProvider<T> {
    val tag: TagKey<T>
}
