package com.mystery2099.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block

class BlockLootTableDataGen(dataOutput: FabricDataOutput) : FabricBlockLootTableProvider(dataOutput) {
    override fun generate() {
        dropsSelf.forEach(::addDrop)
    }

    companion object {
        val dropsSelf = HashSet<Block>()
        fun Block.dropsSelf() {
            dropsSelf.add(this)
        }
    }

}