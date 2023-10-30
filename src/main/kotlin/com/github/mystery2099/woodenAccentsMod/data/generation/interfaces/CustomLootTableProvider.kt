package com.github.mystery2099.woodenAccentsMod.data.generation.interfaces

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.loot.LootTable

interface CustomLootTableProvider {
    fun getLootTableBuilder(provider: FabricBlockLootTableProvider): LootTable.Builder
}