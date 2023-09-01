package com.mystery2099.wooden_accents_mod.block.custom.interfaces

import net.minecraft.loot.LootTable

interface CustomLootTableProvider {
    val lootTableBuilder: LootTable.Builder
}