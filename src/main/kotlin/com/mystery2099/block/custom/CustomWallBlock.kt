package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.block.custom.interfaces.GroupedBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.WallBlock

class CustomWallBlock(val baseBlock: Block) : WallBlock(FabricBlockSettings.copyOf(baseBlock)), GroupedBlock {
    //init { WoodenAccentsModItemGroups.outsideItems += this }

    override val itemGroup get() = WoodenAccentsModItemGroups.outsideBlockItemGroup
}