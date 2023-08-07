package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.WallBlock

class CustomWallBlock(val baseBlock: Block) : WallBlock(FabricBlockSettings.copyOf(baseBlock)), GroupedBlock {
    //init { WoodenAccentsModItemGroups.outsideItems += this }

    override val itemGroup get() = ModItemGroups.outsideBlockItemGroup
}