package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.data.ModBlockTags
import net.minecraft.block.Block
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess

class ThickPillarBlock(baseBlock: Block) : AbstractPillarBlock(baseBlock, size) {

    override fun WorldAccess.checkUp(pos: BlockPos): Boolean {
        val otherState = this.getUpState(pos)
        return otherState.isIn(ModBlockTags.pillars) || otherState.isIn(BlockTags.WALLS)
    }

    override fun WorldAccess.checkDown(pos: BlockPos): Boolean {
        val otherState = this.getDownState(pos)
        return otherState.isIn(ModBlockTags.pillars) || otherState.isIn(BlockTags.WALLS)
    }
    companion object {
        @JvmStatic
        val size = Size(
            createCuboidShape(1.0, 10.0, 1.0, 15.0, 16.0, 15.0),
            createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0),
            createCuboidShape(1.0, 0.0, 1.0, 15.0, 6.0, 15.0)
        )
    }
    init {
        WoodenAccentsModItemGroups.outsideItems += this
    }
}