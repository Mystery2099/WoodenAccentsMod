package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.data.generation.BlockTagDataGen
import net.minecraft.block.Block
import net.minecraft.registry.tag.TagKey

/**
 * The [CustomTagProvider] interface is used for adding blocks to tags during data generation.
 *
 * When [ModBlocks.blocks] is looped through in [BlockTagDataGen], any [Block] implementing this interface will be added to the associated tag specified by the [tag] property.
 *
 * @param T The type of the element being added to the [tag].
 *
 */
interface CustomTagProvider<T> {
    /**
     * Gets the [TagKey] to which the implementing [T] should be added during data generation.
     */
    val tag: TagKey<T>
}