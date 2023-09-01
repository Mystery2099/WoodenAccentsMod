package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.CoffeeTableBlock
import com.mystery2099.wooden_accents_mod.block.custom.CrateBlock
import com.mystery2099.wooden_accents_mod.block_entity.ModBlockEntities
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.BlockStatePropertyLootCondition
import net.minecraft.loot.entry.DynamicEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.entry.LootPoolEntry
import net.minecraft.loot.function.CopyNameLootFunction
import net.minecraft.loot.function.CopyNbtLootFunction
import net.minecraft.loot.function.SetContentsLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.predicate.StatePredicate
import net.minecraft.state.property.Property
import net.minecraft.util.StringIdentifiable

class BlockLootTableDataGen(dataOutput: FabricDataOutput) : FabricBlockLootTableProvider(dataOutput) {
    override fun generate() {
        ModBlocks.blocks.forEach { block ->
            when (block) {
                is CrateBlock -> addDrop(block, crateDrops(block))
                !is CoffeeTableBlock -> addDrop(block)
            }
        }
    }

    private fun crateDrops(drop: CrateBlock): LootTable.Builder {
        return LootTable.builder().pool(
            addSurvivesExplosionCondition(
                drop, LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(
                    ((ItemEntry.builder(drop)
                        .apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY)) as LeafEntry.Builder<*>).apply(
                        CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY)
                            .withOperation("Lock", "BlockEntityTag.Lock")
                            .withOperation("LootTable", "BlockEntityTag.LootTable")
                            .withOperation("LootTableSeed", "BlockEntityTag.LootTableSeed")
                    ) as LeafEntry.Builder<*>).apply(
                        SetContentsLootFunction.builder(ModBlockEntities.crate)
                            .withEntry(DynamicEntry.builder(CrateBlock.contents))
                    ) as Any as LootPoolEntry.Builder<*>
                )
            )
        )
    }

    private fun <T> addDropsDoubleWithProperty(
        drop: Block,
        property: Property<T>,
        value: T
    ) where T : Comparable<T>, T : StringIdentifiable {
        LootTable.builder().pool(
            LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(
                applyExplosionDecay(
                    drop, ItemEntry.builder(drop).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0f))
                        .conditionally(BlockStatePropertyLootCondition.builder(drop)
                            .properties(StatePredicate.Builder.create().exactMatch(property, value))
                        )
                    )
                )
            )
        ).also { addDrop(drop, it) }
    }
}