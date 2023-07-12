package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.data.ModBlockTags
import net.minecraft.block.Block
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess

class ThinPillarBlock(baseBlock: Block) : AbstractPillarBlock(baseBlock, size) {

    override fun WorldAccess.checkUp(pos: BlockPos): Boolean {
        val up = this.getUpState(pos)
        return up.isIn(ModBlockTags.thinPillars) || up.isIn(BlockTags.FENCES)
    }

    override fun WorldAccess.checkDown(pos: BlockPos): Boolean {
        val down = this.getDownState(pos)
        return down.isIn(ModBlockTags.thinPillars) || down.isIn(BlockTags.FENCES)
    }
    companion object {
        @JvmStatic
        val size = Size(
            createCuboidShape(4.0, 13.0, 4.0, 12.0, 16.0, 12.0),
            createCuboidShape(6.0, 1.0, 6.0, 10.0, 16.0, 10.0),
            createCuboidShape(4.0, 0.0, 4.0, 12.0, 3.0, 12.0)
        )
    }
    init {
        WoodenAccentsModItemGroups.outsideItems += this

    }
}