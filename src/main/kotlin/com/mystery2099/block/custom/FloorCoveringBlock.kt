package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.CarpetBlock
import net.minecraft.block.Material

class FloorCoveringBlock(val baseBlock: Block) : CarpetBlock(
    FabricBlockSettings.of(Material.CARPET).strength(0.1f).apply {
        mapColor(baseBlock.defaultMapColor)
        sounds(baseBlock.getSoundGroup(baseBlock.defaultState))
    }
) {
    init {
        WoodenAccentsModItemGroups.livingRoomItems += this
    }
}