package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.loot.LootTable

/** Lets a block define its own loot table alongside its runtime behavior. */
interface CustomBlockLootTableProvider {
    fun getLootTableBuilder(provider: FabricBlockLootTableProvider): LootTable.Builder


}
