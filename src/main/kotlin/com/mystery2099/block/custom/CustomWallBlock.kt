package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.WallBlock

class CustomWallBlock(val baseBlock: Block) : WallBlock(FabricBlockSettings.copyOf(baseBlock)) {
    init {
        WoodenAccentsModItemGroups.outsideItems += this
    }
}