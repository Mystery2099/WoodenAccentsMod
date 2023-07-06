package com.mystery2099.block.custom

import com.mystery2099.datagen.BlockLootTableDataGen.Companion.dropsSelf
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

class TableBlock(val baseBlock: Block, val topBlock: Block) : AbstractTableBlock(baseBlock, topBlock) {
    init {
        instances += this
        this.dropsSelf()
    }



    override fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        val instances = HashSet<TableBlock>()

    }
}