package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import net.minecraft.block.Block
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess

class ThickPillarBlock(baseBlock: Block) : AbstractPillarBlock(baseBlock, shape) {

    override infix fun WorldAccess.checkUp(pos: BlockPos): Boolean = getUpState(pos).run {
        isIn(ModBlockTags.pillars) || isIn(BlockTags.WALLS)
    }

    override infix fun WorldAccess.checkDown(pos: BlockPos): Boolean  = getDownState(pos).run {
        isIn(ModBlockTags.pillars) || isIn(BlockTags.WALLS)
    }
    companion object {
        @JvmStatic
        val shape = Shape(
            createCuboidShape(1.0, 10.0, 1.0, 15.0, 16.0, 15.0),
            createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0),
            createCuboidShape(1.0, 0.0, 1.0, 15.0, 6.0, 15.0)
        )
    }
}