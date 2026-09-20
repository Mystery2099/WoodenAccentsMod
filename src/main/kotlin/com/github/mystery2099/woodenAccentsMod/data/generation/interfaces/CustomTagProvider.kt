package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.data.generation.BlockTagDataGen
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

/** Adds a registered object to [tag] during data generation. */
interface CustomTagProvider<T> {
    val tag: TagKey<T>
}
