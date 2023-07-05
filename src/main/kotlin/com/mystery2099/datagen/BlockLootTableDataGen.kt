package com.mystery2099.datagen

import com.mystery2099.block.custom.CoffeeTableBlock
import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.BlockStatePropertyLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.predicate.StatePredicate
import net.minecraft.state.property.Property
import net.minecraft.util.StringIdentifiable

class BlockLootTableDataGen(dataOutput: FabricDataOutput) : FabricBlockLootTableProvider(dataOutput) {
    override fun generate() {
        dropsSelf.forEach(::addDrop)
        CoffeeTableBlock.instances.forEach{
            addDropsDoubleWithProperty(it, ModProperties.coffeeTableType, CoffeeTableType.TALL)
        }
    }

    companion object {
        val dropsSelf = HashSet<Block>()
        fun Block.dropsSelf() {
            dropsSelf.add(this)
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