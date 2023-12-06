package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockLootTableProvider
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
 * The `BlockLootTableDataGen` class is responsible for generating custom loot tables for blocks during data generation.
 * It extends the `FabricBlockLootTableProvider` class, which provides the basic functionality for generating loot tables.
 *
 * @param dataOutput The data output to which the generated loot tables will be written.
 */
class BlockLootTableDataGen(dataOutput: FabricDataOutput) : FabricBlockLootTableProvider(dataOutput) {
    override fun generate() {
        ModBlocks.blocks.forEach { block ->
            when (block) {
                is CustomBlockLootTableProvider -> block.addDrop()
                else -> addDrop(block)
            }
        }
    }

    /**
     * Adds a loot pool to the loot table of a block that drops the block twice if a specific property has a specific value.
     *
     * @param drop The block to add the loot pool to.
     * @param property The property to compare with.
     * @param value The value of the property to match.
     * @param T The type of the property.
     *
     * @throws IllegalArgumentException if T is not Comparable and StringIdentifiable.
     */
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

    private fun CustomBlockLootTableProvider.addDrop() {
        if (this is Block) {
            addDrop(this, this.getLootTableBuilder(this@BlockLootTableDataGen))
        } else {
            WoodenAccentsMod.logger.info("Interface: ${CustomBlockLootTableProvider::class.simpleName} must be used on a class which extends Block!")
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