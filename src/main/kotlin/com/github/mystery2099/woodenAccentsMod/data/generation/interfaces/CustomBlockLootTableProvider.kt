package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.loot.LootTable

/**
 * The `CustomLootTableProvider` interface allows the ability define custom loot tables for blocks during data generation from within the blocks class.
 *
 * To provide a custom loot table for a [Block], implement this interface and override the [getLootTableBuilder] function
 * to specify the loot table's structure using a [LootTable.Builder].
 * Call [getLootTableBuilder] on the block in a class implementing [FabricBlockLootTableProvider] so it can generate the provided loot table.
 *
 * @see FabricBlockLootTableProvider
 * @see LootTable.Builder
 */
interface CustomBlockLootTableProvider {
    fun getLootTableBuilder(provider: FabricBlockLootTableProvider): LootTable.Builder


}