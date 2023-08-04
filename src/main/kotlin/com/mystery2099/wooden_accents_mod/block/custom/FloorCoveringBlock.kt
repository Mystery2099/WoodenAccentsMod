package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.ModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.CarpetBlock
import net.minecraft.block.Material
import net.minecraft.resource.featuretoggle.FeatureFlags

class FloorCoveringBlock(val baseBlock: Block) : CarpetBlock(
    FabricBlockSettings.of(Material.CARPET).strength(0.1f).apply {
        mapColor(baseBlock.defaultMapColor)
        sounds(baseBlock.getSoundGroup(baseBlock.defaultState))
        if (baseBlock.requiredFeatures.contains(FeatureFlags.UPDATE_1_20)) {
            requires(FeatureFlags.UPDATE_1_20)
        }
    }
), GroupedBlock {
    //init { WoodenAccentsModItemGroups.livingRoomItems += this }

    override val itemGroup get() = ModItemGroups.livingRoomItemGroup
}