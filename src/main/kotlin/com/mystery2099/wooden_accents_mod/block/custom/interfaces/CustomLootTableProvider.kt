package com.mystery2099.wooden_accents_mod.block.custom.interfaces

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.loot.LootTable

interface CustomLootTableProvider {
    fun getLootTableBuilder(provider: FabricBlockLootTableProvider): LootTable.Builder
}