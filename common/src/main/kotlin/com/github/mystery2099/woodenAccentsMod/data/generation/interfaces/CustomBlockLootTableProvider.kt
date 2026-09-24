package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.minecraft.core.HolderLookup
import net.minecraft.world.level.storage.loot.LootTable

/** Lets a block define its own loot table alongside its runtime behavior. */
interface CustomBlockLootTableProvider {
    fun getLootTableBuilder(registries: HolderLookup.Provider): LootTable.Builder
}
