package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomLootTableProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.BlockStatePropertyLootCondition
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.ConditionalLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.predicate.StatePredicate
import net.minecraft.state.property.Property
import net.minecraft.util.StringIdentifiable

/**
 * Block loot table data gen
 *
 * @constructor
 *
 * @param dataOutput
 */
class BlockLootTableDataGen(dataOutput: FabricDataOutput) : FabricBlockLootTableProvider(dataOutput) {
    override fun generate() {
        ModBlocks.blocks.forEach { block ->
            when (block) {
                is CustomLootTableProvider -> block.addDrop()
                else -> addDrop(block)
            }
        }
    }

    private fun <T> addDropsDoubleWithProperty(
        drop: Block,
        property: Property<T>,
        value: T
    ) where T : Comparable<T>, T : StringIdentifiable {
        LootTable.builder().pool(
            LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(
                applyExplosionDecay(
                    drop, ItemEntry.builder(drop).apply(
                        SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0f))
                            .conditionally(
                                BlockStatePropertyLootCondition.builder(drop)
                                    .properties(StatePredicate.Builder.create().exactMatch(property, value))
                            )
                    )
                )
            )
        ).also { addDrop(drop, it) }
    }

    private fun CustomLootTableProvider.addDrop() {
        if (this is Block) {
            addDrop(this, this.getLootTableBuilder(this@BlockLootTableDataGen))
        } else {
            WoodenAccentsMod.logger.info("Interface: ${CustomLootTableProvider::class.simpleName} must be used on a class which extends Block!")
        }
    }
}

/**
 * Conditionally
 *
 * @param t
 * @param builders
 * @return
 */
fun <t : ConditionalLootFunction.Builder<*>> t.conditionally(vararg builders: LootCondition.Builder): t {
    builders.forEach { this.conditionally(it) }
    return this
}